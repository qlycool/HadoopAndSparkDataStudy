# 2.Sqoop1的安装

####解压安装
```
hadoop@Master:~$ sudo tar xvfz sqoop-1.4.6.bin__hadoop-2.0.4-alpha.tar.gz 

hadoop@Master:~$ sudo mv sqoop-1.4.6.bin__hadoop-2.0.4-alpha /usr/local/sqoop1/
hadoop@Master:~$ sudo chmod -R 775 /usr/local/sqoop1
hadoop@Master:~$ sudo chown -R hadoop:hadoop /usr/local/sqoop1

```

####修改环境变量

```
hadoop@Master:~$ sudo nano /etc/profile
#sqoop
export SQOOP_HOME=/usr/local/sqoop1
export PATH=$SQOOP_HOME/bin:$PATH
 
hadoop@Master:~$ source /etc/profile


```

####配置sqoop的环境变量
```
hadoop@Master:/usr/local/sqoop1/conf$ cp sqoop-env-template.sh sqoop-env.sh

# 指定各环境变量的实际配置
# Set Hadoop-specific environment variables here.

#Set path to where bin/hadoop is available
#export HADOOP_COMMON_HOME=

#Set path to where hadoop-*-core.jar is available
#export HADOOP_MAPRED_HOME=

#set the path to where bin/hbase is available
#export HBASE_HOME=

#Set the path to where bin/hive is available
#export HIVE_HOME=

但是一般情况下我们的/etc/profile已经配置相关的环境变量

export JAVA_HOME=/usr/lib/jvm/
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/local/hive/lib
export PATH=${JAVA_HOME}/bin:$PATH

#HADOOP VARIABLES START
export JAVA_HOME=/usr/lib/jvm/
export HADOOP_INSTALL=/usr/local/hadoop
export PATH=$PATH:$HADOOP_INSTALL/bin
export PATH=$PATH:$JAVA_HOME/bin
export PATH=$PATH:$HADOOP_INSTALL/sbin
export HADOOP_MAPRED_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_HOME=$HADOOP_INSTALL
export HADOOP_HDFS_HOME=$HADOOP_INSTALL
export YARN_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_INSTALL/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_INSTALL/lib"
#HADOOP VARIABLES END


export HIVE_HOME=/usr/local/hive
export PATH=$PATH:$HIVE_HOME/bin:/usr/local/hbase/bin

export JAVA_LIBRARY_PATH=/usr/local/hadoop/lib/native

export SCALA_HOME=/usr/lib/scala
export PATH=$PATH:$SCALA_HOME/bin

#sqoop
export SQOOP_HOME=/usr/local/sqoop1
export PATH=$SQOOP_HOME/bin:$PATH

#HBASE
export HBASE_HOME=/usr/local/hbase


```

