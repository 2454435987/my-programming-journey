package db;

import java.sql.*;

//负责数据库连接和断开释放资源
public class DBHelper {
    private static final String DRVERNAME = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/registered?useUnicode=true&characterEncoding=UTF-8";

    //加载驱动
    static {
        try {
            Class.forName(DRVERNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取数据库连接并返回给调用者
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, "root", "123456");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //释放资源
    public static void close(ResultSet rst,PreparedStatement pstmt,Connection conn) {
        try {
            if(rst!=null)
                rst.close();
            if(pstmt!=null)
                pstmt.close();
            if(conn!=null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

