# 一 数据库与表

##数据库基本操作命令

###1 选择数据库命令

* Mysql:

```
登录方式:
#直接本地登录 root:123456
#mysql -u root -p 
#远程登录 192.168.1.178 chu888chu888:skybar
#mysql -h 192.168.1.178 -u chu888chu888 -p


mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| Northwind          |
| Pubs               |
| ReportServer       |
| hive               |
| hive_hadoop        |
| hivetestdb         |
| mysql              |
| performance_schema |
+--------------------+
9 rows in set (0.10 sec)

mysql> use Pubs;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+----------------+
| Tables_in_Pubs |
+----------------+
| authors        |
| discounts      |
| employee       |
| jobs           |
| pub_info       |
| publishers     |
| roysched       |
| sales          |
| stores         |
| titleauthor    |
| titles         |
+----------------+
11 rows in set (0.00 sec)

mysql> show columns from jobs;
+----------+---------------------+------+-----+-----------------------------------------+----------------+
| Field    | Type                | Null | Key | Default                                 | Extra          |
+----------+---------------------+------+-----+-----------------------------------------+----------------+
| job_id   | smallint(6)         | NO   | PRI | NULL                                    | auto_increment |
| job_desc | varchar(50)         | NO   |     | New Position - title not formalized yet |                |
| min_lvl  | tinyint(3) unsigned | NO   |     | NULL                                    |                |
| max_lvl  | tinyint(3) unsigned | NO   |     | NULL                                    |                |
+----------+---------------------+------+-----+-----------------------------------------+----------------+
4 rows in set (0.00 sec)

mysql> show status;
+------------------------------------------+-------------+
| Variable_name                            | Value       |
+------------------------------------------+-------------+
| Aborted_clients                          | 0           |
| Aborted_connects                         | 0           |
| Binlog_cache_disk_use                    | 0           |
| Binlog_cache_use                         | 0           |
| Binlog_stmt_cache_disk_use               | 0           |
| Binlog_stmt_cache_use                    | 0           |
----------------------------------------------------------


```

* Oracle:
等待补充

* DB2:
等待补充

* Inceptor:

```
[root@dhc-1 ~]# beeline -u jdbc:hive2://192.168.1.70:10000/
scan complete in 2ms
Connecting to jdbc:hive2://192.168.1.70:10000/
2016-03-22 08:33:48,094 INFO jdbc.Utils: Supplied authorities: 192.168.1.70:10000
2016-03-22 08:33:48,094 INFO jdbc.Utils: Resolved authority: 192.168.1.70:10000
Connected to: Apache Hive (version 0.12.0-transwarp-tdh40)
Driver: Hive JDBC (version 0.12.0-transwarp-tdh40)
Transaction isolation: TRANSACTION_REPEATABLE_READ
Beeline version 0.12.0-transwarp-tdh40 by Apache Hive
0: jdbc:hive2://192.168.1.70:10000/> show databases;
+----------------+
| database_name  |
+----------------+
| default        |
+----------------+
1 row selected (2.282 seconds)
0: jdbc:hive2://192.168.1.70:10000/> 

1 row selected (2.282 seconds)
0: jdbc:hive2://192.168.1.70:10000/> use default;
No rows affected (0.068 seconds)
0: jdbc:hive2://192.168.1.70:10000/> show tables;
+-----------+
| tab_name  |
+-----------+
+-----------+
No rows selected (0.08 seconds)
0: jdbc:hive2://192.168.1.70:10000/> 


```

##实验准备数据一 Pubs数据库

为了能在Inceptor中实现兼容性测试,我们必须去移植一下我们样例数据库中的数据(来之微软的Pubs数据库)

###1 载入Inceptor

```
--登录Inceptor server节点
beeline -u jdbc:hive2://192.168.1.70:10000/
```
###2 使用Sqoop将MYSQL数据库导入HDFS

