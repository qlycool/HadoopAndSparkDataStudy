# 5 FAQ

##FAQ

###调试中出现的Jline版本过低的FAQ
```
Logging initialized using configuration in jar:file:/hive/apache-hive-1.1.0-bin/lib/hive-common-1.1.0.jar!/hive-log4j.properties
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/hadoop-2.5.2/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/hive/apache-hive-1.1.0-bin/lib/hive-jdbc-1.1.0-standalone.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
[ERROR] Terminal initialization failed; falling back to unsupported
java.lang.IncompatibleClassChangeError: Found class jline.Terminal, but interface was expected
        at jline.TerminalFactory.create(TerminalFactory.java:101)
        at jline.TerminalFactory.get(TerminalFactory.java:158)
        at jline.console.ConsoleReader.<init>(ConsoleReader.java:229)
        at jline.console.ConsoleReader.<init>(ConsoleReader.java:221)
        at jline.console.ConsoleReader.<init>(ConsoleReader.java:209)
        at org.apache.hadoop.hive.cli.CliDriver.getConsoleReader(CliDriver.java:773)
        at org.apache.hadoop.hive.cli.CliDriver.executeDriver(CliDriver.java:715)
        at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:675)
        at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:615)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:606)
        at org.apache.hadoop.util.RunJar.main(RunJar.java:212)
 
原因是hadoop目录下存在老版本jline：
/hadoop-2.5.2/share/hadoop/yarn/lib：
-rw-r--r-- 1 root root   87325 Mar 10 18:10 jline-0.9.94.jar
 
解决方法是：
将hive下的新版本jline的JAR包拷贝到hadoop下：
cp /hive/apache-hive-1.1.0-bin/lib/jline-2.12.jar ./
 
/hadoop-2.5.2/share/hadoop/yarn/lib：
-rw-r--r-- 1 root root   87325 Mar 10 18:10 jline-0.9.94.jar.bak
-rw-r--r-- 1 root root  213854 Mar 11 22:22 jline-2.12.jar
 
hive cli启动成功：
root@ubuntu:/hive# hive

Logging initialized using configuration in jar:file:/hive/apache-hive-1.1.0-bin/lib/hive-common-1.1.0.jar!/hive-log4j.properties
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/hadoop-2.5.2/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/hive/apache-hive-1.1.0-bin/lib/hive-jdbc-1.1.0-standalone.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
hive> 
```

###调试中出现java.io.tmpdir目录的问题
```
异常详情如下：
Exception in thread "main" java.lang.RuntimeException: java.lang.IllegalArgumentException: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
        at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:444)
        at org.apache.hadoop.hive.cli.CliDriver.run(CliDriver.java:672)
        at org.apache.hadoop.hive.cli.CliDriver.main(CliDriver.java:616)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:606)
        at org.apache.hadoop.util.RunJar.main(RunJar.java:160)
Caused by: java.lang.IllegalArgumentException: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
        at org.apache.hadoop.fs.Path.initialize(Path.java:148)
        at org.apache.hadoop.fs.Path.<init>(Path.java:126)
        at org.apache.hadoop.hive.ql.session.SessionState.createSessionDirs(SessionState.java:487)
        at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:430)
        ... 7 more
Caused by: java.net.URISyntaxException: Relative path in absolute URI: ${system:java.io.tmpdir%7D/$%7Bsystem:user.name%7D
        at java.net.URI.checkPath(URI.java:1804)
        at java.net.URI.<init>(URI.java:752)
        at org.apache.hadoop.fs.Path.initialize(Path.java:145)
        ... 10 more


解决方案如下：
1.查看hive-site.xml配置，会看到配置值含有"system:java.io.tmpdir"的配置项
2.新建文件夹/home/grid/hive-0.14.0-bin/iotmp
3.将含有"system:java.io.tmpdir"的配置项的值修改为如上地址
启动hive，成功！
```

### hive内存不够用的问题

```
hive> select * from t_test where ds=20150323 limit 2;
OK
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 
问题原因： hive堆内存默认为256M
 
这个问题的解决方法为：
修改/usr/lib/hive/bin/hive-config.sh文件 中
# Default to use 256MB 
export HADOOP_HEAPSIZE=${HADOOP_HEAPSIZE:-256}
将上面256调大就行
```

### 用户不对的错误
编写JDBC客户端程序连接hive时，出现报错：

```
org.apache.hive.service.cli.HiveSQLException: Failed to open new session: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: hadoop is not allowed to impersonate anonymous
```

具体出错信息

