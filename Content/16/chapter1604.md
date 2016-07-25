# 4 Sqoop的使用

##sqoop操作步骤：
1. 在Inceptor metastore节点服务器上安装sqoop服务
yum install sqoop

2. 由于Inceptor-SQL中metastore中已经安装了mysql，就不需要安装mysql了

3. 将mysql-connector-java-5.1.38tar.gz驱动包先解压
```
tar -zxvf mysql-connector-java-5.1.38tar.gz
```
4. cd进刚刚解压后的目录，将里面的mysql-connector-java-5.1.38-bin.jar包copy到/usr/lib/sqoop/lib本地目录下

5. 在Inceptor Server节点上输入mysql -u [用户名] -p连接数据库,此时提示输入密码，通过后输入mysql命令，再执行Grant操作：

    ----add user to mysql（username＝tdh，password＝123456），授权可以访问所有数据库
```
GRANT ALL PRIVILEGES ON *.* TO 'tdh'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
```
（若仅授权db1数据库里所有表，则可以这样指定)：
```
GRANT ALL PRIVILEGES ON db1.* TO 'tdh'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;）
```
6. 浏览mysql数据库
```
sqoop list-databases \
--username tdh \
--password 123456 \
--connect jdbc:mysql://<mysql IP>:3306/
```
7. 浏览SQL SERVER数据库
```
浏览SQL SERVER
sqoop list-databases --connect "jdbc:sqlserver://192.168.1.139:1433;username=sa;password=123456"
```
8. 浏览mysql数据库中的表，db1为mysql中的一个数据库，可以使用describe [table名]命令查看mysql中表的信息
```
sqoop list-tables \
--username tdh \
--password 123456 \
--connect jdbc:mysql://<mysql IP>:3306/db1
```
9. 浏览SQL SERVER中的表并且导入到HDFS中

```
sqoop list-databases --connect "jdbc:sqlserver://192.168.1.139:1433;username=sa;password=123456"

sqoop list-tables --connect "jdbc:sqlserver://192.168.1.139:1433;username=sa;password=123456;database=pubs"

sqoop import --connect "jdbc:sqlserver://192.168.1.139:1433;username=sa;password=123456;database=pubs" --table sales --target-dir /user/lm/sqoop/data/sales -m 1
```

8. 从mysql————>HDFS上（import，将mysql中的db1数据库里面的表导入到/user/datadir，这里的datadir目录一定不要事先创建，不然会报错，语句执行的时候会自动创建目录的！最后一行的－m表示map成4个文件）
```
sqoop import \
--username tdh \
--password 123456 \
--connect jdbc:mysql://<mysql IP>:3306/db1 \
--table country \
--target-dir /user/user1/data/sqoop -m 4
```

9. 从HDFS————>mysql表上（export）
```
sqoop export \
--username tdh \
--password 123456 \
--connect jdbc:mysql://<mysql IP>:3306/db1 \
--table cc \
--export-dir /user/testdir \
--staging-table tmptable
```

10. 查看sqoop导入进来的文件

```
hadoop fs -cat /user/user1/data/inceptor/part-m-00000 | more
```
![](images/16/lALOCzkaWc0BgM0DEQ_785_384.png)

![](images/16/lALOCzkabM0B1s0DIw_803_470.png)

##注意事项
- 在执行导入导出数据时，可能由于yarn资源不足或者其他进程的占用，而一直停留在job作业等待处理中，
此时可以通过浏览器进入YARN中Resource Manager节点中的8088端口查看被占用的Application ID号，里面描述为Application master为常驻进程，不用
杀掉，再在shell中输入命令
```yarn -application -kill <Application ID>```
来杀死卡掉的进程，再运行上面的import、export语句。

原因很简单，Inceptor-sql的常驻进程ApplicationMaster跑的是spark任务，非常消耗内存使用量，约为7-8G，所以在没有用到Inceptor-SQL的操作场景的时候就应该关闭该服务。