1. 在Inceptor metastore节点服务器上安装sqoop服务
```
yum install sqoop
```
2. 由于Inceptor-SQL中metastore中已经安装了mysql，就不需要安装mysql了

3. 将mysql-connector-java-5.1.38tar.gz驱动包先解压
```
tar -zxvf mysql-connector-java-5.1.38tar.gz
```
4. cd进刚刚解压后的目录，将里面的mysql-connector-java-5.1.38-bin.jar包copy到/usr/lib/sqoop/lib本地目录下

5. 从mysql————>HDFS上（import，将mysql中的db1数据库里面的表导入到/user/datadir，这里的datadir目录一定不要事先创建，不然会报错，语句执行的时候会自动创建目录的！最后一行的－m表示map成4个文件）

```
sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table titleauthor \
--target-dir /user/chu888chu888/data/titleauthor -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table authors \
--target-dir /user/chu888chu888/data/authors -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table authors \
--target-dir /user/chu888chu888/data/employee -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table discounts \
--target-dir /user/chu888chu888/data/discounts -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table jobs \
--target-dir /user/chu888chu888/data/jobs -m 4


sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table pub_info \
--target-dir /user/chu888chu888/data/pub_info -m 4


sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table publishers \
--target-dir /user/chu888chu888/data/publishers -m 4


有一个问题如果表没有主键的话,就会导入不了.
alter table roysched add roysched_id int unsigned not Null auto_increment primary key;

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table roysched \
--target-dir /user/chu888chu888/data/roysched -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table sales \
--target-dir /user/chu888chu888/data/sales -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table stores \
--target-dir /user/chu888chu888/data/stores -m 4

sqoop import \
--username chu888chu888 \
--password skybar \
--connect jdbc:mysql://192.168.1.178:3306/Pubs \
--table titles \
--target-dir /user/chu888chu888/data/titles -m 4

```

6 SQL SERVER导入的问题

```
sqoop import \
--table address \
--connect "jdbc:sqlserver://192.168.1.139:1433;database=AdventureWorks" \
--username=sa \
--password=123456 \
--hive-drop-import-delims \
--null-string '\\N' \
--null-non-string '\\N' \
--fields-terminated-by '\001' \
--target-dir /user/test/address1 -m 1
```


###3 在Inceptor中建立外表结构

