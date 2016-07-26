#Hadoop Training Sheet
##

###1. 操作系统

- Linux操作系统（检查操作系统版本号）

- Linux操作系统概述

- 安装Linux操作系统
	> CentOS、Ubuntu

- Linux Shell相关工具的使用
	> Xshell、Xftp

- Linux图形界面、字符界面

- Linux基本命令
	- 查看主机名：hostname
	 
	- 硬件配置：df、free、ethtool
	 
	- 文件/文件夹操作：cd、mkdir、rm、cp、mv、touch、du、lsof
	
	- 查看文本：cat、less、tail、vim、vi
	
	- 查找类：grep、find
	
	- 压缩解压缩：tar、gzip、zip、unzip

	- 软件安装类：rpm、yum、apt-get
	
	- 帮助类：man
	
	- 时间类：date
	
	- IO类：lostat
	
	- 权限类：sudo、chown、chmod、chgrp
	
	- 端口连接类：netstat、ping、telnet
	
	- 启停服务类：etc/init.d/mysqld [start|stop|restart]
	
	- 网页类：elinks http://192.168.1.210:60010
	
	- 挂载类：mount、umount

- 用户、组群和权限管理
	
- 文件系统管理

- 软件包管理与系统备份

- Linux网络配置

- Linux基本服务配置
	> DNS服务、DHCP服务、HTTP服务、FTP服务、SMTP服务、POP3服务
- Linux Shell命令
	- 文件及文本常用命令：tar、find、cut、wc、split、grep、head、tail、sed、awk

	- 系统运行状况命令：top、watch、free、mpstat、vmstat、lostat、pidstat、df、du

	- 系统运行进程命令：ps、nice、renice、lsof、pgrep、pkill

	- 追踪命令：strace

	- 排序命令：sort

	- 删除重复行：uniq

	- 正则表达式

- Linux Shell脚本编写

	- 定时备份系统日志

	- 自动监控其它主机硬盘及内存使用状况	

	- 自动化安装JDK、Tomcat

###2. 数据库

- 关系型数据库原理

- 在Linux上安装Mysql、SQL-Server、DB2、Oracle数据库

- DDL、DML、DCL语法

	> Mysql、SQL-Server、Oracle、DB2

- SQL基础
	- 基本语句：insert、select、where、update、delete、join、group by、having、desc、asc、limit、isnull、等

	- 函数：日期函数、嵌套函数、字符串函数、数字函数、聚集函数

- SQL高级
	- PL/SQL、if、case、loop、while、for、游标、视图、索引、存储过程、事务、SQL编程

- 数据库管理
	- 容量规划

	- 安全

	- 性能

	- 对象

	- 存储管理

	- 变化管理

	- 任务调度

	- 网络管理

	- 故障排查

	- 管理工具
		- Mysql：Workbench、Navicat
		- SQL-Server：SSMSE
		- Oracle：OEM、PL/SQL developer、Toad
		- DB2：DB2top、Toad for db2

- 备份与恢复
	- 文件：参数文件、控制文件、数据文件、转储文件、重做日志

	- 备份：冷备份、热备份

	- 还原和恢复：备份控制文件、归档日志文件

- 数据库优化
	- 表：建立分区表、重建索引

	- I/O：将数据、日志、索引放在不同I/O设备

	- 切分：横/纵向分割表

	- 硬件：升级CPU、内存、网络带宽等

	- 业务分离：OLTP与OLAP分离

	- 字段选取：where后的查询字段避免冗余


###3. 大数据


#####一、原生Hadoop
##

- Hadoop框架
	- 大数据概念及应用场景

	- Hadoop介绍

	- Hadoop组件

- HDFS组件
	- HDFS读取过程

	- HDFS基本命令：cat、chgrp、chmod、chown、cp、df、du、find、get、ls、lsr、mkdir、mv、put、rm、rmdir、rmr、tail、stat等

- Hive组件

	- Hive表结构与数据存储

	- Hive与RDBMS区别

	- Hive数据库与表

	- 基本HiveQL语法

	- 向Hive装载数据

- Sqoop组件
	
	- Sqoop工作原理

	- Sqoop数据流转

- Flume组件
	- Flume工作原理

	- Flume参数配置

	- 实时将系统日志文件导入HDFS

- HBase组件
	
	- HBase概念及应用场景

	- HBase与RDBMS联系与区别

	- HBase表结构与数据存储