####开始测试
```
需要拷贝mysql的驱动到lib下面
hadoop@Master:~/mysql-connector-java-5.0.8$ sudo cp mysql-connector-java-5.0.8-bin.jar /usr/local/sqoop1/lib/

以mysql为例子
IP:192.168.1.178
用户名:chu888chu888
密码:skybar
数据库:hivetestdb
表:cdsgus

[root@hadoop01 ~]# sqoop help
Available commands:
  codegen            Generate code to interact with database records
  create-hive-table  Import a table definition into Hive
  eval               Evaluate a SQL statement and display the results
  export             Export an HDFS directory to a database table
  help               List available commands
  import             Import a table from a database to HDFS
  import-all-tables  Import tables from a database to HDFS
  import-mainframe   Import datasets from a mainframe server to HDFS
  job                Work with saved jobs
  list-databases     List available databases on a server
  list-tables        List available tables in a database
  merge              Merge results of incremental imports
  metastore          Run a standalone Sqoop metastore
  version            Display version information



列出所有的数据库
hadoop@Master:/usr/local/sqoop1/lib$ sqoop list-databases --connect jdbc:mysql://192.168.1.178 --username chu888chu888 --password skybar

列出数据库中所有的表
hadoop@Master:/usr/local/sqoop1/lib$ sqoop list-tables --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar

导出mysql表到hdfs上
hadoop@Master:/$ hdfs dfs -mkdir /user/sqoop
hadoop@Master:/$ hdfs dfs -chown sqoop:hadoop /user/sqoop
sqoop import  --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar --table cdsgus --m 2 --target-dir /user/sqoop/cdsgus

hadoop@Master:~$ sqoop import  --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar --table cdsgus --m 2 --target-dir /user/sqoop/cdsgus
Warning: /usr/local/sqoop1/../hcatalog does not exist! HCatalog jobs will fail.
Please set $HCAT_HOME to the root of your HCatalog installation.
Warning: /usr/local/sqoop1/../accumulo does not exist! Accumulo imports will fail.
Please set $ACCUMULO_HOME to the root of your Accumulo installation.
Warning: /usr/local/sqoop1/../zookeeper does not exist! Accumulo imports will fail.
Please set $ZOOKEEPER_HOME to the root of your Zookeeper installation.
16/03/03 01:28:13 INFO sqoop.Sqoop: Running Sqoop version: 1.4.6
16/03/03 01:28:13 WARN tool.BaseSqoopTool: Setting your password on the command-line is insecure. Consider using -P instead.
16/03/03 01:28:13 INFO manager.MySQLManager: Preparing to use a MySQL streaming resultset.
16/03/03 01:28:13 INFO tool.CodeGenTool: Beginning code generation
16/03/03 01:28:14 INFO manager.SqlManager: Executing SQL statement: SELECT t.* FROM `cdsgus` AS t LIMIT 1
16/03/03 01:28:14 INFO manager.SqlManager: Executing SQL statement: SELECT t.* FROM `cdsgus` AS t LIMIT 1
16/03/03 01:28:14 INFO orm.CompilationManager: HADOOP_MAPRED_HOME is /usr/local/hadoop
Note: /tmp/sqoop-hadoop/compile/7b9cf86a577c124c063ff5dc2242b3fb/cdsgus.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.
16/03/03 01:28:17 INFO orm.CompilationManager: Writing jar file: /tmp/sqoop-hadoop/compile/7b9cf86a577c124c063ff5dc2242b3fb/cdsgus.jar
16/03/03 01:28:17 WARN manager.MySQLManager: It looks like you are importing from mysql.
16/03/03 01:28:17 WARN manager.MySQLManager: This transfer can be faster! Use the --direct
16/03/03 01:28:17 WARN manager.MySQLManager: option to exercise a MySQL-specific fast path.
16/03/03 01:28:17 INFO manager.MySQLManager: Setting zero DATETIME behavior to convertToNull (mysql)
16/03/03 01:28:17 INFO mapreduce.ImportJobBase: Beginning import of cdsgus
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/local/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/local/hbase/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
16/03/03 01:28:17 INFO Configuration.deprecation: mapred.jar is deprecated. Instead, use mapreduce.job.jar
16/03/03 01:28:18 INFO Configuration.deprecation: mapred.map.tasks is deprecated. Instead, use mapreduce.job.maps
16/03/03 01:28:18 INFO client.RMProxy: Connecting to ResourceManager at Master/192.168.1.80:8032
16/03/03 01:28:23 INFO db.DBInputFormat: Using read commited transaction isolation
16/03/03 01:28:23 INFO db.DataDrivenDBInputFormat: BoundingValsQuery: SELECT MIN(`id`), MAX(`id`) FROM `cdsgus`
16/03/03 01:28:23 INFO mapreduce.JobSubmitter: number of splits:2
16/03/03 01:28:23 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1456939431067_0002
16/03/03 01:28:24 INFO impl.YarnClientImpl: Submitted application application_1456939431067_0002
16/03/03 01:28:24 INFO mapreduce.Job: The url to track the job: http://Master:8088/proxy/application_1456939431067_0002/
16/03/03 01:28:24 INFO mapreduce.Job: Running job: job_1456939431067_0002
16/03/03 01:28:38 INFO mapreduce.Job: Job job_1456939431067_0002 running in uber mode : false
16/03/03 01:28:38 INFO mapreduce.Job:  map 0% reduce 0%
16/03/03 01:32:11 INFO mapreduce.Job:  map 50% reduce 0%
16/03/03 01:32:13 INFO mapreduce.Job:  map 100% reduce 0%
16/03/03 01:32:14 INFO mapreduce.Job: Job job_1456939431067_0002 completed successfully
16/03/03 01:32:14 INFO mapreduce.Job: Counters: 31
	File System Counters
		FILE: Number of bytes read=0
		FILE: Number of bytes written=247110
		FILE: Number of read operations=0
		FILE: Number of large read operations=0
		FILE: Number of write operations=0
		HDFS: Number of bytes read=218
		HDFS: Number of bytes written=3130492684
		HDFS: Number of read operations=8
		HDFS: Number of large read operations=0
		HDFS: Number of write operations=4
	Job Counters 
		Killed map tasks=1
		Launched map tasks=3
		Other local map tasks=3
		Total time spent by all maps in occupied slots (ms)=422821
		Total time spent by all reduces in occupied slots (ms)=0
		Total time spent by all map tasks (ms)=422821
		Total vcore-seconds taken by all map tasks=422821
		Total megabyte-seconds taken by all map tasks=432968704
	Map-Reduce Framework
		Map input records=20050144
		Map output records=20050144
		Input split bytes=218
		Spilled Records=0
		Failed Shuffles=0
		Merged Map outputs=0
		GC time elapsed (ms)=19391
		CPU time spent (ms)=206680
		Physical memory (bytes) snapshot=313565184
		Virtual memory (bytes) snapshot=3757293568
		Total committed heap usage (bytes)=65142784
	File Input Format Counters 
		Bytes Read=0
	File Output Format Counters 
		Bytes Written=3130492684
16/03/03 01:32:14 INFO mapreduce.ImportJobBase: Transferred 2.9155 GB in 235.5966 seconds (12.672 MB/sec)
16/03/03 01:32:14 INFO mapreduce.ImportJobBase: Retrieved 20050144 records.

```
![](images/9/chapter09sqoopmapreduce.png)