```
mysql> desc authors;
+----------+-------------+------+-----+---------+-------+
| Field    | Type        | Null | Key | Default | Extra |
+----------+-------------+------+-----+---------+-------+
| au_id    | varchar(11) | NO   | PRI | NULL    |       |
| au_lname | varchar(40) | NO   | MUL | NULL    |       |
| au_fname | varchar(20) | NO   |     | NULL    |       |
| phone    | varchar(12) | NO   |     | UNKNOWN |       |
| address  | varchar(40) | YES  |     | NULL    |       |
| city     | varchar(20) | YES  |     | NULL    |       |
| state    | varchar(2)  | YES  |     | NULL    |       |
| zip      | varchar(5)  | YES  |     | NULL    |       |
| contract | bit(1)      | NO   |     | NULL    |       |
+----------+-------------+------+-----+---------+-------+
9 rows in set (0.00 sec)
create external table authors
(
  au_id               STRING,
  au_lname            STRING,
  au_fname            STRING,
  phone               STRING,
  address             STRING,
  city                STRING,
  state               STRING,
  zip                 STRING,
  contract            STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/authors';


mysql> desc discounts;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| discounttype | varchar(40)  | NO   |     | NULL    |       |
| stor_id      | varchar(4)   | YES  |     | NULL    |       |
| lowqty       | smallint(6)  | YES  |     | NULL    |       |
| highqty      | smallint(6)  | YES  |     | NULL    |       |
| discount     | decimal(6,2) | NO   |     | NULL    |       |
+--------------+--------------+------+-----+---------+-------+
5 rows in set (0.00 sec)
create external table discounts
(
  discounttype       STRING,
  stor_id            STRING,
  lowqty             STRING,
  highqty            STRING,
  discount           STRING,
  discount_id        STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/discounts';


mysql> desc employee;
+-----------+---------------------+------+-----+---------+-------+
| Field     | Type                | Null | Key | Default | Extra |
+-----------+---------------------+------+-----+---------+-------+
| emp_id    | varchar(9)          | NO   | PRI | NULL    |       |
| fname     | varchar(20)         | NO   |     | NULL    |       |
| minit     | varchar(1)          | YES  |     | NULL    |       |
| lname     | varchar(30)         | NO   | MUL | NULL    |       |
| job_id    | smallint(6)         | NO   |     | 1       |       |
| job_lvl   | tinyint(3) unsigned | YES  |     | 10      |       |
| pub_id    | varchar(4)          | NO   |     | 9952    |       |
| hire_date | date                | YES  |     | NULL    |       |
+-----------+---------------------+------+-----+---------+-------+
8 rows in set (0.00 sec)

create external table employee
(
  emp_id       STRING,
  fname        STRING,
  minit        STRING,
  lname        STRING,
  job_id       STRING,
  job_lvl      STRING,
  pub_id       STRING,
  hire_date    STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/employee';


mysql> desc jobs;
+----------+---------------------+------+-----+-----------------------------------------+----------------+
| Field    | Type                | Null | Key | Default                                 | Extra          |
+----------+---------------------+------+-----+-----------------------------------------+----------------+
| job_id   | smallint(6)         | NO   | PRI | NULL                                    | auto_increment |
| job_desc | varchar(50)         | NO   |     | New Position - title not formalized yet |                |
| min_lvl  | tinyint(3) unsigned | NO   |     | NULL                                    |                |
| max_lvl  | tinyint(3) unsigned | NO   |     | NULL                                    |                |
+----------+---------------------+------+-----+-----------------------------------------+----------------+
4 rows in set (0.00 sec)
create external table jobs
(
  job_id       STRING,
  job_desc     STRING,
  min_lvl      STRING,
  max_lvl      STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/jobs';


mysql> desc pub_info;
+---------+------------+------+-----+---------+-------+
| Field   | Type       | Null | Key | Default | Extra |
+---------+------------+------+-----+---------+-------+
| pub_id  | varchar(4) | NO   | PRI | NULL    |       |
| logo    | longblob   | YES  |     | NULL    |       |
| pr_info | longtext   | YES  |     | NULL    |       |
+---------+------------+------+-----+---------+-------+
3 rows in set (0.00 sec)

create external table pub_info
(
  pub_id       STRING,
  logo         STRING,
  pr_info      STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/pub_info';

mysql> desc publishers;
+----------+-------------+------+-----+---------+-------+
| Field    | Type        | Null | Key | Default | Extra |
+----------+-------------+------+-----+---------+-------+
| pub_id   | varchar(4)  | NO   | PRI | NULL    |       |
| pub_name | varchar(40) | YES  |     | NULL    |       |
| city     | varchar(20) | YES  |     | NULL    |       |
| state    | varchar(2)  | YES  |     | NULL    |       |
| country  | varchar(30) | YES  |     | USA     |       |
+----------+-------------+------+-----+---------+-------+
5 rows in set (0.00 sec)
create external table publishers
(
  pub_id       STRING,
  pub_name     STRING,
  city         STRING,
  state        STRING,
  country      STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/publishers';

mysql> desc roysched;
+----------+------------+------+-----+---------+-------+
| Field    | Type       | Null | Key | Default | Extra |
+----------+------------+------+-----+---------+-------+
| title_id | varchar(6) | NO   | MUL | NULL    |       |
| lorange  | int(11)    | YES  |     | NULL    |       |
| hirange  | int(11)    | YES  |     | NULL    |       |
| royalty  | int(11)    | YES  |     | NULL    |       |
+----------+------------+------+-----+---------+-------+
4 rows in set (0.00 sec)
create external table roysched
(
  title_id    STRING,
  lorange     STRING,
  hirange     STRING,
  royalty     STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/roysched';


mysql> desc sales;
+----------+-------------+------+-----+---------+-------+
| Field    | Type        | Null | Key | Default | Extra |
+----------+-------------+------+-----+---------+-------+
| stor_id  | varchar(4)  | NO   | PRI | NULL    |       |
| ord_num  | varchar(20) | NO   | PRI | NULL    |       |
| ord_date | date        | YES  |     | NULL    |       |
| qty      | smallint(6) | NO   |     | NULL    |       |
| payterms | varchar(12) | NO   |     | NULL    |       |
| title_id | varchar(6)  | NO   | PRI | NULL    |       |
+----------+-------------+------+-----+---------+-------+
6 rows in set (0.00 sec)
create external table sales
(
  stor_id    STRING,
  ord_num    STRING,
  ord_date   STRING,
  qty        STRING,
  title_id   STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/sales';


mysql> desc stores;
+--------------+-------------+------+-----+---------+-------+
| Field        | Type        | Null | Key | Default | Extra |
+--------------+-------------+------+-----+---------+-------+
| stor_id      | varchar(4)  | NO   | PRI | NULL    |       |
| stor_name    | varchar(40) | YES  |     | NULL    |       |
| stor_address | varchar(40) | YES  |     | NULL    |       |
| city         | varchar(20) | YES  |     | NULL    |       |
| state        | varchar(2)  | YES  |     | NULL    |       |
| zip          | varchar(5)  | YES  |     | NULL    |       |
+--------------+-------------+------+-----+---------+-------+
6 rows in set (0.01 sec)

create external table stores
(
  stor_id          STRING,
  stor_name        STRING,
  stor_address     STRING,
  city             STRING,
  zip              STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/stores';

mysql> desc titleauthor;
+------------+---------------------+------+-----+---------+-------+
| Field      | Type                | Null | Key | Default | Extra |
+------------+---------------------+------+-----+---------+-------+
| au_id      | varchar(11)         | NO   | PRI | NULL    |       |
| title_id   | varchar(6)          | NO   | PRI | NULL    |       |
| au_ord     | tinyint(3) unsigned | YES  |     | NULL    |       |
| royaltyper | int(11)             | YES  |     | NULL    |       |
+------------+---------------------+------+-----+---------+-------+
4 rows in set (0.00 sec)

create external table titleauthor
(
  au_id STRING,
  title_id STRING,
  au_ord TinyInt,
  royaltyper INT
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/titleauthor';


mysql> desc titles;
+-----------+---------------+------+-----+-----------+-------+
| Field     | Type          | Null | Key | Default   | Extra |
+-----------+---------------+------+-----+-----------+-------+
| title_id  | varchar(6)    | NO   | PRI | NULL      |       |
| title     | varchar(80)   | NO   | MUL | NULL      |       |
| type      | varchar(12)   | NO   |     | UNDECIDED |       |
| pub_id    | varchar(4)    | YES  |     | NULL      |       |
| price     | decimal(19,4) | YES  |     | NULL      |       |
| advance   | decimal(19,4) | YES  |     | NULL      |       |
| royalty   | int(11)       | YES  |     | NULL      |       |
| ytd_sales | int(11)       | YES  |     | NULL      |       |
| notes     | varchar(200)  | YES  |     | NULL      |       |
| pubdate   | datetime      | NO   |     | NULL      |       |
+-----------+---------------+------+-----+-----------+-------+
10 rows in set (0.00 sec)

create external table titles
(
  title_id STRING,
  title    STRING,
  type     STRING,
  pub_id   STRING,
  price    STRING,
  advance  STRING,
  royalty  STRING,
  ytd_sales STRING,
  notes     STRING,
  pubdate   STRING
)row format delimited fields terminated by ',' location '/user/chu888chu888/data/titles';

```








