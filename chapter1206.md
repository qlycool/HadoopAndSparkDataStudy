# 6 Hbase 数据备份及恢复

## 1 简介

&#160; &#160; &#160; &#160;若在生产环境中使用HBase,必须了解备份HBase的各种可选方案和操作方法.备份HBase时的难点是其待备份的数据集可能非常巨大,因此备份方案必须有很高的效果.HBase备份方案必须即能够伸缩至对数百TB的存储容量进行备份,又能够在一个合理的时间范围内完成数据恢复的工作.

备份HBase有两种策略

1. 关闭集群后进行全备份
2. 在线对集群进行备份

&#160; &#160; &#160; &#160;在进行关机全备份时,必须首先关闭HBase(或禁用所有表),然后使用Hadoop的distcp命令将HBase目录中的内容复制到同一(或另一)HDFS中的其他目录中.在使用关机全备分进行恢复时,只要使用distcp命令将所备份的文件复制回原HBase目录就可以了.

在线备份集群也有以下几种方法.

1. 使用copytable工具来将一张表的数据复制到另一张表中
2. 将HBase表导出为HDFS文件,然后再将文件导入到HBase中
3. HBase集群复制

&#160; &#160; &#160; &#160;CopyTable实用程序可以用来在一张表与本集群(或其他集群)中的另一张表之间复制数据,Export实用程序可将某张表中的数据转储到本集群的HDFS中.作为Export的搭档,Import实用程序可用来恢复转储文件的数据.


&#160; &#160; &#160; &#160;上述每种方法都有其自身的优缺点.关机全备份的优点是在备份过程中集群不会有数据写入,因此它能够确保备份的一致性.其不足之处也很显而易见,那就是要关闭集群.对于在线备份集群的方式来说,因为集群处于运行状态,所以有在备份过程中丢失某些数据修改的风险.此外,HBASE的数据修改只是一个行级的原子操作,如果你的表是彼此依赖的,而且在执行Export或CopyTable的过程中表数据发生了修改,那么所生成的备份就可能有不一致的问题.在当前Apache发布的版本中,HBASE还不支持对表建立快照.

&#160; &#160; &#160; &#160;HBASE支持集群复制的功能,这是一种在不同HBASE部署之间复制数据的方法.集群复制可以视为一种HBASE级的灾难恢复解决方案.

&#160; &#160; &#160; &#160;除了表以外,你可能还要对HDFS元数据和HBASE区域的开始键进行备分.HDFS元数据包括HDFS文件系统的映像和提交日志.一处元数据的损坏有可能毁掉整个HDFS元数据,因此建议经常对元数据进行备份.区域开始键代表了的数据在HBASE中分布情况.如果备份了区域的开始键,那么在恢复时不仅可以恢复数据,而且还可以对数据分布情况进行恢复,如果能预先使用均匀分布的区域开始键对表进行分割,那么使用CopyTable或Import恢复数据的速度就会有很大的提高.

## 2 使用distcp进行关机全备份

&#160; &#160; &#160; &#160;disccp(distributed copy 分布式复制)是由Hadoop提供的一个用于在同一HDFS集群或者不同HDFS集群之间复制大型数据集,它使用MapReduce来并行复制文件/处理错误并进行恢复/报告任务运行状态.

&#160; &#160; &#160; &#160;HBASE的所有文件(包括系统文件)存储在HDFS上,因此只要使用distcp将HBASE目录复制到同一HDFS或者其他HDFS的另一个目录中,就可以完成对源HBASE集群的备分工作.

&#160; &#160; &#160; &#160;HBASE请注意,这是一种关机情况下的全备份方案,我们可以使用distcp工具来进行备份的原因是HBASE集群已被关闭(或所有表都已被禁用),因此在备份过程中不会有对文件的修改操作.不要在运行中的HBASE集群上使用distcp.因此这种解决方案适合那种允许对HBASE集群进行周期性关闭的环境.例如一个仅用于后端批处理业务而不对前端请求进行响应的集群.



1 首先启动HBASE,查看文件存放位置

