# Hive的模式设计

## 一 概述

Hive看上去以及实际行为都像一个关系型数据库.用户对如表和列这类术语比较熟悉,而且Hive提供的查询语言和用户之前使用过的SQL方言非常相似.不过Hive实现和使用的方式和传统的关系型数据库是非常不同的.通常,用户视图移植关系型数据库中的模式,而事实上Hive是反模式

### 1 按天划分的表

按天划分表就是一种模式,其通常会在表中加入一个时间戳,例如表名为upply_2011_01_01等等.这种每天一张表的方式在数据库领域是反模式的一种方式,但是因为实际情况下数据集增长得很快,这种方式应用还是比较广泛的.

```
0: jdbc:hive2://hadoopmaster:10000/> CREATE TABLE supply_2011_01_02(id int,part string,quantity int);
OK
No rows affected (1.279 seconds)
0: jdbc:hive2://hadoopmaster:10000/> CREATE TABLE supply_2011_01_03(id int,part string,quantity int);
OK
No rows affected (0.055 seconds)
0: jdbc:hive2://hadoopmaster:10000/> CREATE TABLE supply_2011_01_04(id int,part string,quantity int);
OK
No rows affected (0.056 seconds)
0: jdbc:hive2://hadoopmaster:10000/> 

0: jdbc:hive2://hadoopmaster:10000/> select part,quantity supply_2011_01_02 from supply_2011_01_02
. . . . . . . . . . . . . . . . . .> union all
. . . . . . . . . . . . . . . . . .> select part,quantity supply_2011_01_02 from supply_2011_01_03
. . . . . . . . . . . . . . . . . .> where quantity<4;

```

对于Hive,这种情况下应该使用分区表.Hive通过Where子句中的表达式来选择查询所需要的指定分区,这样的查询执行效率高,而且看起来清晰明了:

```
0: jdbc:hive2://hadoopmaster:10000/> CREATE TABLE supplybypartition (id int,part string,quantity int)
. . . . . . . . . . . . . . . . . .> partitioned by (day int);

0: jdbc:hive2://hadoopmaster:10000/> alter table supplybypartition add partition(day=20110102);
OK
No rows affected (0.088 seconds)
0: jdbc:hive2://hadoopmaster:10000/> alter table supplybypartition add partition(day=20110103);
OK
No rows affected (0.067 seconds)
0: jdbc:hive2://hadoopmaster:10000/> alter table supplybypartition add partition(day=20110104);
OK
No rows affected (0.083 seconds)

0: jdbc:hive2://hadoopmaster:10000/> select * from supplybypartition
. . . . . . . . . . . . . . . . . .> where day>=20110102 and day<20110103 and quantity<4;
OK
+-----------------------+-------------------------+-----------------------------+------------------------+--+
| supplybypartition.id  | supplybypartition.part  | supplybypartition.quantity  | supplybypartition.day  |
+-----------------------+-------------------------+-----------------------------+------------------------+--+
+-----------------------+-------------------------+-----------------------------+------------------------+--+
No rows selected (0.162 seconds)
0: jdbc:hive2://hadoopmaster:10000/> 

```

### 2 关于分区

Hive中分区的功能是非常有用的,这是因为Hive通常要对输入进行全盘扫描,来满足查询条件,通过创建很多的分区确定可以优化一些查询,但是同时可能会对其他一些重要的查询不利:

HDFS用于设计存储数百万的大文件,而非数十亿的小文件.使用过多分区可能导致的一个问题就是会创建大量的非必须的hadoop文件和文件夹.一个分区就对应着一个包含有多个文件的文件夹.如果指定的表存在数百个分区,那么可能每天都会创建好几万个文件.如果保持这样的表很多年,那么最终就会超出NameNode对系统云数据信息的处理能力.因为NameNode必须将所有系统文件的元数据保存在内存中.虽然每个文件只需要少量字节大小的元数据(大约是150字节/文件),但是这样也会限制一个HDFS实例所能管理的文件总数的上限.而其他的文件系统,比如MapR和Amazon S3就没有这个限制.

MapReduce会将一个任务(job)转换成多个任务(task).默认情况下,每个task都是一个新的JVM实例,都需要开启和销毁的开销.对于小文件,每个文件都会对应一个task.在一些情况下,JVM开启和销毁的时间中销毁可能会比实际处理数据的时间消耗要长

因此,一个理想的分区方案不应该导致产生太多的分区和文件夹目录,并且每个目录下的文件应该足够大,应该是文件系统中块大小的若干倍.

接时间范围进行分区的一个好的策略就是按照不同的时间颗粒度来确定合适大小的数据积累量,而且安装这个时间颗粒.随着时间的推移,分区数量的增长是均匀的,而且每个分区下包含的文件大小至少是文件系统中块或块大小的数倍.

**如果用户找不到好的,大小相对合适的分区方式的话,我们可以考虑使用分桶表来解决问题**

### 3 关于分桶表数据存储

