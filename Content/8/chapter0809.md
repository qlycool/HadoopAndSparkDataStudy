# Hive的开发
## 一 IDEA环境准备

###1 Maven地址

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.hithinksoft</groupId>
    <artifactId>HiveDemo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-service</artifactId>
            <version>2.1.0</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>jboss</id>
            <url>http://repository.jboss.org/nexus/content/repositories/root_repository/maven2/</url>
        </repository>
    </repositories>
</project>
```
**例子**

```
/**
 * Created by chuguangming on 16/7/25.
 */
import java.sql.Connection;
importjava.sql.DriverManager;
import java.sql.ResultSet;
importjava.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        try{
            Connection con = DriverManager.getConnection("jdbc:hive2://hadoop:10000/default","hive","hive");
            PreparedStatement sta = con.prepareStatement("select * from t_hive");
            ResultSet result = sta.executeQuery();
            while(result.next()){
                System.out.println(result.getDate(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
```
