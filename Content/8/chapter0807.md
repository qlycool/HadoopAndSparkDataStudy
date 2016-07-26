# Hive的安装

## 一 安装环境

* Hadoop 2.7.2
* JDK 1.7 U79
* Hive 2.1.0
* Mysql(apt-get 安装)
* 192.168.1.166为Mysql server meta server安装位置
* 192.168.1.159为Hive数据仓库安装位置

## 二 Hive的安装-MySQL作为元数据库
* 安装JDK-略过
* 安装Hadoop-略过
* 安装Mysql-略过

##三 在192.168.1.166上建立Hive meta数据库,用户,赋予权限


**mysql虚拟机的默认密码,在我做试验的时候是123456**

```

$mysql -u root -p
mysql>grant all privileges on *.* to hive@"%" identified by "hive" with grant option;
mysql>flush privileges;
Mysql在Ubuntu中默认安装后,只能在本机访问,如果要开启远程访问,需要做以下两个步骤:
$nano /etc/mysql/my.cnf

找到bind-address=127.0.0.1 ,把这一行注释掉
$service mysql restart

```

##四 在192.168.1.159上安装Hive


** 1 安装Hive **

```
hadoop@hadoopmaster:~$ sudo tar xvfz apache-hive-2.1.0-bin.tar.gz 
hadoop@hadoopmaster:~$ sudo cp -R apache-hive-2.1.0-bin /usr/local/hive 
hadoop@hadoopmaster:~$ sudo chmod -R 775 /usr/local/hive/
hadoop@hadoopmaster:~$ sudo chown -R hadoop:hadoop /usr/local/hive
```


**2 修改/etc/profile加入HIVE_HOME的变量**

```
export HIVE_HOME=/usr/local/hive
export PATH=$PATH:$HIVE_HOME/bin
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/local/hive/lib
$source /etc/profile
```

**3 修改hive/conf下的几个template模板并重命名为其他**

```
cp hive-env.sh.template hive-env.sh
cp hive-default.xml.template hive-site.xml
```

**配置hive-env.sh文件，指定HADOOP_HOME**

```
HADOOP_HOME=/usr/local/hadoop
```

**4 修改hive-site.xml文件，指定MySQL数据库驱动、数据库名、用户名及密码，修改的内容如下所示**

```
<property>
  <name>javax.jdo.option.ConnectionURL</name>
  <value>jdbc:mysql://192.168.1.178:3306/hive?createDatabaseIfNotExist=true</value>
  <description>JDBC connect string for a JDBC metastore</description>
</property>
<property>
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>com.mysql.jdbc.Driver</value>
  <description>Driver class name for a JDBC metastore</description>
</property>
<property>
  <name>javax.jdo.option.ConnectionUserName</name>
  <value>hive</value>
  <description>username to use against metastore database</description>
</property>
<property>
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>hive</value>
  <description>password to use against metastore database</description>
</property>

其中：
javax.jdo.option.ConnectionURL参数指定的是Hive连接数据库的连接字符串；
javax.jdo.option.ConnectionDriverName参数指定的是驱动的类入口名称；
javax.jdo.option.ConnectionUserName参数指定了数据库的用户名；
javax.jdo.option.ConnectionPassword参数指定了数据库的密码。

```

**5 缓存目录的问题,如果不配置也会出错的**

```
 <property> 
 <name>hive.exec.local.scratchdir</name>
 <value>/home/hadoop/iotmp</value>
 <description>Local scratch space for Hive jobs</description>
 </property>
 <property>
 <name>hive.downloaded.resources.dir</name>
 <value>/home/hadoop/iotmp</value>
 <description>Temporary local directory for added resources in the remote file system.</description>
 </property>
```
并且需要对目录进行权限设定 
``` 
mkdir -p /home/hadoop/iotmp 
chmod -R 775 /home/hadoop/iotmp 
```

## 五修改hive/bin下的hive-config.sh文件，设置JAVA_HOME,HADOOP_HOME

```
export JAVA_HOME=/usr/lib/jvm
export HADOOP_HOME=/usr/local/hadoop
export HIVE_HOME=/usr/local/hive

```

## 六 下载mysql-connector-java-5.1.27-bin.jar文件，并放到$HIVE_HOME/lib目录下


**可以从Mysql的官方网站下载,但是记得一定要解压呀,下载的是一个tar.gz文件**

##七 在HDFS中创建/tmp和/user/hive/warehouse并设置权限

```
hadoop fs -mkdir /tmp
hadoop fs -mkdir -p /user/hive/warehouse
hadoop fs -chmod g+w /tmp
hadoop fs -chmod g+w /user/hive/warehouse
```

###1 初始化meta数据库

进入之前需要初始化数据库

```
schematool -initSchema -dbType mysql

hadoop@hadoopmaster:/usr/local/hive/lib$ schematool -initSchema -dbType mysql 
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/local/hive/lib/log4j-slf4j-impl-2.4.1.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/local/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
Metastore connection URL: jdbc:mysql://192.168.1.166:3306/hive?createDatabaseIfNotExist=true
Metastore Connection Driver : com.mysql.jdbc.Driver
Metastore connection User: hive
Starting metastore schema initialization to 2.1.0
Initialization script hive-schema-2.1.0.mysql.sql
Initialization script completed
schemaTool completed
```

