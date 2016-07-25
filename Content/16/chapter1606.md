# 第二章 进入SQL兼容性测试

##查询数据

**1 DISTINCT关键字**

Mysql:

```
mysql> select DISTINCT title_id from titles;
+----------+
| title_id |
+----------+
| BU1032   |
| BU1111   |
| BU2075   |
| BU7832   |
| MC2222   |
| MC3021   |
| MC3026   |
| PC1035   |
| PC8888   |
| PC9999   |
| PS1372   |
| PS2091   |
| PS2106   |
| PS3333   |
| PS7777   |
| TC3218   |
| TC4203   |
| TC7777   |
+----------+
18 rows in set (0.00 sec)

```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select DISTINCT title_id from titles;
+-----------+
| title_id  |
+-----------+
| BU7832    |
| BU2075    |
| MC3026    |
| MC2222    |
| PS7777    |
| PS1372    |
| PC8888    |
| TC7777    |
| BU1032    |
| MC3021    |
| PC9999    |
| TC3218    |
| PC1035    |
| BU1111    |
| PS2091    |
| PS3333    |
| PS2106    |
| TC4203    |
+-----------+
18 rows selected (7.386 seconds)

```

**2 限制结果**

Mysql:

```
mysql> select title from titles limit 5;
+-----------------------------------------------------------------+
| title                                                           |
+-----------------------------------------------------------------+
| But Is It User Friendly?                                        |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |
| Cooking with Computers: Surreptitious Balance Sheets            |
| Emotional Security: A New Algorithm                             |
| Fifty Years in Buckingham Palace Kitchens                       |
+-----------------------------------------------------------------+
5 rows in set (0.00 sec)

```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title from titles limit 5;
+-------------------------------------------------------+
|                         title                         |
+-------------------------------------------------------+
| The Busy Executive's Database Guide                   |
| Cooking with Computers: Surreptitious Balance Sheets  |
| You Can Combat Computer Stress!                       |
| Straight Talk About Computers                         |
| Silicon Valley Gastronomic Treats                     |
+-------------------------------------------------------+
5 rows selected (1.918 seconds)

```

##排序查询数据

**1 Order语句:**

Mysql:

