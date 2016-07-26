# 6.HBASE安装疑难杂症

##错误表现-SLF4J: Class path contains multiple SLF4J bindings.

```
错误表现：
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/usr/hbase/lib/slf4j-log4j12-1.6.4.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/usr/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.

含义为：
发生jar包冲突了：
分别为：
file:/usr/hbase/lib/slf4j-log4j12-1.6.4.jar!/org/slf4j/impl/StaticLoggerBinder.class
file:/usr/hadoop/share/hadoop/common/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class
移除其中一个jar包即可
解决方案：
使用下面命令：
/usr/hbase/lib rm slf4j-log4j12-1.6.4.jar
问题解决
```