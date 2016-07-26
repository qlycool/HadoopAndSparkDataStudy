# Hive的开发
## 一 IDEA环境准备

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