```
Exception in thread "main" org.apache.hive.service.cli.HiveSQLException: Failed to open new session: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: hadoop is not allowed to impersonate anonymous
    at org.apache.hive.jdbc.Utils.verifySuccess(Utils.java:258)
    at org.apache.hive.jdbc.Utils.verifySuccess(Utils.java:249)
    at org.apache.hive.jdbc.HiveConnection.openSession(HiveConnection.java:579)
    at org.apache.hive.jdbc.HiveConnection.<init>(HiveConnection.java:167)
    at org.apache.hive.jdbc.HiveDriver.connect(HiveDriver.java:107)
    at java.sql.DriverManager.getConnection(DriverManager.java:571)
    at java.sql.DriverManager.getConnection(DriverManager.java:215)
    at client.main(client.java:21)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:606)
    at com.intellij.rt.execution.application.AppMain.main(AppMain.java:140)
Caused by: org.apache.hive.service.cli.HiveSQLException: Failed to open new session: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: hadoop is not allowed to impersonate anonymous
    at org.apache.hive.service.cli.session.SessionManager.openSession(SessionManager.java:324)
    at org.apache.hive.service.cli.CLIService.openSessionWithImpersonation(CLIService.java:187)
    at org.apache.hive.service.cli.thrift.ThriftCLIService.getSessionHandle(ThriftCLIService.java:424)
    at org.apache.hive.service.cli.thrift.ThriftCLIService.OpenSession(ThriftCLIService.java:318)
    at org.apache.hive.service.cli.thrift.TCLIService$Processor$OpenSession.getResult(TCLIService.java:1257)
    at org.apache.hive.service.cli.thrift.TCLIService$Processor$OpenSession.getResult(TCLIService.java:1242)
    at org.apache.thrift.ProcessFunction.process(ProcessFunction.java:39)
    at org.apache.thrift.TBaseProcessor.process(TBaseProcessor.java:39)
    at org.apache.hive.service.auth.TSetIpAddressProcessor.process(TSetIpAddressProcessor.java:56)
    at org.apache.thrift.server.TThreadPoolServer$WorkerProcess.run(TThreadPoolServer.java:286)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.RuntimeException: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: hadoop is not allowed to impersonate anonymous
    at org.apache.hive.service.cli.session.HiveSessionProxy.invoke(HiveSessionProxy.java:89)
    at org.apache.hive.service.cli.session.HiveSessionProxy.access$000(HiveSessionProxy.java:36)
    at org.apache.hive.service.cli.session.HiveSessionProxy$1.run(HiveSessionProxy.java:63)
    at java.security.AccessController.doPrivileged(Native Method)
    at javax.security.auth.Subject.doAs(Subject.java:422)
    at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1657)
    at org.apache.hive.service.cli.session.HiveSessionProxy.invoke(HiveSessionProxy.java:59)
    at com.sun.proxy.$Proxy35.open(Unknown Source)
    at org.apache.hive.service.cli.session.SessionManager.openSession(SessionManager.java:315)
    ... 12 more
Caused by: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: hadoop is not allowed to impersonate anonymous
    at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:554)
    at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:489)
    at org.apache.hive.service.cli.session.HiveSessionImpl.open(HiveSessionImpl.java:156)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:497)
    at org.apache.hive.service.cli.session.HiveSessionProxy.invoke(HiveSessionProxy.java:78)
    ... 20 more
Caused by: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException:User: hadoop is not allowed to impersonate anonymous
    at org.apache.hadoop.ipc.Client.call(Client.java:1476)
    at org.apache.hadoop.ipc.Client.call(Client.java:1407)
    at org.apache.hadoop.ipc.ProtobufRpcEngine$Invoker.invoke(ProtobufRpcEngine.java:229)
    at com.sun.proxy.$Proxy30.getFileInfo(Unknown Source)
    at org.apache.hadoop.hdfs.protocolPB.ClientNamenodeProtocolTranslatorPB.getFileInfo(ClientNamenodeProtocolTranslatorPB.java:771)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:497)
    at org.apache.hadoop.io.retry.RetryInvocationHandler.invokeMethod(RetryInvocationHandler.java:187)
    at org.apache.hadoop.io.retry.RetryInvocationHandler.invoke(RetryInvocationHandler.java:102)
    at com.sun.proxy.$Proxy31.getFileInfo(Unknown Source)
    at org.apache.hadoop.hdfs.DFSClient.getFileInfo(DFSClient.java:2116)
    at org.apache.hadoop.hdfs.DistributedFileSystem$22.doCall(DistributedFileSystem.java:1305)
    at org.apache.hadoop.hdfs.DistributedFileSystem$22.doCall(DistributedFileSystem.java:1301)
    at org.apache.hadoop.fs.FileSystemLinkResolver.resolve(FileSystemLinkResolver.java:81)
    at org.apache.hadoop.hdfs.DistributedFileSystem.getFileStatus(DistributedFileSystem.java:1301)
    at org.apache.hadoop.fs.FileSystem.exists(FileSystem.java:1424)
    at org.apache.hadoop.hive.ql.session.SessionState.createRootHDFSDir(SessionState.java:639)
    at org.apache.hadoop.hive.ql.session.SessionState.createSessionDirs(SessionState.java:597)
    at org.apache.hadoop.hive.ql.session.SessionState.start(SessionState.java:526)
    ... 27 more
    
```

从最终的错误信息来看：User: hadoop is not allowed to impersonate anonymous，意思是用户hadoop不允许伪装成anonymous（hive的默认用户，默认配置可以查看）。

解决方案

```
<property>
      <name>hadoop.proxyuser.hadoop.groups</name>
      <value>hadoop</value>
      <description>Allow the superuser oozie to impersonate any members of the group group1 and group2</description>
 </property>
 
 <property>
      <name>hadoop.proxyuser.hadoop.hosts</name>
      <value>192.168.21.222,127.0.0.1,localhost</value>
      <description>The superuser can connect only from host1 and host2 to impersonate a user</description>
  </property>
```