```
mysql> select title,price from titles order by price;
+-----------------------------------------------------------------+---------+
| title                                                           | price   |
+-----------------------------------------------------------------+---------+
| Net Etiquette                                                   |    NULL |
| The Psychology of Computer Cooking                              |    NULL |
| You Can Combat Computer Stress!                                 |  2.9900 |
| The Gourmet Microwave                                           |  2.9900 |
| Life Without Fear                                               |  7.0000 |
| Emotional Security: A New Algorithm                             |  7.9900 |
| Is Anger the Enemy?                                             | 10.9500 |
| Cooking with Computers: Surreptitious Balance Sheets            | 11.9500 |
| Fifty Years in Buckingham Palace Kitchens                       | 11.9500 |
| Sushi, Anyone?                                                  | 14.9900 |
| Prolonged Data Deprivation: Four Case Studies                   | 19.9900 |
| Silicon Valley Gastronomic Treats                               | 19.9900 |
| Straight Talk About Computers                                   | 19.9900 |
| The Busy Executive's Database Guide                             | 19.9900 |
| Secrets of Silicon Valley                                       | 20.0000 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean | 20.9500 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations | 21.5900 |
| But Is It User Friendly?                                        | 22.9500 |
+-----------------------------------------------------------------+---------+
18 rows in set (0.00 sec)

mysql> select title,price from titles order by price desc;
+-----------------------------------------------------------------+---------+
| title                                                           | price   |
+-----------------------------------------------------------------+---------+
| But Is It User Friendly?                                        | 22.9500 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations | 21.5900 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean | 20.9500 |
| Secrets of Silicon Valley                                       | 20.0000 |
| The Busy Executive's Database Guide                             | 19.9900 |
| Prolonged Data Deprivation: Four Case Studies                   | 19.9900 |
| Straight Talk About Computers                                   | 19.9900 |
| Silicon Valley Gastronomic Treats                               | 19.9900 |
| Sushi, Anyone?                                                  | 14.9900 |
| Cooking with Computers: Surreptitious Balance Sheets            | 11.9500 |
| Fifty Years in Buckingham Palace Kitchens                       | 11.9500 |
| Is Anger the Enemy?                                             | 10.9500 |
| Emotional Security: A New Algorithm                             |  7.9900 |
| Life Without Fear                                               |  7.0000 |
| The Gourmet Microwave                                           |  2.9900 |
| You Can Combat Computer Stress!                                 |  2.9900 |
| The Psychology of Computer Cooking                              |    NULL |
| Net Etiquette                                                   |    NULL |
+-----------------------------------------------------------------+---------+
18 rows in set (0.00 sec)


```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,price from titles order by price;
+------------------------------------------------------------------+---------------+
|                              title                               |     price     |
+------------------------------------------------------------------+---------------+
| Sushi                                                            | 0877          |
| Is Anger the Enemy?                                              | 10.9500       |
| Cooking with Computers: Surreptitious Balance Sheets             | 11.9500       |
| Fifty Years in Buckingham Palace Kitchens                        | 11.9500       |
| The Busy Executive's Database Guide                              | 19.9900       |
| Straight Talk About Computers                                    | 19.9900       |
| Silicon Valley Gastronomic Treats                                | 19.9900       |
| Prolonged Data Deprivation: Four Case Studies                    | 19.9900       |
| You Can Combat Computer Stress!                                  | 2.9900        |
| The Gourmet Microwave                                            | 2.9900        |
| Secrets of Silicon Valley                                        | 20.0000       |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  | 21.5900       |
| But Is It User Friendly?                                         | 22.9500       |
| Life Without Fear                                                | 7.0000        |
| Emotional Security: A New Algorithm                              | 7.9900        |
| The Psychology of Computer Cooking                               | null          |
| Net Etiquette                                                    | null          |
| Onions                                                           | trad_cook     |
+------------------------------------------------------------------+---------------+
18 rows selected (1.993 seconds)

```
##过滤数据

Mysql:

```
mysql> select title from titles where price>=14;
+-----------------------------------------------------------------+
| title                                                           |
+-----------------------------------------------------------------+
| The Busy Executive's Database Guide                             |
| Straight Talk About Computers                                   |
| Silicon Valley Gastronomic Treats                               |
| But Is It User Friendly?                                        |
| Secrets of Silicon Valley                                       |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |
| Prolonged Data Deprivation: Four Case Studies                   |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean |
| Sushi, Anyone?                                                  |
+-----------------------------------------------------------------+
9 rows in set (0.00 sec)

mysql> select title from titles where price between 5 and 10;
+-------------------------------------+
| title                               |
+-------------------------------------+
| Life Without Fear                   |
| Emotional Security: A New Algorithm |
+-------------------------------------+
2 rows in set (0.00 sec)

mysql> select title from titles where price IS NULL;
+------------------------------------+
| title                              |
+------------------------------------+
| The Psychology of Computer Cooking |
| Net Etiquette                      |
+------------------------------------+
2 rows in set (0.00 sec)


```

inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title from titles where price>=14;
+------------------------------------------------------------------+
|                              title                               |
+------------------------------------------------------------------+
| The Busy Executive's Database Guide                              |
| Straight Talk About Computers                                    |
| Silicon Valley Gastronomic Treats                                |
| But Is It User Friendly?                                         |
| Secrets of Silicon Valley                                        |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  |
| Prolonged Data Deprivation: Four Case Studies                    |
| Sushi                                                            |
+------------------------------------------------------------------+
8 rows selected (7.862 seconds)

0: jdbc:hive2://192.168.1.70:10000/> select title from titles where price between 5 and 10;
+--------------------------------------+
|                title                 |
+--------------------------------------+
| Life Without Fear                    |
| Emotional Security: A New Algorithm  |
+--------------------------------------+
2 rows selected (1.413 seconds)


0: jdbc:hive2://192.168.1.70:10000/>  select title,price from titles where price='null' ;
+-------------------------------------+--------+
|                title                | price  |
+-------------------------------------+--------+
| The Psychology of Computer Cooking  | null   |
| Net Etiquette                       | null   |
+-------------------------------------+--------+
2 rows selected (1.162 seconds)

```

