# 1.Hive的安装

##Hive的安装-MySQL作为元数据库
* 安装JDK-略过
* 安装Hadoop-略过
* 安装Mysql-略过

###1建立Hive数据库,用户,赋予权限
```
#mysql虚拟机的默认密码,在我做试验的时候是123456
#mysql -u root -p
mysql>grant all privileges on *.* to hive@"%" identified by "hive" with grant option;
mysql>flush privileges;
Mysql在Ubuntu中默认安装后,只能在本机访问,如果要开启远程访问,需要做以下两个步骤:
#nano /etc/mysql/my.cnf
找到bind-address=127.0.0.1 ,把这一行注释掉

```

###2安装Hive
```
hadoop@Master:~$ sudo tar xvfz apache-hive-1.1.1-bin.tar.gz 
hadoop@Master:~$ sudo cp -R apache-hive-1.1.1-bin /usr/local/hive
hadoop@Master:~$ sudo chmod -R 775 /usr/local/hive/
hadoop@Master:~$ sudo chown hadoop:hadoop /usr/local/hive/
#修改/etc/profile加入HIVE_HOME的变量
export HIVE_HOME=/usr/local/hive
export PATH=$PATH:$HIVE_HOME/bin
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:/usr/local/hive/lib

#修改hive/conf下的几个template模板并重命名为其他
cp hive-env.sh.template hive-env.sh
cp hive-default.xml.template hive-site.xml

#配置hive-env.sh文件，指定HADOOP_HOME
HADOOP_HOME=/usr/local/hadoop

#修改hive-site.xml文件，指定MySQL数据库驱动、数据库名、用户名及密码，修改的内容如下所示
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
###3修改hive/bin下的hive-config.sh文件，设置JAVA_HOME,HADOOP_HOME
```
export JAVA_HOME=/usr/lib/jvm
export HADOOP_HOME=/usr/local/hadoop
export HIVE_HOME=/usr/local/hive

```

###4下载mysql-connector-java-5.1.27-bin.jar文件，并放到$HIVE_HOME/lib目录下
```
可以从Mysql的官方网站下载,但是记得一定要解压呀,下载的是一个tar.gz文件
```
###5在HDFS中创建/tmp和/user/hive/warehouse并设置权限
```
hadoop fs -mkdir /tmp
hadoop fs -mkdir /user/hive/warehouse
hadoop fs -chmod g+w /tmp
hadoop fs -chmod g+w /user/hive/warehouse
```
###6启动hadoop。进入hive shell，输入一些命令查看
```
hive
show databases;
show tables;
```
###7可以在hadoop中查看hive生产的文件
```
hadoop dfs -ls /user/hive/warehouse
```
##Hive使用实例

在正式讲解HiveQL之前,先在命令行下运行几样命令是有好处的,可以感受一下HiveQL是如何工作的,也可以自已随便探索一下.

###1查询示例

```
hive> SHOW TABLES;
OK
testuser
Time taken: 0.707 seconds, Fetched: 1 row(s)

hive> DESC testuser;
OK
id                  	int                 	                    
username            	string              	                    
Time taken: 0.38 seconds, Fetched: 2 row(s)
hive> SELECT * from testuser limit 10;
OK
1	sssss
1	sssss
Time taken: 0.865 seconds, Fetched: 2 row(s)
hive> 

hive> select count(1) from testuser;
Query ID = hadoop_20160205004747_9d84aaca-887a-43a0-bad9-eddefe4e2219
Total jobs = 1
Launching Job 1 out of 1
Number of reduce tasks determined at compile time: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapreduce.job.reduces=<number>
Starting Job = job_1454604205731_0001, Tracking URL = http://Master:8088/proxy/application_1454604205731_0001/
Kill Command = /usr/local/hadoop/bin/hadoop job  -kill job_1454604205731_0001
Hadoop job information for Stage-1: number of mappers: 1; number of reducers: 1
2016-02-05 00:48:11,942 Stage-1 map = 0%,  reduce = 0%
2016-02-05 00:48:19,561 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 1.38 sec
2016-02-05 00:48:28,208 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 2.77 sec
MapReduce Total cumulative CPU time: 2 seconds 770 msec
Ended Job = job_1454604205731_0001
MapReduce Jobs Launched: 
Stage-Stage-1: Map: 1  Reduce: 1   Cumulative CPU: 2.77 sec   HDFS Read: 6532 HDFS Write: 2 SUCCESS
Total MapReduce CPU Time Spent: 2 seconds 770 msec
OK
2
Time taken: 35.423 seconds, Fetched: 1 row(s)


```

**通过这些消息,可以知道该查询生成了一个Mapreduce作业,Hive之美在于用户根本不需要知道MapReduce的存在,用户所需关心的,仅仅是使用一种类似于SQL的语言.**

```
多次重复实现大量数据插入

hive> insert overwrite table testuser
    > select id,count(id)
    > from testuser
    > group by id;

```