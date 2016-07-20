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