##AND操作符

MYSQL:

```
mysql> select title,title_id,price,pubdate from titles
    -> where title_id='BU1032' and price>10;
+-------------------------------------+----------+---------+---------------------+
| title                               | title_id | price   | pubdate             |
+-------------------------------------+----------+---------+---------------------+
| The Busy Executive's Database Guide | BU1032   | 19.9900 | 1991-06-12 00:00:00 |
+-------------------------------------+----------+---------+---------------------+
1 row in set (0.00 sec)

```


Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price,pubdate from titles
. . . . . . . . . . . . . . . . . .> where title_id='BU1032' and price>10;
+--------------------------------------+-----------+----------+------------------------+
|                title                 | title_id  |  price   |        pubdate         |
+--------------------------------------+-----------+----------+------------------------+
| The Busy Executive's Database Guide  | BU1032    | 19.9900  | 1991-06-12 00:00:00.0  |
+--------------------------------------+-----------+----------+------------------------+
1 row selected (1.153 seconds)

```

##OR操作符

MYSQL:

```
mysql> select title,title_id,price from titles where title_id='BU1032' or price>10;
+-----------------------------------------------------------------+----------+---------+
| title                                                           | title_id | price   |
+-----------------------------------------------------------------+----------+---------+
| The Busy Executive's Database Guide                             | BU1032   | 19.9900 |
| Cooking with Computers: Surreptitious Balance Sheets            | BU1111   | 11.9500 |
| Straight Talk About Computers                                   | BU7832   | 19.9900 |
| Silicon Valley Gastronomic Treats                               | MC2222   | 19.9900 |
| But Is It User Friendly?                                        | PC1035   | 22.9500 |
| Secrets of Silicon Valley                                       | PC8888   | 20.0000 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations | PS1372   | 21.5900 |
| Is Anger the Enemy?                                             | PS2091   | 10.9500 |
| Prolonged Data Deprivation: Four Case Studies                   | PS3333   | 19.9900 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean | TC3218   | 20.9500 |
| Fifty Years in Buckingham Palace Kitchens                       | TC4203   | 11.9500 |
| Sushi, Anyone?                                                  | TC7777   | 14.9900 |
+-----------------------------------------------------------------+----------+---------+
12 rows in set (0.01 sec)

```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price from titles where title_id='BU1032' or price>10;
+------------------------------------------------------------------+-----------+----------+
|                              title                               | title_id  |  price   |
+------------------------------------------------------------------+-----------+----------+
| The Busy Executive's Database Guide                              | BU1032    | 19.9900  |
| Cooking with Computers: Surreptitious Balance Sheets             | BU1111    | 11.9500  |
| Straight Talk About Computers                                    | BU7832    | 19.9900  |
| Silicon Valley Gastronomic Treats                                | MC2222    | 19.9900  |
| But Is It User Friendly?                                         | PC1035    | 22.9500  |
| Secrets of Silicon Valley                                        | PC8888    | 20.0000  |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  | PS1372    | 21.5900  |
| Is Anger the Enemy?                                              | PS2091    | 10.9500  |
| Prolonged Data Deprivation: Four Case Studies                    | PS3333    | 19.9900  |
| Fifty Years in Buckingham Palace Kitchens                        | TC4203    | 11.9500  |
| Sushi                                                            | TC7777    | 0877     |
+------------------------------------------------------------------+-----------+----------+
11 rows selected (1.023 seconds)

```

##IN操作符

Mysql:

