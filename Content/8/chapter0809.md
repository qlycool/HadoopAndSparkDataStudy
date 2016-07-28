# Hive的开发
## 一 Hive的Thrift服务
Hive具有一个可选的组件叫做HiveServer或者HiveThrift,其允许通过指定的端口访问Hive,Thrift是一个软件框架,其用于跨语言的服务开发.关于Thrift,可以通过链接http://thrift.apache.org获取更详细的介绍.Thrift允许客户端使用包括Java C++ Ruby和其他语言,通过编程的方式远程访问Hive.

访问Hive的最常用的方式就是通过CLI进行访问,不过CLI的设计使其不便于通过编程的方式进行访问.CLI是胖客户端,其需要本地具有所有的Hive组件,包括配置,同时还需要一个Hadoop客户端及其配置.同时,其可作为HDFS客户端,Mapreduce客户端以及JDBC客户端进行使用.

### 1 启动Thrift Server
如果想启动Hiveserver,可以在后台启动执行这个Hive服务:

```
hadoop@hadoopmaster:~$ hiveserver2 start &
```

检查HiveServer是否启动成功的最快捷方法就是使用netstat 命令查看10000端口是否打开并监听连接:

```
$netstat -nl|grep 10000
```

正如前面所提到过的,HiveServer使用Thrift提供服务,Thrift提供了一个接口语言.通过这些接口,Thrift编译器可以产生创建网络RPC的多种语言的客户端的代码.



## 二 一个简单的链接例子

###1 Maven地址

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.hithinksoft.com</groupId>
    <artifactId>chu888chu888</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
    <!-- https://mvnrepository.com/artifact/org.apache.hive/hive-jdbc -->
    <dependency>
        <groupId>org.apache.hive</groupId>
        <artifactId>hive-jdbc</artifactId>
        <version>2.1.0</version>
    </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>jboss</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        </repository>
    </repositories>

</project>

```
**例子**

```
import java.sql.*;

/**
 * Created by chuguangming on 16/7/26.
 */
public class testHive {
    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        try{
            Connection con = DriverManager.getConnection("jdbc:hive2://hadoopmaster:10000/default","hive","hive");
            PreparedStatement sta = con.prepareStatement("select * from u_data_partitioned_table");
            ResultSet result = sta.executeQuery();
            while(result.next()){
                System.out.println(result.getString(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}

```