```
导出mysql表全部数据到hive
hive> create database test_sqoop;
OK
Time taken: 0.81 seconds
hive> show databases;
OK
chu888chu888
default
test_sqoop
Time taken: 0.247 seconds, Fetched: 3 row(s)
hive> 

使用sqoop创建表并导入表
sqoop import --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar --table cdsgus --hive-import  --hive-table test_sqoop.cdsgus

将数据从hive导入mysql
mysql> use hivetestdb;
Database changed
mysql> show tables;
+----------------------+
| Tables_in_test_sqoop |
+----------------------+
| cdsgus               |
+----------------------+
1 row in set (0.00 sec)
mysql> truncate cdsgus;
Query OK, 0 rows affected (0.00 sec)
mysql> select * from cdsgus;
Empty set (0.00 sec)


sqoop  --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar --table cdsgus --export-dir /user/hive/warehouse/test_sqoop.db/cdsgus/ --input-fields-terminated-by '\0001'

增量导入
sqoop import --connect jdbc:mysql://192.168.1.178/hivetestdb  --username chu888chu888 --password skybar --table cdsgus --hive-import  --hive-table
test_sqoop.cdsgus --check-column id --incremental append --last-value 2

HBASE导入
sqoop  import  --connect jdbc:mysql://192.168.1.178/hive_hadoop --username chu888chu888 --password skybar --table TBLS --hbase-table TBLS --hbase-create-table --hbase-row-key TBL_ID --column-family SD_ID
```