```
mysql> select title,title_id,price from titles where title_id in ('BU1032','BU1111');
+------------------------------------------------------+----------+---------+
| title                                                | title_id | price   |
+------------------------------------------------------+----------+---------+
| The Busy Executive's Database Guide                  | BU1032   | 19.9900 |
| Cooking with Computers: Surreptitious Balance Sheets | BU1111   | 11.9500 |
+------------------------------------------------------+----------+---------+
2 rows in set (0.00 sec)

mysql> select title,title_id,price from titles where title_id not in ('BU1032','BU1111');
+-----------------------------------------------------------------+----------+---------+
| title                                                           | title_id | price   |
+-----------------------------------------------------------------+----------+---------+
| You Can Combat Computer Stress!                                 | BU2075   |  2.9900 |
| Straight Talk About Computers                                   | BU7832   | 19.9900 |
| Silicon Valley Gastronomic Treats                               | MC2222   | 19.9900 |
| The Gourmet Microwave                                           | MC3021   |  2.9900 |
| The Psychology of Computer Cooking                              | MC3026   |    NULL |
| But Is It User Friendly?                                        | PC1035   | 22.9500 |
| Secrets of Silicon Valley                                       | PC8888   | 20.0000 |
| Net Etiquette                                                   | PC9999   |    NULL |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations | PS1372   | 21.5900 |
| Is Anger the Enemy?                                             | PS2091   | 10.9500 |
| Life Without Fear                                               | PS2106   |  7.0000 |
| Prolonged Data Deprivation: Four Case Studies                   | PS3333   | 19.9900 |
| Emotional Security: A New Algorithm                             | PS7777   |  7.9900 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean | TC3218   | 20.9500 |
| Fifty Years in Buckingham Palace Kitchens                       | TC4203   | 11.9500 |
| Sushi, Anyone?                                                  | TC7777   | 14.9900 |
+-----------------------------------------------------------------+----------+---------+
16 rows in set (0.00 sec)

```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price from titles where title_id in ('BU1032','BU1111');
+-------------------------------------------------------+-----------+----------+
|                         title                         | title_id  |  price   |
+-------------------------------------------------------+-----------+----------+
| The Busy Executive's Database Guide                   | BU1032    | 19.9900  |
| Cooking with Computers: Surreptitious Balance Sheets  | BU1111    | 11.9500  |
+-------------------------------------------------------+-----------+----------+
2 rows selected (1.017 seconds)

0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price from titles where title_id not in ('BU1032','BU1111');
+------------------------------------------------------------------+-----------+---------------+
|                              title                               | title_id  |     price     |
+------------------------------------------------------------------+-----------+---------------+
| You Can Combat Computer Stress!                                  | BU2075    | 2.9900        |
| Straight Talk About Computers                                    | BU7832    | 19.9900       |
| Silicon Valley Gastronomic Treats                                | MC2222    | 19.9900       |
| The Gourmet Microwave                                            | MC3021    | 2.9900        |
| The Psychology of Computer Cooking                               | MC3026    | null          |
| But Is It User Friendly?                                         | PC1035    | 22.9500       |
| Secrets of Silicon Valley                                        | PC8888    | 20.0000       |
| Net Etiquette                                                    | PC9999    | null          |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  | PS1372    | 21.5900       |
| Is Anger the Enemy?                                              | PS2091    | 10.9500       |
| Life Without Fear                                                | PS2106    | 7.0000        |
| Prolonged Data Deprivation: Four Case Studies                    | PS3333    | 19.9900       |
| Emotional Security: A New Algorithm                              | PS7777    | 7.9900        |
| Onions                                                           | TC3218    | trad_cook     |
| Fifty Years in Buckingham Palace Kitchens                        | TC4203    | 11.9500       |
| Sushi                                                            | TC7777    | 0877          |
+------------------------------------------------------------------+-----------+---------------+
16 rows selected (1.121 seconds)

```

##Like通配符

Mysql:

```
mysql> select title,title_id,price from titles where title like 'You %';
+---------------------------------+----------+--------+
| title                           | title_id | price  |
+---------------------------------+----------+--------+
| You Can Combat Computer Stress! | BU2075   | 2.9900 |
+---------------------------------+----------+--------+
1 row in set (0.01 sec)