```
hadoop@Master:~$ start-dfs.sh
Starting namenodes on [Master]
Master: starting namenode, logging to /usr/local/hadoop/logs/hadoop-hadoop-namenode-Master.out
Slave2: starting datanode, logging to /usr/local/hadoop/logs/hadoop-hadoop-datanode-Slave2.out
Slave1: starting datanode, logging to /usr/local/hadoop/logs/hadoop-hadoop-datanode-Slave1.out
Starting secondary namenodes [Master]
Master: starting secondarynamenode, logging to /usr/local/hadoop/logs/hadoop-hadoop-secondarynamenode-Master.out
hadoop@Master:~$ start-yarn.sh
starting yarn daemons
starting resourcemanager, logging to /usr/local/hadoop/logs/yarn-hadoop-resourcemanager-Master.out
Slave2: starting nodemanager, logging to /usr/local/hadoop/logs/yarn-hadoop-nodemanager-Slave2.out
Slave1: starting nodemanager, logging to /usr/local/hadoop/logs/yarn-hadoop-nodemanager-Slave1.out
hadoop@Master:~$ mr-jobhistory-daemon.sh start historyserver
starting historyserver, logging to /usr/local/hadoop/logs/mapred-hadoop-historyserver-Master.out
hadoop@Master:~$ start-hbase.sh
Slave2: starting zookeeper, logging to /usr/local/hbase/bin/../logs/hbase-hadoop-zookeeper-Slave2.out
Slave1: starting zookeeper, logging to /usr/local/hbase/bin/../logs/hbase-hadoop-zookeeper-Slave1.out
Master: starting zookeeper, logging to /usr/local/hbase/bin/../logs/hbase-hadoop-zookeeper-Master.out
starting master, logging to /usr/local/hbase/logs/hbase-hadoop-master-Master.out
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=128m; support was removed in 8.0
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=128m; support was removed in 8.0
Slave1: starting regionserver, logging to /usr/local/hbase/bin/../logs/hbase-hadoop-regionserver-Slave1.out
Slave2: starting regionserver, logging to /usr/local/hbase/bin/../logs/hbase-hadoop-regionserver-Slave2.out
Slave1: Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=128m; support was removed in 8.0
Slave1: Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=128m; support was removed in 8.0
Slave2: Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=128m; support was removed in 8.0
Slave2: Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=128m; support was removed in 8.0
hadoop@Master:~$ 

```

2 如果源HBASE集群和备份HBASE集群已经启动了,请将其关闭,检查HMASTER守护进程是否已启动,以确认源集群上的HBASE已被关闭.我们把HBASE目录备份到集群上的/backup目录中.请在备份集群的Hadoop客户端上使用如下命令预先创建好该目录.

```
hadoop@Master:~$ hadoop fs -mkdir /backup
hadoop@Master:~$ hadoop fs -ls
Found 7 items
drwxr-xr-x   - hadoop supergroup          0 2016-02-25 18:41 .sparkStaging
drwxr-xr-x   - hadoop supergroup          0 2016-03-03 02:24 cdsgus
drwxr-xr-x   - hadoop supergroup          0 2016-01-27 21:32 chu888chu888
drwxrwxr-x   - hadoop supergroup          0 2016-01-28 18:54 hive
drwxr-xr-x   - hadoop supergroup          0 2016-01-27 22:15 input
drwxr-xr-x   - hadoop supergroup          0 2016-01-27 22:19 output
drwxrwxr-x   - hadoop supergroup          0 2016-01-28 18:53 tmp
hadoop@Master:~$ hadoop fs -ls /
Found 5 items
drwxr-xr-x   - hadoop supergroup          0 2016-05-07 00:44 /backup
drwxr-xr-x   - hadoop supergroup          0 2016-04-30 00:23 /hbase
drwxr-xr-x   - hadoop supergroup          0 2016-04-03 18:43 /input
drwxrwx---   - hadoop supergroup          0 2016-01-28 19:16 /tmp
drwxr-xr-x   - hadoop supergroup          0 2016-03-03 01:00 /user

hadoop@Master:~$ stop-hbase.sh

hadoop@Master:~$ jps
1984 JobHistoryServer
1553 SecondaryNameNode
2290 Jps
1698 ResourceManager
1336 NameNode
hadoop@Master:~$ 

请确认上述输出结果中未列出HMaster守护进程
```

3 使用distcp命令从源集群将HBASE根目录复制到备份集群中.HBASE的根目录由HBASE配置文件(hbase-site.xml)中的hbase.rootdir属性所指定.

