# Hive综合案例实战

## 一 创建数据表,导入数据
首先我们去一个网站下载相关的数据,之后通过hive导入进行实验.[http://grouplens.org/](http://grouplens.org/)

![](../../images/10/chapter10001.png)

```
hive> create table u_data (userid INT, movieid INT, rating INT, unixtime STRING) row format delimited fields terminated by '\t' lines terminated by '\n';

hive> LOAD DATA LOCAL INPATH '/home/hadoop/u.data' OVERWRITE INTO TABLE u_data;

CSV的导入有点不同

hive> create table ratings (userid INT, movieid INT, rating INT, timestamp STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

hive> LOAD DATA LOCAL INPATH '/home/hadoop/ratings.csv' OVERWRITE INTO TABLE ratings;

hive> select * from ratings limit 10;
OK
NULL	NULL	NULL	timestamp
1	169	2	1204927694
1	2471	3	1204927438
1	48516	5	1204927435
2	2571	3	1436165433
2	109487	4	1436165496
2	112552	5	1436165496
2	112556	4	1436165499
3	356	4	920587155
3	2394	4	920586920
Time taken: 0.143 seconds, Fetched: 10 row(s)

hive> desc ratings;
OK
userid              	int                 	                    
movieid             	int                 	                    
rating              	int                 	                    
timestamp           	string              	                    
Time taken: 0.163 seconds, Fetched: 4 row(s)


```
##没有想好,想一想继续,反正数据量是有了2000W行了