#####二、TDH发行版本
##
**安装前准备**

- 操作系统版本  CentOS 6.3-6.5/REHL 6.3-6.5/ Suse SP2-SP3/操作系统是否干净？
	
- 是否需要配置sudo用户安装TDH？

- 机器硬件配置  CPU/MEM是否满足要求？/ 系统根分区大于300G?/千兆以上网络？

- 是否配置了SSD？
	
- 是否操作系统双盘RAID1，数据盘RAID0？ 

- 配置是否对称同构

	（1）磁盘同构： 数据盘对应的每块磁盘是否一样大？（严禁大小磁盘混合做数据盘，例如300G /mnt/	disk1,  2.7T /mnt/disk2）

	（2）网络同构： 每台机器网卡配置是否相同？

	（3）CPU/内存大小是否同构：
	
- 系统时间是否正确。 > date -s ‘2015-11-11 09:45:00’

- 确认网络解析是用/etc/hosts文件还是DNS server。

	（1）推荐使用hosts文件解析。

	（2）若用hosts文件解析，确保/etc/resolv.conf 为空或隐掉。并保证/etc/nsswitch.conf 中	files 解析在DNS解析之前

	（3）各节点尽可能的在一个网段
	
- hostname只能是以字母和数字的组合(中间允许’-’)，不能有“,” / ”.” / “_”等特殊字符。


**TDH安装与运维**

- 安装

	- root安装、非root安装
	
	- 配置RACK（机柜命名一定要以’/’ 开头，如  /default）
	
	- 添加节点、添加硬盘、升级Licence

- 配置检查

	- Zookeeper的重要配置
		Zookeeper 配置个数是否检查？（奇数个，10个节点以下3个，10-50个节点 5个）

	- HDFS的重要配置
		HDFS 的1 个目录配置是否只包含 /mnt/disk*的数据盘，SSD是否排除在外？

	- YARN的重要配置
	
		（1）YARN 的2个目录配置是否只包含 /mnt/disk*的数据盘，SSD是否排除在外？
	
		（2）YARN 的 vcore/Mem配置是否配置成了1个core对应2G内存？

	- Inceptor的重要配置
		（1）Inceptor 是否配置了HiveServer2 （推荐 Kerberos+LDAP HA模式）

		（2）Inceptor 的 fastdisk 是否配置了SSD？ 

		（3）Inceptor 的localdir 配置里是否只包含 /mnt/disk* ，SSD是否排除在外？

		（4）Inceptor 的资源配置是否合理？ 每个core是否都分配了1.5-2G内存？

	- Hyperbase的重要配置
	- Hmaster个数是否为奇数？（3个或者5个）
	
	- Fair Schedule配置

- 日志相关

	- Zookeeper的日志位置（/var/log/zookeeper1）
	
	- HDFS的日志位置（/var/log/hdfs1）
	
	- YARN的日志位置（/var/log/yarn1）

	- Hyperbase的日志位置（/var/log/hyperbase1）

	- Inceptor的日志位置（/var/log/inceptor1）

- 服务启停

	- 查看机器已启动的服务
	
	- 各服务启停的顺序
	
	- Zookeeper的启停
	
	- HDFS的启停
	
	- Hyperbase的启停
	
	- YARN的启停

	- Inceptor的启停

- 管理页面

	- HDFS/YARN/Hyperbase/Inceptor重要的管理界面
	
	- HDFS健康状态的检查

	- YARN状态的检查

	- Hyperbase状态的检查

	- Inceptor运行状态的检查

- 安全相关

	- 开启Kerberos

	- 添加/删除用户

- HDFS状态检查
 
	- 查看HDFS状态
	
	- 查看损坏文件
	
	- fsimage和editlog存放的位置

- Inceptor操作
 
	- Hive、Inceptor默认的分隔符

	- 创建外表以及数据存放位置

	- 创建ORC格式表及数据存放位置

	- 创建Transaction ORC表及数据存放位置

	- 创建Hyperbase外表及数据存放位置

- 数据迁移

	- Sqoop应用场景

	- Sqoop工具的使用

- 常用BI工具对接

	- Tableau对接

	- JDBC程序对接

	- SQuirreL对接