mysql> select title,title_id,price from titles where title like 'I_ %';
+---------------------+----------+---------+
| title               | title_id | price   |
+---------------------+----------+---------+
| Is Anger the Enemy? | PS2091   | 10.9500 |
+---------------------+----------+---------+
1 row in set (0.00 sec)



```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price from titles where title like 'You %';
+----------------------------------+-----------+---------+
|              title               | title_id  |  price  |
+----------------------------------+-----------+---------+
| You Can Combat Computer Stress!  | BU2075    | 2.9900  |
+----------------------------------+-----------+---------+
1 row selected (1.057 seconds)

0: jdbc:hive2://192.168.1.70:10000/> select title,title_id,price from titles where title like 'I_ %';
+----------------------+-----------+----------+
|        title         | title_id  |  price   |
+----------------------+-----------+----------+
| Is Anger the Enemy?  | PS2091    | 10.9500  |
+----------------------+-----------+----------+
1 row selected (1.472 seconds)

```

##使用数据处理函数

>函数没有SQL的可移值性强,能运行在多个系统上的代码称为可移值的,相对来说,多数SQL语句是可移植的,在SQL实现上有一定的差异,这些差异通常不那么难处理.而函数的可移值性却不强.几乎每种主要的DBMS的实现都支持其他实现不支持的函数,而且有时差异还很大.
>
>为了代码的可移植性,请保证做好代码的注释,以便以后你能确切地知道所编写SQL代码的含义.


MYSQL:

```
mysql> select upper(title) from titles;
+-----------------------------------------------------------------+
| upper(title)                                                    |
+-----------------------------------------------------------------+
| BUT IS IT USER FRIENDLY?                                        |
| COMPUTER PHOBIC AND NON-PHOBIC INDIVIDUALS: BEHAVIOR VARIATIONS |
| COOKING WITH COMPUTERS: SURREPTITIOUS BALANCE SHEETS            |
| EMOTIONAL SECURITY: A NEW ALGORITHM                             |
| FIFTY YEARS IN BUCKINGHAM PALACE KITCHENS                       |
| IS ANGER THE ENEMY?                                             |
| LIFE WITHOUT FEAR                                               |
| NET ETIQUETTE                                                   |
| ONIONS, LEEKS, AND GARLIC: COOKING SECRETS OF THE MEDITERRANEAN |
| PROLONGED DATA DEPRIVATION: FOUR CASE STUDIES                   |
| SECRETS OF SILICON VALLEY                                       |
| SILICON VALLEY GASTRONOMIC TREATS                               |
| STRAIGHT TALK ABOUT COMPUTERS                                   |
| SUSHI, ANYONE?                                                  |
| THE BUSY EXECUTIVE'S DATABASE GUIDE                             |
| THE GOURMET MICROWAVE                                           |
| THE PSYCHOLOGY OF COMPUTER COOKING                              |
| YOU CAN COMBAT COMPUTER STRESS!                                 |
+-----------------------------------------------------------------+
18 rows in set (0.00 sec)


```

Inceptor:

见TDH官方手册上的函数支持


##聚集函数

MYSQL:

```
mysql> select avg(price) from titles;
+-------------+
| avg(price)  |
+-------------+
| 14.76625000 |
+-------------+
1 row in set (0.00 sec)

mysql> select count(price) from titles;
+--------------+
| count(price) |
+--------------+
|           16 |
+--------------+
1 row in set (0.00 sec)


mysql> select max(price) from titles;
+------------+
| max(price) |
+------------+
|    22.9500 |
+------------+
1 row in set (0.00 sec)

mysql> select min(price) from titles;
+------------+
| min(price) |
+------------+
|     2.9900 |
+------------+
1 row in set (0.00 sec)

mysql> select sum(price) from titles;
+------------+
| sum(price) |
+------------+
|   236.2600 |
+------------+
1 row in set (0.00 sec)

mysql> select avg( distinct price) from titles;
+----------------------+
| avg( distinct price) |
+----------------------+
|          14.66818182 |
+----------------------+
1 row in set (0.03 sec)

mysql> select count(*) as title_items,
    -> min(price) as price_min,
    -> max(price) as price_max,
    -> avg(price) as price_avg
    -> from titles;
+-------------+-----------+-----------+-------------+
| title_items | price_min | price_max | price_avg   |
+-------------+-----------+-----------+-------------+
|          18 |    2.9900 |   22.9500 | 14.76625000 |
+-------------+-----------+-----------+-------------+
1 row in set (0.00 sec)


```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select avg(price) as price_avg from titles;
+--------------------+
|     price_avg      |
+--------------------+
| 71.82133333333334  |
+--------------------+
1 row selected (1.033 seconds)

