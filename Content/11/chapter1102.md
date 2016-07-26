# 2.HBASE常用的Shell命令

## 表的管理
####1 查看有哪些表

```hbase(main)> list```

####2 创建表

```
语法：create <table>, {NAME => <family>, VERSIONS => <VERSIONS>}
例如：创建表t1，有两个family name：f1，f2，且版本数均为2

hbase(main)> create 't1',{NAME => 'f1', VERSIONS => 2},{NAME => 'f2', VERSIONS => 2}

```
**创建表t1,列族为f1,列族版本号为5,命令如下**
```
hbase(main):002:0> create 't1',{NAME=>'f1',VERSIONS=>5}
```
**或者使用如下等价的命令**

```
hbase(main):003:0> create 't2','f1','f2','f3'
```

**创建表t1,将表依据分割算法HexStringSplit分布在15个Region里,命令如下:**

```
hbase(main):006:0* create 't3','f1',{NUMREGIONS=>15,SPLITALGO=>'HexStringSplit'}
```

**创建表t1,指定切分点,命令如下:**

```
hbase(main):007:0> create 't4','f1',{SPLITS=>['10','20','30','40']}
```
**put:向表/行/列指定的单元格添加数据**

**向表t1中行row1,列f1:1,添加数据value1,时间戳为14222222222,命令如下:**
```
hbase(main):009:0> put 't1','row1','f1:1','value1',55555434344
```

####3 删除表

分两步：首先disable，然后drop,例如：删除表t1

```
hbase(main)> disable 't1'
hbase(main)> drop 't1'
```


####4 查看表的结构

```
语法：describe <table>
例如：查看表t1的结构
hbase(main)> describe 't1'
```


####5 修改表结构
```
修改表结构必须先disable
语法：alter 't1', {NAME => 'f1'}, {NAME => 'f2', METHOD => 'delete'}
例如：修改表test1的cf的TTL为180天
hbase(main)> disable 'test1'
hbase(main)> alter 'test1',{NAME=>'body',TTL=>'15552000'},{NAME=>'meta', TTL=>'15552000'}
hbase(main)> enable 'test1'

```

## 权限管理
####1 分配权限

```
语法 : grant <user> <permissions> <table> <column family> <column qualifier> 
参数后面用逗号分隔权限用五个字母表示： "RWXCA".
READ('R'), WRITE('W'), EXEC('X'), CREATE('C'), ADMIN('A')
例如，给用户‘test'分配对表t1有读写的权限，
hbase(main)> grant 'test','RW','t1'
```


####2 查看权限
```
语法：user_permission <table>
例如，查看表t1的权限列表
hbase(main)> user_permission 't1'
```


####3 收回权限
```
与分配权限类似，语法：revoke <user> <table> <column family> <column qualifier>
例如，收回test用户在表t1上的权限
hbase(main)> revoke 'test','t1'
```



## 表数据的增删改查

####1 添加数据
```
语法：put <table>,<rowkey>,<family:column>,<value>,<timestamp>
例如：给表t1的添加一行记录：rowkey是rowkey001，family name：f1，column name：col1，value：value01，timestamp：系统默认
hbase(main)> put 't1','rowkey001','f1:col1','value01'
用法比较单一。
```

####2 查询某行记录

```
语法：get <table>,<rowkey>,[<family:column>,....]
例如：查询表t1，rowkey001中的f1下的col1的值
hbase(main)> get 't1','rowkey001', 'f1:col1'
或者：
hbase(main)> get 't1','rowkey001', {COLUMN=>'f1:col1'}
查询表t1，rowke002中的f1下的所有列值
hbase(main)> get 't1','rowkey001'
```


####3 扫描表
```
语法：scan <table>, {COLUMNS => [ <family:column>,.... ], LIMIT => num}
另外，还可以添加STARTROW、TIMERANGE和FITLER等高级功能
例如：扫描表t1的前5条数据
hbase(main)> scan 't1',{LIMIT=>5}
```

####4 查询表中的数据行数
```
语法：count <table>, {INTERVAL => intervalNum, CACHE => cacheNum}
INTERVAL设置多少行显示一次及对应的rowkey，默认1000；CACHE每次去取的缓存区大小，默认是10，调整该参数可提高查询速度
例如，查询表t1中的行数，每100条显示一次，缓存区为500
hbase(main)> count 't1', {INTERVAL => 100, CACHE => 500}
```


## 删除数据

####1 删除行中的某个列值

```
语法：delete <table>, <rowkey>,  <family:column> , <timestamp>,必须指定列名
例如：删除表t1，rowkey001中的f1:col1的数据
hbase(main)> delete 't1','rowkey001','f1:col1'
注：将删除改行f1:col1列所有版本的数据
```


####2 删除行
```
语法：deleteall <table>, <rowkey>,  <family:column> , <timestamp>，可以不指定列名，删除整行数据
例如：删除表t1，rowk001的数据
hbase(main)> deleteall 't1','rowkey001'
```


###3 删除表中的所有数据
```
语法： truncate <table>
其具体过程是：disable table -> drop table -> create table
例如：删除表t1的所有数据
hbase(main)> truncate 't1'

```

## Region管理

####1 移动region

```
语法：move 'encodeRegionName', 'ServerName'
encodeRegionName指的regioName后面的编码，ServerName指的是master-status的Region Servers列表
示例
hbase(main)>move '4343995a58be8e5bbc739af1e91cd72d', 'db-41.xxx.xxx.org,60020,1390274516739'
```


####2 开启/关闭region

```
语法：balance_switch true|false
hbase(main)> balance_switch
```


####3 手动split

语法：split 'regionName', 'splitKey'

####4 手动触发major compaction
```
语法：
Compact all regions in a table:
hbase> major_compact 't1'
Compact an entire region:
hbase> major_compact 'r1'
Compact a single column family within a region:
hbase> major_compact 'r1', 'c1'
Compact a single column family within a table:
hbase> major_compact 't1', 'c1'
```