###2 测试hive shell

```
hive
show databases;
show tables;
```

###3可以在hadoop中查看hive生产的文件
```
hadoop dfs -ls /user/hive/warehouse
```

##七 Hive shell使用实例

在正式讲解HiveQL之前,先在命令行下运行几样命令是有好处的,可以感受一下HiveQL是如何工作的,也可以自已随便探索一下.


###1 创建数据(文本以tab分隔)

```
~ vi /home/cos/demo/t_hive.txt

16      2       3
61      12      13
41      2       31
17      21      3
71      2       31
1       12      34
11      2       34
```
###2 创建新表

```
hive> CREATE TABLE t_hive (a int, b int, c int) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
OK
Time taken: 0.121 seconds

```

###3 导入数据t_hive.txt到t_hive表

```
hive> LOAD DATA LOCAL INPATH '/tmp/t_hive.txt' OVERWRITE INTO TABLE t_hive ;
Loading data to table default.t_hive
OK
Time taken: 0.609 seconds
```

###4 查看表 
```
hive> show tables;
OK
t_hive
Time taken: 0.099 seconds
```
###5 正则匹配表名
```
hive>show tables '*t*';
OK
t_hive
Time taken: 0.065 seconds
```
###6 查看表数据
```
hive> select * from t_hive;
OK
16      2       3
61      12      13
41      2       31
17      21      3
71      2       31
1       12      34
11      2       34
Time taken: 0.264 seconds
```

###7 查看表结构
```
hive> desc t_hive;
OK
a       int
b       int
c       int
Time taken: 0.1 seconds
```


###8 增加一个字段
```
hive> ALTER TABLE t_hive ADD COLUMNS (new_col String);
OK
Time taken: 0.186 seconds
hive> desc t_hive;
OK
a       int
b       int
c       int
new_col string
Time taken: 0.086 seconds
```

###9 重命令表名
```
~ ALTER TABLE t_hive RENAME TO t_hadoop;
OK
Time taken: 0.45 seconds
hive> show tables;
OK
t_hadoop
Time taken: 0.07 seconds
```
###10 删除表

```
hive> DROP TABLE t_hadoop;
OK
Time taken: 0.767 seconds

hive> show tables;
OK
Time taken: 0.064 seconds
```


## 八 使用beeline

HiveServer2提供了一个新的命令行工具Beeline，它是基于SQLLine CLI的JDBC客户端。
关于SQLLine的的知识，可以参考这个网站：http://sqlline.sourceforge.net/#manual

Beeline工作模式有两种，即本地嵌入模式和远程模式。嵌入模式情况下，它返回一个嵌入式的Hive（类似于Hive CLI）。而远程模式则是通过Thrift协议与某个单独的HiveServer2进程进行连接通信。下面给一个简单的登录Beeline的使用实例：

**1 首先把驱动拷贝到Lib中**

```
sudo cp jdbc/hive-jdbc-2.1.0-standalone.jar /usr/local/hive/lib/
```

**2 启动hiveserver2的服务**

```
命令行模式:

hive --service hiveserver2 --hiveconf hive.server2.thrift.port=10001

服务模式：

hiveserver2 start
```

**3 执行操作**

```
% bin/beeline
Hive version 0.11.0-SNAPSHOT by Apache
beeline> !connect jdbc:hive2://localhost:10000/default 
!connect jdbc:hive2://localhost:10000/default 
Connecting to jdbc:hive2://localhost:10000/default
Connected to: Hive (version 0.10.0)
Driver: Hive (version 0.10.0-SNAPSHOT)
Transaction isolation: TRANSACTION_REPEATABLE_READ
0: jdbc:hive2://localhost:10000> show tables;
show tables;
+-------------------+
|     tab_name      |
+-------------------+
| primitives        |
| src               |
| src1              |
| src_json          |
| src_sequencefile  |
| src_thrift        |
| srcbucket         |
| srcbucket2        |
| srcpart           |
+-------------------+
9 rows selected (1.079 seconds)
```


##九 FAQ


### 出错信息1