####错误阻力
```
hadoop@Master:/$ sqoop import --connect jdbc:mysql://192.168.1.178/hivetestdb --username chu888chu888 --password skybar --table cdsgus
Warning: /usr/local/sqoop1/../hcatalog does not exist! HCatalog jobs will fail.
Please set $HCAT_HOME to the root of your HCatalog installation.
Warning: /usr/local/sqoop1/../accumulo does not exist! Accumulo imports will fail.
Please set $ACCUMULO_HOME to the root of your Accumulo installation.
Warning: /usr/local/sqoop1/../zookeeper does not exist! Accumulo imports will fail.
Please set $ZOOKEEPER_HOME to the root of your Zookeeper installation.
16/03/03 00:32:16 INFO sqoop.Sqoop: Running Sqoop version: 1.4.6
16/03/03 00:32:16 WARN tool.BaseSqoopTool: Setting your password on the command-line is insecure. Consider using -P instead.
16/03/03 00:32:16 INFO manager.MySQLManager: Preparing to use a MySQL streaming resultset.
16/03/03 00:32:16 INFO tool.CodeGenTool: Beginning code generation
16/03/03 00:32:16 INFO manager.SqlManager: Executing SQL statement: SELECT t.* FROM `cdsgus` AS t LIMIT 1
16/03/03 00:32:16 ERROR manager.SqlManager: Error reading from database: java.sql.SQLException: Streaming result set com.mysql.jdbc.RowDataDynamic@654f0d9c is still active. No statements may be issued when any streaming result sets are open and in use on a given connection. Ensure that you have called .close() on any active streaming result sets before attempting more queries.
java.sql.SQLException: Streaming result set com.mysql.jdbc.RowDataDynamic@654f0d9c is still active. No statements may be issued when any streaming result sets are open and in use on a given connection. Ensure that you have called .close() on any active streaming result sets before attempting more queries.
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:914)
	at com.mysql.jdbc.MysqlIO.checkForOutstandingStreamingData(MysqlIO.java:2181)
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1542)
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:1723)
	at com.mysql.jdbc.Connection.execSQL(Connection.java:3277)
	at com.mysql.jdbc.Connection.execSQL(Connection.java:3206)
	at com.mysql.jdbc.Statement.executeQuery(Statement.java:1232)
	at com.mysql.jdbc.Connection.getMaxBytesPerChar(Connection.java:3673)
	at com.mysql.jdbc.Field.getMaxBytesPerCharacter(Field.java:482)
	at com.mysql.jdbc.ResultSetMetaData.getPrecision(ResultSetMetaData.java:443)
	at org.apache.sqoop.manager.SqlManager.getColumnInfoForRawQuery(SqlManager.java:286)
	at org.apache.sqoop.manager.SqlManager.getColumnTypesForRawQuery(SqlManager.java:241)
	at org.apache.sqoop.manager.SqlManager.getColumnTypes(SqlManager.java:227)
	at org.apache.sqoop.manager.ConnManager.getColumnTypes(ConnManager.java:295)
	at org.apache.sqoop.orm.ClassWriter.getColumnTypes(ClassWriter.java:1833)
	at org.apache.sqoop.orm.ClassWriter.generate(ClassWriter.java:1645)
	at org.apache.sqoop.tool.CodeGenTool.generateORM(CodeGenTool.java:107)
	at org.apache.sqoop.tool.ImportTool.importTable(ImportTool.java:478)
	at org.apache.sqoop.tool.ImportTool.run(ImportTool.java:605)
	at org.apache.sqoop.Sqoop.run(Sqoop.java:143)
	at org.apache.hadoop.util.ToolRunner.run(ToolRunner.java:70)
	at org.apache.sqoop.Sqoop.runSqoop(Sqoop.java:179)
	at org.apache.sqoop.Sqoop.runTool(Sqoop.java:218)
	at org.apache.sqoop.Sqoop.runTool(Sqoop.java:227)
	at org.apache.sqoop.Sqoop.main(Sqoop.java:236)
16/03/03 00:32:17 ERROR tool.ImportTool: Encountered IOException running import job: java.io.IOException: No columns to generate for ClassWriter
	at org.apache.sqoop.orm.ClassWriter.generate(ClassWriter.java:1651)
	at org.apache.sqoop.tool.CodeGenTool.generateORM(CodeGenTool.java:107)
	at org.apache.sqoop.tool.ImportTool.importTable(ImportTool.java:478)
	at org.apache.sqoop.tool.ImportTool.run(ImportTool.java:605)
	at org.apache.sqoop.Sqoop.run(Sqoop.java:143)
	at org.apache.hadoop.util.ToolRunner.run(ToolRunner.java:70)
	at org.apache.sqoop.Sqoop.runSqoop(Sqoop.java:179)
	at org.apache.sqoop.Sqoop.runTool(Sqoop.java:218)
	at org.apache.sqoop.Sqoop.runTool(Sqoop.java:227)
	at org.apache.sqoop.Sqoop.main(Sqoop.java:236)

如果出现上面的错误，请更新/usr/lib/sqoop/mysql-java-connector.jar文件。

ISSUE: https://issues.apache.org/jira/browse/SQOOP-1400


这里面还有一种可能就是你在hadoop/common这个目录也有一个mysql的驱动包,这个包也许版本很古老!
hadoop@Master:/usr/local/hadoop/share/hadoop/common$ ls
hadoop-common-2.6.0.jar  hadoop-common-2.6.0-tests.jar  hadoop-nfs-2.6.0.jar  jdiff  lib  mysql-connector-java-5.0.8-bin.jar  sources  templates
hadoop@Master:/usr/local/hadoop/share/hadoop/common$ sudo rm -rf mysql-connector-java-5.0.8-bin.jar 


```