0: jdbc:hive2://192.168.1.70:10000/> select count(price) from titles;
+------+
| _c0  |
+------+
| 18   |
+------+
1 row selected (1.339 seconds)

0: jdbc:hive2://192.168.1.70:10000/> select avg( distinct price) from titles;
+---------------------+
|         _c0         |
+---------------------+
| 100.24100000000001  |
+---------------------+

0: jdbc:hive2://192.168.1.70:10000/> select count(*) as title_items,min(price) as price_min,max(price) as price_max,avg(price) as price_avg from titles;
+--------------+------------+---------------+--------------------+
| title_items  | price_min  |   price_max   |     price_avg      |
+--------------+------------+---------------+--------------------+
| 18           | 0877       | trad_cook     | 71.82133333333334  |
+--------------+------------+---------------+--------------------+
1 row selected (0.899 seconds)

```

##数据分组

Mysql:

```
mysql> select type from titles group by  type;
+--------------+
| type         |
+--------------+
| business     |
| mod_cook     |
| popular_comp |
| psychology   |
| trad_cook    |
| UNDECIDED    |
+--------------+
6 rows in set (0.01 sec)


mysql> select type,avg(price)as book_avg_price from titles group by type having book_avg_price>10;
+--------------+----------------+
| type         | book_avg_price |
+--------------+----------------+
| business     |    13.73000000 |
| mod_cook     |    11.49000000 |
| popular_comp |    21.47500000 |
| psychology   |    13.50400000 |
| trad_cook    |    15.96333333 |
+--------------+----------------+
5 rows in set (0.00 sec)

mysql> select title_id,sum(qty) as book_sum_qty from sales where title_id in (select title_id from titles where title='Sushi, Anyone?');
+----------+--------------+
| title_id | book_sum_qty |
+----------+--------------+
| TC7777   |           20 |
+----------+--------------+
1 row in set (0.00 sec)


```


Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select type from titles group by  type;
+---------------+
|     type      |
+---------------+
| psychology    |
| popular_comp  |
| trad_cook     |
| business      |
|  Leeks        |
| UNDECIDED     |
| mod_cook      |
|  Anyone?      |
+---------------+

0: jdbc:hive2://192.168.1.70:10000/> select type,avg(price)as book_avg_price from titles group by type having book_avg_price>10;
+---------------+---------------------+
|     type      |   book_avg_price    |
+---------------+---------------------+
| psychology    | 13.504              |
| popular_comp  | 21.475              |
| trad_cook     | 11.95               |
| business      | 13.73               |
| mod_cook      | 11.489999999999998  |
|  Anyone?      | 877.0               |
+---------------+---------------------+
6 rows selected (2.829 seconds)

select s.title_id,sum(s.qty) as book_sum_qty from sales as s where s.title_id in (select t.title_id from titles t where t.title='Sushi, Anyone?') group by s.title_id;
+-----------+---------------+
| title_id  | book_sum_qty  |
+-----------+---------------+
+-----------+---------------+
No rows selected (2.285 seconds)

```

##表连接

MYSQL:

