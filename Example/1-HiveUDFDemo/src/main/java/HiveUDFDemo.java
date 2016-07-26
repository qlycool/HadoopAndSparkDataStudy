import java.sql.*;

/**
 * Created by chuguangming on 16/7/26.
 */
public class HiveUDFDemo {
    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        try{
            Connection con = DriverManager.getConnection("jdbc:hive2://hadoopmaster:10000/default","hive","hive");
            PreparedStatement sta = con.prepareStatement("select * from t_hive");
            ResultSet result = sta.executeQuery();
            while(result.next()){
                System.out.println(result.getString(1));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