```
hadoop@hadoopmaster:/usr/local/hive/conf$ hive 
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/local/hive/lib/log4j-slf4j-impl-2.4.1.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/local/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

Logging initialized using configuration in jar:file:/usr/local/hive/lib/hive-common-2.1.0.jar!/hive-log4j2.properties Async: true
Exception in thread "main" java.lang.RuntimeException: org.apache.hadoop.hive.ql.metadata.HiveException: org.apache.hadoop.hive.ql.metadata.HiveException: MetaException(message:Hive metastore database is not initialized. Please use schematool (e.g. ./schematool -initSchema -dbType ...) to create the schema. If needed, don't forget to include the option to auto-create the underlying database in your JDBC connection string (e.g. ?createDatabaseIfNotExist=true for mysql))
 at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:578)
 at org.apache.hadoop.hive.ql.session.SessionState.beginStart(SessionState.java:518)
 at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:705)
 at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:641)
 at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
 at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 at java.lang.reflect.Method.invoke(Method.java:606)
 at org.apache.hadoop.util.RunJar.run(RunJar.java:221)
 at org.apache.hadoop.util.RunJar.main(RunJar.java:136)
Caused by: org.apache.hadoop.hive.ql.metadata.HiveException: org.apache.hadoop.hive.ql.metadata.HiveException: MetaException(message:Hive metastore database is not initialized. Please use schematool (e.g. ./schematool -initSchema -dbType ...) to create the schema. If needed, don't forget to include the option to auto-create the underlying database in your JDBC connection string (e.g. ?createDatabaseIfNotExist=true for mysql))
 at org.apache.hadoop.hive.ql.metadata.Hive.registerAllFunctionsOnce(Hive.java:226)
 at org.apache.hadoop.hive.ql.metadata.Hive.<init>(Hive.java:366)
 at org.apache.hadoop.hive.ql.metadata.Hive.create(Hive.java:310)
 at org.apache.hadoop.hive.ql.metadata.Hive.getInternal(Hive.java:290)
 at org.apache.hadoop.hive.ql.metadata.Hive.get(Hive.java:266)
 at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:545)
 ... 9 more
Caused by: org.apache.hadoop.hive.ql.metadata.HiveException: MetaException(message:Hive metastore database is not initialized. Please use schematool (e.g. ./schematool -initSchema -dbType ...) to create the schema. If needed, don't forget to include the option to auto-create the underlying database in your JDBC connection string (e.g. ?createDatabaseIfNotExist=true for mysql))
 at org.apache.hadoop.hive.ql.metadata.Hive.getAllFunctions(Hive.java:3593)
 at org.apache.hadoop.hive.ql.metadata.Hive.reloadFunctions(Hive.java:236)
 at org.apache.hadoop.hive.ql.metadata.Hive.registerAllFunctionsOnce(Hive.java:221)
 ... 14 more
Caused by: MetaException(message:Hive metastore database is not initialized. Please use schematool (e.g. ./schematool -initSchema -dbType ...) to create the schema. If needed, don't forget to include the option to auto-create the underlying database in your JDBC connection string (e.g. ?createDatabaseIfNotExist=true for mysql))
 at org.apache.hadoop.hive.ql.metadata.Hive.getMSC(Hive.java:3364)
 at org.apache.hadoop.hive.ql.metadata.Hive.getMSC(Hive.java:3336)
 at org.apache.hadoop.hive.ql.metadata.Hive.getAllFunctions(Hive.java:3590)
 ... 16 more
```
**没有执行**

```
schematool -initSchema -dbType mysql
```
执行之后搞定

### 出错信息2 


```
 hadoop@hadoopmaster:/usr/local/hive/lib$ hive 
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/local/hive/lib/log4j-slf4j-impl-2.4.1.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/local/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]

Logging initialized using configuration in jar:file:/usr/local/hive/lib/hive-common-2.1.0.jar!/hive-log4j2.properties Async: true
Exception in thread "main" java.lang.IllegalArgumentException: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
 at org.apache.hadoop.fs.Path.initialize(Path.java:205)
 at org.apache.hadoop.fs.Path.<init>(Path.java:171)
 at org.apache.hadoop.hive.ql.session.SessionState.createSessionDirs(SessionState.java:631)
 at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:550)
 at org.apache.hadoop.hive.ql.session.SessionState.beginStart(SessionState.java:518)
 at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:705)
 at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:641)
 at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
 at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 at java.lang.reflect.Method.invoke(Method.java:606)
 at org.apache.hadoop.util.RunJar.run(RunJar.java:221)
 at org.apache.hadoop.util.RunJar.main(RunJar.java:136)
Caused by: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
 at java.net.URI.checkPath(URI.java:1804)
 at java.net.URI.<init>(URI.java:752)
 at org.apache.hadoop.fs.Path.initialize(Path.java:202)
 ... 12 more
```


**hive-site.xml中没有配置合理临时目录的问题**

```
 <property> 
 <name>hive.exec.local.scratchdir</name>
 <value>/home/hadoop/iotmp</value>
 <description>Local scratch space for Hive jobs</description>
 </property>
 <property>
 <name>hive.downloaded.resources.dir</name>
 <value>/home/hadoop/iotmp</value>
 <description>Temporary local directory for added resources in the remote file system.</description>
 </property>
 <property>
```
并且需要对目录进行权限设定

```
mkdir -p /home/hadoop/iotmp
chmod -R 775 /home/hadoop/iotmp
```


##参考文档

+ [出错信息解决](http://blog.csdn.net/zwx19921215/article/details/42776589)
+ [配置参考](http://blog.csdn.net/lnho2015/article/details/51307438)
+ [配置参考](http://blog.csdn.net/lnho2015/article/details/51355511)
+ [出错信息解决](http://blog.csdn.net/zwx19921215/article/details/42776589)