```
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


mysql> select title,au_ord from titles inner join titleauthor on titles.title_id=titleauthor.title_id;
+-----------------------------------------------------------------+--------+
| title                                                           | au_ord |
+-----------------------------------------------------------------+--------+
| But Is It User Friendly?                                        |      1 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      2 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      1 |
| Cooking with Computers: Surreptitious Balance Sheets            |      2 |
| Cooking with Computers: Surreptitious Balance Sheets            |      1 |
| Emotional Security: A New Algorithm                             |      1 |
| Fifty Years in Buckingham Palace Kitchens                       |      1 |
| Is Anger the Enemy?                                             |      2 |
| Is Anger the Enemy?                                             |      1 |
| Life Without Fear                                               |      1 |
| Net Etiquette                                                   |      1 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean |      1 |
| Prolonged Data Deprivation: Four Case Studies                   |      1 |
| Secrets of Silicon Valley                                       |      1 |
| Secrets of Silicon Valley                                       |      2 |
| Silicon Valley Gastronomic Treats                               |      1 |
| Straight Talk About Computers                                   |      1 |
| Sushi, Anyone?                                                  |      2 |
| Sushi, Anyone?                                                  |      3 |
| Sushi, Anyone?                                                  |      1 |
| The Busy Executive's Database Guide                             |      2 |
| The Busy Executive's Database Guide                             |      1 |
| The Gourmet Microwave                                           |      1 |
| The Gourmet Microwave                                           |      2 |
| You Can Combat Computer Stress!                                 |      1 |
+-----------------------------------------------------------------+--------+
25 rows in set (0.00 sec)

mysql> select title,au_ord from titles inner join titleauthor on titles.title_id=titleauthor.title_id  inner join authors on authors.au_id=titleauthor.au_id;
+-----------------------------------------------------------------+--------+
| title                                                           | au_ord |
+-----------------------------------------------------------------+--------+
| But Is It User Friendly?                                        |      1 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      2 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      1 |
| Cooking with Computers: Surreptitious Balance Sheets            |      2 |
| Cooking with Computers: Surreptitious Balance Sheets            |      1 |
| Emotional Security: A New Algorithm                             |      1 |
| Fifty Years in Buckingham Palace Kitchens                       |      1 |
| Is Anger the Enemy?                                             |      2 |
| Is Anger the Enemy?                                             |      1 |
| Life Without Fear                                               |      1 |
| Net Etiquette                                                   |      1 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean |      1 |
| Prolonged Data Deprivation: Four Case Studies                   |      1 |
| Secrets of Silicon Valley                                       |      1 |
| Secrets of Silicon Valley                                       |      2 |
| Silicon Valley Gastronomic Treats                               |      1 |
| Straight Talk About Computers                                   |      1 |
| Sushi, Anyone?                                                  |      2 |
| Sushi, Anyone?                                                  |      3 |
| Sushi, Anyone?                                                  |      1 |
| The Busy Executive's Database Guide                             |      2 |
| The Busy Executive's Database Guide                             |      1 |
| The Gourmet Microwave                                           |      1 |
| The Gourmet Microwave                                           |      2 |
| You Can Combat Computer Stress!                                 |      1 |
+-----------------------------------------------------------------+--------+
25 rows in set (0.00 sec)

mysql> select title,au_ord from titles left join titleauthor on titles.title_id=titleauthor.title_id;
+-----------------------------------------------------------------+--------+
| title                                                           | au_ord |
+-----------------------------------------------------------------+--------+
| But Is It User Friendly?                                        |      1 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      2 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      1 |
| Cooking with Computers: Surreptitious Balance Sheets            |      2 |
| Cooking with Computers: Surreptitious Balance Sheets            |      1 |
| Emotional Security: A New Algorithm                             |      1 |
| Fifty Years in Buckingham Palace Kitchens                       |      1 |
| Is Anger the Enemy?                                             |      2 |
| Is Anger the Enemy?                                             |      1 |
| Life Without Fear                                               |      1 |
| Net Etiquette                                                   |      1 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean |      1 |
| Prolonged Data Deprivation: Four Case Studies                   |      1 |
| Secrets of Silicon Valley                                       |      1 |
| Secrets of Silicon Valley                                       |      2 |
| Silicon Valley Gastronomic Treats                               |      1 |
| Straight Talk About Computers                                   |      1 |
| Sushi, Anyone?                                                  |      2 |
| Sushi, Anyone?                                                  |      3 |
| Sushi, Anyone?                                                  |      1 |
| The Busy Executive's Database Guide                             |      2 |
| The Busy Executive's Database Guide                             |      1 |
| The Gourmet Microwave                                           |      1 |
| The Gourmet Microwave                                           |      2 |
| The Psychology of Computer Cooking                              |   NULL |
| You Can Combat Computer Stress!                                 |      1 |
+-----------------------------------------------------------------+--------+
26 rows in set (0.00 sec)

mysql> select title,au_ord from titles right join titleauthor on titles.title_id=titleauthor.title_id;
+-----------------------------------------------------------------+--------+
| title                                                           | au_ord |
+-----------------------------------------------------------------+--------+
| Prolonged Data Deprivation: Four Case Studies                   |      1 |
| The Busy Executive's Database Guide                             |      2 |
| You Can Combat Computer Stress!                                 |      1 |
| But Is It User Friendly?                                        |      1 |
| Cooking with Computers: Surreptitious Balance Sheets            |      2 |
| Sushi, Anyone?                                                  |      2 |
| Straight Talk About Computers                                   |      1 |
| The Busy Executive's Database Guide                             |      1 |
| Secrets of Silicon Valley                                       |      1 |
| Sushi, Anyone?                                                  |      3 |
| Net Etiquette                                                   |      1 |
| Emotional Security: A New Algorithm                             |      1 |
| Fifty Years in Buckingham Palace Kitchens                       |      1 |
| Sushi, Anyone?                                                  |      1 |
| Silicon Valley Gastronomic Treats                               |      1 |
| The Gourmet Microwave                                           |      1 |
| Cooking with Computers: Surreptitious Balance Sheets            |      1 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      2 |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations |      1 |
| Onions, Leeks, and Garlic: Cooking Secrets of the Mediterranean |      1 |
| Secrets of Silicon Valley                                       |      2 |
| The Gourmet Microwave                                           |      2 |
| Is Anger the Enemy?                                             |      2 |
| Is Anger the Enemy?                                             |      1 |
| Life Without Fear                                               |      1 |
+-----------------------------------------------------------------+--------+
25 rows in set (0.00 sec)


```