- Hyperbase操作

	- 全局索引、local索引、全文索引的概念与区别

	- localmode、clustermode的区别
	
	- Hyperbase.reader=true的含义
	
	- 4040页面上的表征
	
	- 怎样查看Hyperbase相关状态
	
	- 一个RegionServer最多host多少个Region？
	
	- 哪些情况会导致数据写入热点？

	- ObjectStore 程序怎么写？Json支持程度？
	
	- Batchinsert是什么？语法怎么写


- Bulkload相关

	- Bulkload的意义和本质

	- 各个阶段的目的

	- Bulkload有几个要点？

	- 什么是SQLbulkload？

	- SQLBulkload操作（TPCDS一张表的数据）

- TPC-DS相关

	- 什么是TPC-DS？

	- TPC-DS中有多少个SQL？

	- 怎样运行TPC-DS？

	- TPC-DS截图
	
	

 ##考试内容一
 
1、Zookeeper/HDFS/YARN/Hyperbase/Inceptor的配置分别放在哪里？

答：放在/etc/服务名1/conf/文件夹中

2、HDFS有哪几个重要的配置？分别怎么设置？

答：namenode、datanode存放的dir目录，在配置选项里可以进行设置

3、YARN有哪几个重要的配置？分别怎么设置？

答：cpu core、memory，在配置里可以设置

4、Fair Schedule如何配置？

答：YARN中的资源管理中可以进行设置，Fair Schedule为公平分配资源原则，一般在集群同构的情况下选择，Capacity Shedule为按需分配原则，一般在集群异构的情况下选择。

5、日志放在哪里？

答：/var/log/[服务名]目录下

6、服务启停，查看都开了哪些服务，如何启停服务？

答：/etc/init.d/服务名 [start｜stop｜restart]

7、产品升级流程

答：详见Transwarp Data Hub4.3产品手册160页

8、Ganglia界面如何查看

答：默认端口为8652，可以监控集群（CPU、内存、网络）状态

##考试内容二 

# 问题二：
1.tdh是什么，有什么作用，有哪些组件

答：tdh全称Transwarp data hub，它是全面支持Spark和SQL 2003解析的Hadoop商业发行版，主要包括Transwarp Manager管理平台、Inceptor交互式查询引擎、Hyperbase非关系型数据库、Stream流式计算引擎、Discover数据挖掘等组建。

2.基本的linux命令 

答：详见https://lemonal88.gitbooks.io/tdh-/content/master/gettingstartmd.html

3.PL/SQL的使用

答：详见Inceptor Manual手册

4.Inceptor的几种登陆方式 

答：Inceptor主要有四种登陆方式，HiveServer1（无安全认证模式）、HiveServer2（安全认证模式）、HiveServer2（LDAP认证模式）、HiveServer2（Kerberos认证模式）

Hive1的登陆方式为： transwarp -t -h <hiveserver1>

Hive2的登陆方式为：beeline -u jdbc:hive2://<hiveserver2>:10000

Hive2LDAP登陆方式为：beeline -u jdbc:hive2://<hiveserver2>:10000 -n <账户名> －p <密码>

Hive2Kerberos登陆方式：
(1)kinit <用户名>
(2)beeline -u jdbc:hive2://<hiveserver2>:10000;principal=hive/<hiveserver2>@TDH

5.hyperbase的架构

工作流程：

(1)、客户端进行put和delete操作给HRegion Server，会被HRegion Server分配到相应的HRegion中执行，用户数据首先被写入到MemStore和Hlog中，当操作写入Hlog中以后，才会commit给用户

(2)、每个Region是被HRegion Server按行划分出来的许多子表（每张子表的大小为100-200M），每个HRegion里面包含多个列族，这些列族就是一个个Store，所以说一个列族就是一个Store，其中Store里面包含一个MemStore和多个StoreFile，MemStore是在内存中的缓存，保存最新的更新数据，StoreFile是磁盘中的文件，StoreFile在底层的实现方式是HDFS文件系统的HFile，HFile的数据块通常采用压缩方式存储，压缩以后可以大大减少网络I/O和磁盘I/O，MemStore缓存的容量有限，系统会周期性的将MemStore中缓存的数据内容写入到磁盘的StoreFile文件中，每次缓存写入就会生成一个新的StoreFile文件，因此，会出现很多StoreFile小文件，当需要访问某个Store中的某个值时，就必须查找所有这些StoreFile，非常耗时，因此为了减少查询时间，系统一般会将这些生成的若干个StoreFile进行合并操作（当StoreFile文件的数量达到一个阈值的时候才会触发合并操作），例如第一个StoreFile上记录的是添加一行a记录，第二个StoreFile上记录的是删除a记录（删除在HBase中只是打上删除标记，只有在合并的时候才会真正被丢弃），所以当merge时，文件由小变大，同时记录a也被删除了。

