#Hive 数据类型和文件格式

##一 概述

Hive支持关系型数据库中的大多数基本数据类型,同时也支持关系型数据库中很少出现的3种集合数据类型.

### 1 基本数据类型

Hive支持多种不同长度的整型和浮点型数据类型,支持布尔类型,也支持无长度限制的字符串类型.

| 数据类型         | 长度           | 例子  |
| -------------  |:-------------:| -----:|
| TINYINT        | 1byte 有符号整数 | 20 |
| SMALLINT       | 2byte 有符号整数 | 20 |
| INT            | 4byte 有符号整数 | 20 |
| BIGINT         | 8byte 有符号整数 | 20 |
| BOOLEAN        | 布尔类型trueflase|TRUE|
| FLOAT          | 单精度浮点数     |3.14159 |
| DOUBLE         | 双精度浮点数     |3.14159 |
| STRING         | 字符序列        |'now is time' |
| TIMESTAMP      | 整数            |'2012-02-03 12:34:56' |
| BINARY         | 字节数组        | |

### 2 集合数据类型

Hive中列支持使用struct map array集合数据类型.

| 数据类型         | 长度           | 例子  |
| -------------  |:-------------:| -----:|
| STRUCT        | 和C语言中的struct类似 | struct('John','Doe')|
| MAP           | MAP是一组键值对集合 | map('first','join','last','doe') |
| ARRAY         | 数组是一组具有相同类型和名称的变量的集合 | Array('John','Doe') |

我们来看一个简单的集合类型的例子

```
0: jdbc:hive2://localhost:10000/default> create table employees(
. . . . . . . . . . . . . . . . . . . .> name STRING,
. . . . . . . . . . . . . . . . . . . .> salary FLOAT,
. . . . . . . . . . . . . . . . . . . .> subordinates ARRAY<STRING>,
. . . . . . . . . . . . . . . . . . . .> deductions MAP<STRING,FLOAT>,
. . . . . . . . . . . . . . . . . . . .> address STRUCT<street:STRING,city:STRING,state:STRING,zip:INT>);
OK
No rows affected (1.228 seconds)
0: jdbc:hive2://localhost:10000/default> 

```