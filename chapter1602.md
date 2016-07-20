# Inceptor-SQL使用

##简介

Inceptor是一种交互式分析引擎，本质是一种SQL翻译器。Inceptor中一共可以操作五种类型的表结构：

**1. 普通文本表（TXT表）** 

**2. 分区表（分单值分区和范围分区）**

**3. 分桶表**

**4. ORC表（Hive ORC格式）**

**5. ORC事务表（可进行增删改查操作，必须建立分桶表和外表，且两个表的表格式要和源数据字段一一对应起来）**

注意：一般来说分区和分桶是结合来使用的，例如先将一个大表按照时间进行分区，再对每个分区进行分桶。

###一、普通表导入数据
1. 从HDFS导入数据

 1. 创建HDFS数据目录，在本地创建一个存放数据的文件夹，为了区分不同用户和不同数据源，建立以下两个目录
```
hadoop fs -mkdir -p /user/user1/data／inceptor
hadoop fs -mkdir -p /user/user1/data／hyperbase
```

 1. 首先将本地path存放的数据文件put到HDFS目录中，数据可以存放在集群中的任意一台机器中（注意本步操作可能会报load数据没有权限，HDFS上的数据和表的权限不一致
使用：（sudo -u hdfs hadoop fs -chown -R hive /user/＊）命令进行owner的修改，hive为owner名字）或者使用sudo -u hdfs hadoop fs -chmod -R 777 /user/*
```
hadoop fs -put <path>/data.txt /user/user1/data/inceptor
```

 1. 将上传进HDFS的文件load到Inceptor事先建立好的s3表中,在Inceptor中输入如下命令：
```
load data inpath ‘/user/user1/data/inceptor/data.txt’ into table s3;
```

2. 从其他表导入

 2. 将t3的表结构复制给t4，注意不复制数据
```
create table t4 like t3;
```

 2. 查看
```
select * from t4;
```

 2. 将t3表中的数据插入到t4表中
```
insert into table t4 select * from t3;
```


###二、分区表

1. 创建单值分区

 1. 创建单值分区表（每创建一个单值分区表就会产生一个小文件，这里只有一个name值）
```
create table single_tbl(name string) partitioned by(level string);
```
(注意后面的partition分区键和文本是无关的！文本只导入name！分区键是通过load语句中的level具体标识来指定的)

 1. 把本地包含单列数据的txt文件put到HDFS中的/user/datadir目录中
```
hadoop fs -put /tmp/a.txt /user/datadir 
```

 1. 将HDFS中的a.txt文件load到single_tbl单值分区表，即将a这个文档都设置成A标签
```
load data inpath ‘user/datadir/a.txt’ single_tbl partition(level='A');
```

2. 创建范围分区表（用于避免全表扫描，快速检索，导入数据的方法也很少，只能通过从另一个表插入到范围表中，其产生原因是为了规避单值分区每创建一个表就会产生一个小文件，而范围分区则是每个分区存储一个文件）

 2. 创建范围分区表rangepart
```
create table rangepart(name string)partitioned by range(age int)(
            partition values less than(30),
            partition values less than(40),
            partition values less than(50),
            partition values less than(MAXVALUE)
);
```
（注意分区表为左闭右开区间）

 2. 将本地文件或文件夹put到HDFS的user/datadir的目录中
```
hadoop fs -put /tmp/b.txt user/datadir
```

 2. 创建外表，来将HDFS中的文件进行导入进来(外表是用来指定导入数据格式的，且drop外表时，HDFS上的数据还存在)
```
create external table userinfo(name string,age int) row format delimited fields terminated by ',' location 'user/datadir';
```

 2. 将外表的数据插入到建立好的rangepart表中
```
insert into table rangepart select * from userinfo;
```

 2. 查看插入分区表里的数据分布
```
show partitions rangepart;
```


###三、分桶表
（必须创建外表，只支持从外表导入数据，在分桶表中经常做聚合和join操作，速度非常快。另外分桶规则主要分为1、int型，按照数值取模，分几个桶就模几2、string型，按照hash表来分桶）

1. 创建分桶表bucket_tbl(这里分桶的大小是用表的总数据大小除以200M，经实际优化测试，每个桶的数据为200M处理速度最优)
```
create table bucket_tbl(id int, name string) clustered by (id) into 3 buckets;
```

2. 创建外表bucket_info,bucket_info表会自动将HDFS目录/user/datadir中的数据自动load进表里，这和普通表需要手动进行load不一样
```
create external table bucket_info(id int, name string)row format delimited fields terminated by ',' location '/user/datadir';
```

3. 将从本地txt文件put到HDFS中的表（如普通表），再load进外表中
```
load data inpath '/user/tdh/data/bucket-data' into table bucket_info;
```

4. 设置分桶开关
```
set hive.enforce.bucketing=true;
```

5. 插入数据（按照取模大小顺序排列）
```
insert into table bucket_tbl select *from bucket_info;
```

###四、holodesk表
（holodesk表既可以基于内存也可以基于ssd存储和查询，holodesk会存两份，一份存在内存或者ssd中，一份存在HDFS中，这样可能在查询的性能上有所延迟）holodesk最擅长处理groupby的SQL查询语句

说明：建立holodesk表之前最好先建立cube，cube一般为3-5列，表很小，在Inceptor中建立cube内表，取的速度很快，遍历会很快，cube不能将所有的数据都放入内存，所以建内表时，将部分需要的数据放在内存中，因为cube只有3-4列，大大简化了原ssd中的大数据集，查询速度会很快，所以说一般holodesk是和cube配合使用的。内存表创建有两种方式：第一种通过CTAS建表，建表时数据即填入，这种情况下，内存表不能分区或者分桶。第二种通过创建空表，此时内存表可以分区或者分桶，之后可以通过Insert into select插入数据。

（1）通过CTAS（create table...as select）建表

```
create table table_name tblproperties("cache"="cache_medium","cache.checkpoint"="true|false",["filters"="filter_type"]) as select id,name,sex,date from user_info;
```


（2）通过创建空表建表，再插入数据

```
CREATE TABLE table_name (column_name data_type, column_name data_type...) PARTITIONED BY (partition_key data_type, partition_key data_type) CLUSTERED BY (cluster_key,
cluster_key, ...) INTO n BUCKETS TBLPROPERTIES ("cache" = "cache_medium", "cache.checkpoint"="true|false", ["filters"="filter_type"])

```

说明：

"cache"="cache_medium"指定计算缓存的介质。可以选择ram,SSD和memeory三种。只有当服务器上配置有SSD时,才可以选择SSD作为缓存,Inceptor会自动利用SSD为计算加速。"

cache.checkpoint"="true|false"指定是否设置checkpoint。如果设置checkpoint,查询 结果会被同步放入HDFS中,在存储了内存表的机器当机时,内存表中的数据可以从HDFS中 直接读取恢复而不需要重新进行查询计算。
"filters"="filter_type"为可选项,它指定一个过滤器。利用过滤器可以为某些查询进行优化



###五、建立ORC格式表，如下三种方式

（1）
```
create table country（id int，country string）stored as orc;
```

（2）
```
create external table ex_tbl(id int,country string)
        row format delimited fields terminated by ','
        stored as textfile
        location '/user/tdh/externaltbl';
```

（3）
```
insert into country select * from ex_tbl;
```





###六、建立ORC格式事务表（必须要分桶，既可以单值插入，又可以通过外表插入）
（1）
```
create table orc_tbl(id int, country string) clustered by (id) into 3 buckets stored as orc tblproperties("transactional" = "true");
```
（2）(创建外表需要注意的是，一定要指定分隔符，不然当external表自动加载HDFS中的/user/datadir时不知道以什么分隔数据，造成查询出的数据全部都是null值)
```
create external table external_tbl(id int,country string) row format delimited fields terminated by ',' location '/user/datadir';
```

（3）设置分桶开关

```
set hive.enforce.bucketing=true;
```
（4）
```
insert into orc_tbl select * from external_tbl;
```

（注意：ORC只是一种表的格式类型，建表时指定了transactional" = "true"，则表明这是一个事务表，必须要分桶，若没有指定则只是普通的ORC表，不需要进行分桶操作）



##注意事项：

- HDFS不能直接load到Inceptor中的ORC事务表中，(只能load到普通表和ORC表中)要想在ORC事务表里插入数据有两种方法：a.建立一张外表，再将HDFS load进外表上，再insert into select * from external table    b.由于ORC事务表支持增删改查，也可以使用单值插入语句insert into table country values(101,japan)

- 查看分区表的命令是show partitions [table名] 

- 查看每个表的创建时语句命令是show create table [table名]

- 使用命令hdfs dfs -ls /user/country（或者使用hadoop fs -ls /user/country命令）

- 默认数据库存放位置
hdfs：//nameservice/inceptorsql1/user/hive/warehouse/
在Inceptor创建数据库时一般使用它的default默认数据库，若自己建立数据库请不要指定location，还有自己建立的数据库可能会因为权限不够而造成一些操作失败报错。可以使用hadoop fs -ls /inceptorsql1/user/hive/warehouse查看默认目录下存储的数据
，eg：

（1）

        create database ccc location '/user/ccc'；
    
        create table ccc1;
        
    上述语句建立的数据库位置为user/ccc/hive/ccc1


（2）

        create table ccc2(a int) location 'user/ccc2';
    上述语句建立表的位置在user/ccc2

- 外表的作用是load导数据使用的，起到的是媒介作用，而ORC表则是做具体的操作的，外表一般是和ORC事务表配合使用的

- 分区表中的单值插入数据必须指定level

- 分桶中的桶大小，即一个文件大小一般为200M，处理效率最优，拿总文件大小除以200M就大概预估出分几个桶了

- 从HDFS中向Mysql中导入数据规定必须先在Mysql中创建临时表，先从HDFS的location目录下导入到tmp表中，再从tmp表导入到Mysql真正的表中

- Flume需要先使用yum install flume命令安装，Flume的默认存放位置为/user/lib/flume/conf/flume.conf，vi进去后进行相应的修改，有两个位置需要注意，第一个是spoolDir后跟log所在HDFS中的文件夹名！切记，不是跟具体的log文件或者txt文件！（如：spoolDir=/tmp/flume/），第二个是path后面是Active NameNode的HDFS路径
（如：path=hdfs://172.16.2.77:8020/user/datadir），在flume.conf配置中默认指定缓冲区积攒到1k就写入HDFS中

- 养成在Inceptor中使用命令desc formatted <table名>;来查看各个表的底层结构和属性

-   
    hadoop fs：命令使用面最广，可以操作任何文件系统。
    
    hdfs dfs：命令只能操作HDFS文件系统相关。

##附录（示例代码）
```
--登录Inceptor server节点
beeline -u jdbc:hive2://172.16.2.75:10000/

--DDL
--创建数据库(location 中文件的权限需和数据库owner一致)
--没有指定location，则数据库放在默认位置
--额外属性
create database db4
comment 'this is a database'
location '/user/test/'
with dbproperties('owener' = 'transwarp','time'='2016');

--在默认位置创建数据库，使用desc 查看数据库 
create database db2;
 
--删除数据库(数据库需为空)
drop database dbname

--描述数据库
describe database dbname;

--修改数据库
alter database db1 set dbproperties('owener'='hehe');


----------------------------创建简单text表--------------------------
--创建普通text表
create table t1(col1 string, col2 int);
create table t3(col1 int, col2 string)row format delimited fields terminated by ',' location '/user/datadir';
create table t2 like t1;
create table t4 as select col1 from t3;

--导入数据
--从hdfs导入数据(注意owner)
load data inpath '/user/datadir/data' into table t3;

--从其他表中插入
insert into table t4 select * from t3;



--创建分区表
--单值分区
create table part_t6(name string) partitioned by (level string);
--分区表导入数据，一次导入一个分区。分区键是从sql语句中定义，并不是从文件中导入。
--在本句中，如果文件有两列数据，则第二列是无效的。
load data inpath '/user/datadir/part-data1' into table part_t6 partition(level='A')//level变成A


--创建范围分区表(不支持从文件导入分区表，支持insert into select from)
create table rangepart_t7 (name string)partitioned by range(age int)(
partition values less than(30),
partition values less than(40),
partition values less than(50),
partition values less than(MAXVALUE)
);
--普通数据表
create external table user_info(name string, age int)row format delimited fields terminated by ',' location '/user/datadir';
load data inpath '/user/tdh/data/range-part1' into table user_info;
--通过普通数据表向范围分区表插入数据
insert into table rangepart_t7 select * from user_info;
select * from range_part_t6;
--查看分区信息
show partitions rangepart_t7;
--查看底层文件



-------------------------

--分桶表创建(数据只能通过insert插入，load无效)
--创建分桶表
--在windows下创建的文本文件是一\r\n换行，在unix下以\n换行，
--导入数据应该是unix格式的。
---以数字为bucket id
drop table if exists bucket_tbl;
create table bucket_tbl(id int, name string)clustered by (id) into 3 buckets;
--创建普通数据表
drop table if exists bucket_info;
create external table bucket_info(id int, name string)row format delimited fields terminated by ',' location '/user/datadir';
--导入数据到数据表中
load data inpath '/user/tdh/data/bucket-data' into table bucket_info;
--插入数据到分桶表中
set hive.enforce.bucketing = true;
insert into table bucket_tbl select *from bucket_info; 

---以字符串为bucket id,
drop table if exists bucket_tbl;
create table bucket_tbl(id int, name string)clustered by (name) into 3 buckets;
--创建普通数据表
drop table if exists bucket_info;
create external table bucket_info(id int, name string)row format delimited fields terminated by ',' location '/user/datadir';
--导入数据到数据表中
load data inpath '/user/tdh/data/bucket-data' into table bucket_info;
--插入数据到分桶表中
set hive.enforce.bucketing = true;
insert into table bucket_tbl select *from bucket_info;


----创建外表
drop table if exists ;
create external table ex_tbl(id int,country string)
row format delimited fields terminated by ','
stored as textfile
location '/user/tdh/externaltbl';
----------------------------------------------------------------------------------------

----建立ORC表
drop table if exists country;
create table country (id int, country string)stored as orc;

----建立ORC事务表
drop table if exists country;
create table  country(id int, country string) clustered by (id)into 3 buckets stored as orc tblproperties("transactional" = "true");
insert into table country select * from ex_tbl;
insert into table country values(100,'japan');
insert into table country values(101,'isis');


--创建分区分桶ORC表，分区字段不能用date类型，
drop table if exists country;
create table country (id int, country string) partitioned by(level string) clustered by (id)into 3 buckets stored as orc tblproperties("transactional" = "true");
--从外表插入数据
insert into country partition (level='A') select * from ex_tbl where id<5;
--单条插入
insert into table country partition (level='C') values(101,'isis');
```