(3)、补充:HMaster只是负责维护表和Region的元数据，即一张表被划分到哪些Region上的，具体怎么划分，划分成多少个Region是由RegionServer来执行的。那么怎样通过rowid来查询呢，就是通过表名+主键+RegionID来定位查询的

6.新硬盘安装步骤

在Linux中安装第2块硬盘需要注意：如果是IDE硬盘，注意主、从盘的设置；如果是SCSI硬盘，注意选择一个没有被使用的ID号。本例将第2块IDE新硬盘设置成从盘，将它与第一块主盘接到一条数据线上来看看如何在Linux下安装和使用第2个硬盘。

一、分区

一块新的硬盘，它还没有分区，装上后，在Linux中，必须要用硬盘分区程序fdisk（以红旗桌面版为例）分区。

二、格式化

建立好硬盘分区后，执行[root@work root]# mke2fs /dev/hdb1命令，对硬盘进行格式化操作。

三、挂载到目录

硬盘格式化之后，可利用mount命令将该硬盘分区挂载到一个目录上。先创建该目录。如要挂载的目录名为 /disk2，依次执行“ mkdir disk2”、“mount /dev hdb1 /disk2”命令，就可以使用该新硬盘。

四、启动时自动挂载

按照上面的操作，每次机器启动都必须执行mount命令进行挂载才可使用，这样做很麻烦，可以通过修改/etc/fstab配置文件,使机器在启动时自动挂载该硬盘分区。修改完配置文件后重新启动计算机，系统就会自动挂载该分区。至此，你就可以方便地使用你的第2块硬盘了！

7.namenode是什么

答：namenode为HDFS中最重要的节点之一，它是管理文件系统的命名空间，它维护者文件系统所有的文件的元数据，以及每个文件中各个块所在的数据节点信息

8.建立Inceptor external表和Hyperbase表的格式

外表：

eg>create external table userinfo(name string,age int) row format delimited fields terminated by ',' location 'user/datadir';

hbase外表（映射表）：

eg>create external table hbase_test(id string, name string,sex string)
stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
with serdeproperties('hbase.columns.mapping'=':key,info:name,info:sex') tblproperties('hbase.table.name'='test');

9.yarn和inceptor、hbase的关系

答：yarn属于hadoop平台中的一个重要组件，它是对集群中的各个组件进行资源管理和分配的，而inceptor是查询计算引擎，运行时是向yarn申请所需cpu、内存等资源进行计算的，hbase同理

10.HDFS的存储过程

HDFS写过程：1、客户端将需要写的文件发消息给NameNode 2、NameNode发消息给客户端，让客户端将文件写入到随机的三台DataNode中，并让客户端直接和其中一台DataNode进行交互 3、直到3台DataNode都顺利写入以后再返回给客户端，完成写入操作

HDFS读过程：1、客户端询问NameNode应从哪些节点中取数据 2、NameNode将存放客户端所需的DataNode节点的地址返回给客户端，并让客户端直接和DataNode进行交互，完成读文件操作。


11.外表和普通表的区别，分区分桶表在具体情况下的使用

答:(1)外表中数据只是引用数据，它是HDFS文件系统中数据的虚拟指向，当删除表时，表中的数据是不会被删除的，而普通表存放的是HDFS真实路径中的数据，当删除表时，HDFS中数据也将会被删除。

(2)分区表一般是按照时间进行划分的，而分桶表是在分区表中对数据进行继续划分的，分区分桶表一般是结合起来使用的

12.文件从本地导入进HDFS，再导出到本地的具体命令，如果权限不够怎么办
答：
数据导入进HDFS：hadoop fs -put <本地路径> <HDFS路径> 
HDFS导出到本地：hadoop fs -get <HDFS目录> <本地目录>

13.Kerborers认证
答：详见TDH4.3版本安全手册

14.查看inceptorserver所在节点的ip地址
答：在Manager中点击Inceptor节点，右上角查看server节点即可

15.查看CPU使用情况
答：top命令即可