Inceptor:

```
0: jdbc:hive2://192.168.1.70:10000/> select title,au_ord from titles inner join titleauthor on titles.title_id=titleauthor.title_id;
+------------------------------------------------------------------+---------+
|                              title                               | au_ord  |
+------------------------------------------------------------------+---------+
| The Busy Executive's Database Guide                              | 2       |
| The Busy Executive's Database Guide                              | 1       |
| Cooking with Computers: Surreptitious Balance Sheets             | 2       |
| Cooking with Computers: Surreptitious Balance Sheets             | 1       |
| You Can Combat Computer Stress!                                  | 1       |
| Straight Talk About Computers                                    | 1       |
| Silicon Valley Gastronomic Treats                                | 1       |
| The Gourmet Microwave                                            | 1       |
| The Gourmet Microwave                                            | 2       |
| But Is It User Friendly?                                         | 1       |
| Secrets of Silicon Valley                                        | 1       |
| Secrets of Silicon Valley                                        | 2       |
| Net Etiquette                                                    | 1       |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  | 2       |
| Computer Phobic AND Non-Phobic Individuals: Behavior Variations  | 1       |
| Is Anger the Enemy?                                              | 2       |
| Is Anger the Enemy?                                              | 1       |
| Life Without Fear                                                | 1       |
| Prolonged Data Deprivation: Four Case Studies                    | 1       |
| Emotional Security: A New Algorithm                              | 1       |
| Onions                                                           | 1       |
| Fifty Years in Buckingham Palace Kitchens                        | 1       |
| Sushi                                                            | 2       |
| Sushi                                                            | 3       |
| Sushi                                                            | 1       |
+------------------------------------------------------------------+---------+
25 rows selected (1.858 seconds)

```