```
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://Master:9000/hbase</value>
  </property>
  <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
  </property>   
  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>Master,Slave1,Slave2</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/hadoop</value>
  </property>
</configuration>

hadoop@Master:/$ hadoop distcp hdfs://Master:9000/hbase hdfs://Master:9000/backup
16/05/07 01:02:37 INFO tools.DistCp: Input Options: DistCpOptions{atomicCommit=false, syncFolder=false, deleteMissing=false, ignoreFailures=false, maxMaps=20, sslConfigurationFile='null', copyStrategy='uniformsize', sourceFileListing=null, sourcePaths=[hdfs://Master:9000/hbase], targetPath=hdfs://Master:9000/backup, targetPathExists=true, preserveRawXattrs=false}
16/05/07 01:02:37 INFO client.RMProxy: Connecting to ResourceManager at Master/192.168.1.80:8032
16/05/07 01:02:38 INFO Configuration.deprecation: io.sort.mb is deprecated. Instead, use mapreduce.task.io.sort.mb
16/05/07 01:02:38 INFO Configuration.deprecation: io.sort.factor is deprecated. Instead, use mapreduce.task.io.sort.factor
16/05/07 01:02:38 INFO client.RMProxy: Connecting to ResourceManager at Master/192.168.1.80:8032
16/05/07 01:02:39 INFO mapreduce.JobSubmitter: number of splits:5
16/05/07 01:02:39 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1462550883601_0002
16/05/07 01:02:39 INFO impl.YarnClientImpl: Submitted application application_1462550883601_0002
16/05/07 01:02:39 INFO mapreduce.Job: The url to track the job: http://Master:8088/proxy/application_1462550883601_0002/
16/05/07 01:02:39 INFO tools.DistCp: DistCp job-id: job_1462550883601_0002
16/05/07 01:02:39 INFO mapreduce.Job: Running job: job_1462550883601_0002
16/05/07 01:02:44 INFO mapreduce.Job: Job job_1462550883601_0002 running in uber mode : false
16/05/07 01:02:44 INFO mapreduce.Job:  map 0% reduce 0%
16/05/07 01:02:55 INFO mapreduce.Job:  map 40% reduce 0%
16/05/07 01:02:56 INFO mapreduce.Job:  map 60% reduce 0%
16/05/07 01:02:57 INFO mapreduce.Job:  map 76% reduce 0%
16/05/07 01:02:58 INFO mapreduce.Job:  map 80% reduce 0%
16/05/07 01:03:01 INFO mapreduce.Job:  map 100% reduce 0%
16/05/07 01:03:02 INFO mapreduce.Job: Job job_1462550883601_0002 completed successfully
16/05/07 01:03:03 INFO mapreduce.Job: Counters: 33
	File System Counters
		FILE: Number of bytes read=0
		FILE: Number of bytes written=539820
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=345874159
		HDFS: Number of bytes written=345774967
		HDFS: Number of read operations=2275
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=587
	Job Counters 
		Launched map tasks=5
		Other local map tasks=5
		Total time spent by all maps in occupied slots (ms)=49409
		Total time spent by all reduces in occupied slots (ms)=0
		Total time spent by all map tasks (ms)=49409
		Total vcore-seconds taken by all map tasks=49409
		Total megabyte-seconds taken by all map tasks=50594816
	Map-Reduce Framework
		Map input records=404
		Map output records=0
		Input split bytes=680
		Spilled Records=0
		Failed Shuffles=0
		Merged Map outputs=0
		GC time elapsed (ms)=996
		CPU time spent (ms)=8070
		Physical memory (bytes) snapshot=582979584
		Virtual memory (bytes) snapshot=9369141248
		Total committed heap usage (bytes)=162856960
	File Input Format Counters 
		Bytes Read=98512
	File Output Format Counters 
		Bytes Written=0
	org.apache.hadoop.tools.mapred.CopyMapper$Counter
		BYTESCOPIED=345774967
		BYTESEXPECTED=345774967
		COPY=404
hadoop@Master:/$ 
```

4 如果想把数据恢复直接拷贝回去即可

## 3 使用CopyTable在表间复制数据

CopyTable是一个实用程序,它可将一张表中的数据复制到同一个集群或其他HBASE集群的另一张表中.你可以仅将数据复制到同一集群的另一张表中,但如果还有其他可做为备份的集群,你可能更愿意以一种联机备份的方式用CopyTable来将表中的数据复制到备份集群中.

CopyTable还可以带开始和结束时间两个参数.如果指定开始时间和结束时间,该命令就会只对那些时间戳在指定时间范围内的数据进行复制.这一特性使该命令在某些情况下可以对HBASE表进行增量备份.

```

```

## 4 将HBase 表导出为HDFS上的转储文件

## 5 通过从HDFS导入转储文件来恢复HBASE数据


