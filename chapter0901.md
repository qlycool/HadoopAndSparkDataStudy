# 1.Sqoop2的安装------------此实验一直是失败的,正在测试中

####解压并安装
```
hadoop@Master:~$ sudo tar xvfz sqoop-1.99.6-bin-hadoop200.tar.gz 
hadoop@Master:~$ sudo mv sqoop-1.99.6-bin-hadoop200 /usr/local/sqoop
hadoop@Master:~$ sudo chmod -R 775 /usr/local/sqoop
hadoop@Master:~$ sudo chown -R hadoop:hadoop /usr/local/sqoop

```

####修改环境变量
```
hadoop@Master:~$ sudo nano /etc/profile

#sqoop
export SQOOP_HOME=/usr/local/sqoop
export PATH=$SQOOP_HOME/bin:$PATH
export CATALINA_HOME=$SQOOP_HOME/server
export LOGDIR=$SQOOP_HOME/logs

hadoop@Master:~$ source /etc/profile

```

####修改sqoop的环境变量
```
hadoop@Master:/$ sudo nano /usr/local/sqoop/server/conf/sqoop.properties 

#修改指向我的hadoop安装目录  
org.apache.sqoop.submission.engine.mapreduce.configuration.directory=/usr/local/hadoop/etc/hadoop  

hadoop@Master:/$ sudo nano /usr/local/sqoop/server/conf/catalina.properties 

common.loader=/usr/local/hadoop/share/hadoop/common/*.jar,/usr/local/hadoop/share/hadoop/common/lib/*.jar,/usr/local/hadoop/share/hadoop/hdfs/*.jar,/usr/local/hadoop/share/hadoop/hdfs/lib/*.jar,/usr/local/hadoop/share/hadoop/mapreduce/*.jar,/usr/local/hadoop/share/hadoop/mapreduce/lib/*.jar,/usr/local/hadoop/share/hadoop/tools/*.jar,/usr/local/hadoop/share/hadoop/tools/lib/*.jar,/usr/local/hadoop/share/hadoop/yarn/*.jar,/usr/local/hadoop/share/hadoop/yarn/lib/*.jar,/usr/local/hadoop/share/hadoop/httpfs/tomcat/lib/*.jar  

下载mysql驱动包  
mysql-connector-java-5.1.16-bin.jar  
把其中的jar包也丢到到/usr/local/hadoop/share/hadoop/common/下面
$ sudo cp mysql-connector-java-5.0.8-bin.jar /usr/local/hadoop/share/hadoop/common/

```

####启动sqoop
```
hadoop@Master:~/mysql-connector-java-5.0.8$ sqoop.sh server start
Sqoop home directory: /usr/local/sqoop
Setting SQOOP_HTTP_PORT:     12000
Setting SQOOP_ADMIN_PORT:     12001
Using   CATALINA_OPTS:       
Adding to CATALINA_OPTS:    -Dsqoop.http.port=12000 -Dsqoop.admin.port=12001
Using CATALINA_BASE:   /usr/local/sqoop/server
Using CATALINA_HOME:   /usr/local/sqoop/server
Using CATALINA_TMPDIR: /usr/local/sqoop/server/temp
Using JRE_HOME:        /usr/lib/jvm//jre
Using CLASSPATH:       /usr/local/sqoop/server/bin/bootstrap.jar


```
