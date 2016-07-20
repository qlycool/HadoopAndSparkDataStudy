# Hadoop组件

##HDFS文件操作
&#160; &#160; &#160; &#160;HDFS是一种文件系统,专为MapReduce这类框架下的大规模分布式数据处理而设计,你可以把一个大数据集(比如说100TB)在HDFS中存储为单个文件,而大多数其他的文件系统无力实现这一点.
&#160; &#160; &#160; &#160;HDFS并不是一个天生的UNIX文件系统,不支持像ls和cp这种标准的UNIX文件命令,也不支持如fopen()和fread()这样的标准文件读写操作.另一方面,Hadoop确也提供了一套与Linux文件命令类似的命令行工具.
###基本文件命令
Hadoop的文件命令采取的形式为
```hadoop fs -cmd <args>```  

基中cmd是具体的文件命令,而```<args>```是一组数据可变的参数.```cmd```的命名通常与unix对应的命令名相同.例如,文件形表命令为  
```hadoop fs -ls```  
```
hadoop fs -mkdir /user/chuck
hadoop fs -ls /
hadoop fs -put example.txt /user/chuck
hadoop fs -ls
hadoop fs -get example.txt .
hadoop fs -cat example.txt
hadoop fs -rm  example.txt


```
###cat命令
```
将路径指定文件的内容输出到stdout
hadoop@Master:~$ hadoop dfs -cat input/core-site.xml
```
###chgrp命令
```
改变文件所属组.使用-R将使改变在目录结构下递归进行.命令的使用者必须是文件的所有者或者超级用户.
```
###chmod命令
```
修改文件权限
```
###chown命令
```
改变文件的拥有者
```
###cp命令

```
将文件从源路径复制到目标路径,这个命令允许有多个源路径,此时目标路径必须是一个目录
hadoop@Master:/usr/local/hadoop/etc/hadoop$ hdfs dfs -cp input/* output/
hadoop@Master:/usr/local/hadoop/etc/hadoop$ hdfs dfs -ls output
Found 11 items
-rw-r--r--   1 hadoop supergroup          0 2016-01-25 22:14 output/_SUCCESS
-rw-r--r--   1 hadoop supergroup       4436 2016-01-27 19:16 output/capacity-scheduler.xml
-rw-r--r--   1 hadoop supergroup       1072 2016-01-27 19:16 output/core-site.xml
-rw-r--r--   1 hadoop supergroup       9683 2016-01-27 19:16 output/hadoop-policy.xml
-rw-r--r--   1 hadoop supergroup       1257 2016-01-27 19:16 output/hdfs-site.xml
-rw-r--r--   1 hadoop supergroup        620 2016-01-27 19:16 output/httpfs-site.xml
-rw-r--r--   1 hadoop supergroup       3523 2016-01-27 19:16 output/kms-acls.xml
-rw-r--r--   1 hadoop supergroup       5511 2016-01-27 19:16 output/kms-site.xml
-rw-r--r--   1 hadoop supergroup       1103 2016-01-27 19:16 output/mapred-site.xml
-rw-r--r--   1 hadoop supergroup        107 2016-01-25 22:14 output/part-r-00000
-rw-r--r--   1 hadoop supergroup        924 2016-01-27 19:16 output/yarn-site.xml

```
###du命令

显示目录中所有文件的大小,或者当只指定一个文件时,显示此文件的大小.
```
hadoop@Master:/usr/local/hadoop/etc/hadoop$ hadoop fs -du input/core-site.xml
```
###expunge命令
清空回收站
除了文件权限之外,还有一个保护机制可以防止在HDFS上意外删除文件,这就是回收站,默认情况下该功能是被禁用.当它启用后,用于删除的命令行不会立即删除文件.  
相反它们会暂时的把文件移动到用户工作目录下的.Trash文件夹下.若要启用回收站功能并设置清空回收站的时间延迟,可能通过设置core-site.xml的fs.trash.interval属性(以分钟为单位).    
例如如果你希望用户有24个小时的时间来还原已删除的文件,就应该在core-site.xml中设置.  
如果将该值设置为0,则将禁用回收站的功能

```
<property>
   <name>fs.trash.interval</name>
   <value>1440</value>
</property>
```


###get命令

复制文件到本地文件系统.
```
hadoop fs -get input/hadoop.tar.gz ~/
```

###lsr命令

ls命令的递归版本,类似于Unix中的ls -R
###mkdir命令

接受路径指定的uri作为参数,创建这些目录,其行为类似于Unix的mkdir -p,它会创建路径中的各级父目录.
```
hadoop fs -mkdir /user/hadoop/dir1 /user/hadoop/dir2

```
###mv命令

将文件从源路径移动到目标路径
```
hadoop fs -mv /user/hadoop/file1 /user/hadoop/file2
```
###put命令

从本地文件系统中复制单个或者多个源路径到目标文件系统.也支持从标准输入中读取输入写入目标文件系统.
```
hadoop fs -put /tmp/*.xml /user/hadoop/
```

###rmr命令
```
hadoop fs -rmr /user/hadoop/chu888chu888
```
###job命令

```
 * Job操作
 * 提交MapReduce Job, Hadoop所有的MapReduce Job都是一个jar包
 * $ hadoop jar <local-jar-file> <java-class> <hdfs-input-file> <hdfs-output-dir>
 * $ hadoop jar sandbox-mapred-0.0.20.jar sandbox.mapred.WordCountJob /user/cl/input.dat /user/cl/outputdir
 *
 * 杀死某个正在运行的Job
 * 假设Job_Id为：job_201207121738_0001
 * $ hadoop job -kill job_201207121738_0001
```

###系统体检
Hadoop提供的文件系统检查工具叫做fsck,如参数为文件路径时,它会递归检查该路径下所有文件的健康状态,如果参数为/,它就会检查整个文件系统,如下输出一个例子.
```
hadoop@Master:~$ hadoop fsck /
DEPRECATED: Use of this script to execute hdfs command is deprecated.
Instead use the hdfs command for it.

16/01/27 22:55:14 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Connecting to namenode via http://Master:50070
FSCK started by hadoop (auth:SIMPLE) from /192.168.1.80 for path / at Wed Jan 27 22:55:15 CST 2016
.....................Status: HEALTHY
 Total size:	878899 B
 Total dirs:	21
 Total files:	21
 Total symlinks:		0
 Total blocks (validated):	20 (avg. block size 43944 B)
 Minimally replicated blocks:	20 (100.0 %)
 Over-replicated blocks:	0 (0.0 %)
 Under-replicated blocks:	0 (0.0 %)
 Mis-replicated blocks:		0 (0.0 %)
 Default replication factor:	1
 Average block replication:	1.0
 Corrupt blocks:		0
 Missing replicas:		0 (0.0 %)
 Number of data-nodes:		2
 Number of racks:		1
FSCK ended at Wed Jan 27 22:55:15 CST 2016 in 32 milliseconds


The filesystem under path '/' is HEALTHY

```
###编程读写HDFS
