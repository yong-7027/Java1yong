package movie_management;

import Connect.DatabaseUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/*public class jdbcDemo {
    public static void main(String[] args) throws Exception {
        // 注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DatabaseUtils.getConnection();
        // 定义
        String sql = "select * from movie";

        // 获取执行sql的对象 Statement
        Statement stmt = conn.createStatement();

        // 执行SQL
        ResultSet result = stmt.executeQuery(sql);

        // 处理结果
        while (result.next()) {
            int id = result.getInt("movie_id");
            String name = result.getString("mv_name");
            // 在这里处理每一行数据
            System.out.println("ID: " + id + ", Title: " + name);
        }

        // 释放资源
        result.close();
        stmt.close();
        conn.close();
    }
}*/
