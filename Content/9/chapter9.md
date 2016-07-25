#Sqoop
Apache Sqoop（SQL-to-Hadoop） 项目旨在协助 RDBMS 与 Hadoop 之间进行高效的大数据交流。用户可以在 Sqoop 的帮助下，轻松地把关系型数据库的数据导入到 Hadoop 与其相关的系统 (如HBase和Hive)中；同时也可以把数据从 Hadoop 系统里抽取并导出到关系型数据库里。除了这些主要的功能外，Sqoop 也提供了一些诸如查看数据库表等实用的小工具。

理论上，Sqoop 支持任何一款支持 JDBC 规范的数据库，如 DB2、MySQL 等。Sqoop 还能够将 DB2 数据库的数据导入到 HDFS 上，并保存为多种文件类型。常见的有定界文本类型，Avro 二进制类型以及 SequenceFiles 类型。在本文里，统一用定界文本类型。

![](images/9/chapter09mapreduce.png)

Sqoop中一大亮点就是可以通过hadoop的mapreduce把数据从关系型数据库中导入数据到HDFS。Sqoop架构非常简单，其整合了Hive、Hbase和Oozie，通过map-reduce任务来传输数据，从而提供并发特性和容错。

##Sqoop1和Sqoop 2架构的变迁
首先这两个版本是完全不兼容的，其具体的版本号区别为

```1.4.x为sqoop 1，1.99x为sqoop ```

sqoop1和sqoop2在架构和用法上已经完全不同。在架构上，sqoop1仅仅使用一个sqoop客户端，sqoop2引入了sqoopserver，对connector实现了集中的管理。

其访问方式也变得多样化了，其可以通过REST API、JAVA API、WEB UI以及CLI控制台方式进行访问。另外，其在安全性能方面也有一定的改善，在sqoop1中我们经常用脚本的方式将HDFS中的数据导入到mysql中，或者反过来将mysql数据导入到HDFS中，其中在脚本里边都要显示指定mysql数据库的用户名和密码的，安全性做的不是太完善。在sqoop2中，如果是通过CLI方式访问的话，会有一个交互过程界面，你输入的密码信息不被看到，同时Sqoop2引入基于角色的安全机制。

下图是sqoop1和sqoop2简单架构对比：

**Sqoop1架构图：**

![](images/9/chapter09sqoop1.png)

**Sqoop2架构图：**

![](images/9/chapter09sqoop2.png)

两个不同的版本，完全不兼容 版本号划分区别.

Apache版本：
1.4.x(Sqoop1); 1.99.x(Sqoop2)     

CDH版本 : Sqoop-1.4.3-cdh4(Sqoop1) ; Sqoop2-1.99.2-cdh4.5.0 



* sqoop1优点：架构部署简单
* sqoop1缺点：命令行方式容易出错，格式紧耦合，无法支持所有数据类型，安全机制不够完善，例如密码暴漏， 安装需要root权限，connector必须符合JDBC模型
* sqoop2优点：多种交互方式，命令行，web UI，rest API，conncetor集中化管理，所有的链接安装在sqoop server上，完善权限管理机制，connector规范化，仅仅负责数据的读写
* sqoop2缺点：架构稍复杂，配置部署更繁琐

