package dao;

import java.sql.*;
import db.DBHelper;

//数据库操作的父类
public class BaseDao {
    Connection conn;//sql连接
    PreparedStatement pstmt;//java.sql包中的接口
    ResultSet rst;//结果集

    //增删改封装为一个公用的方法
    public boolean zsg(String sql,Object...objects) {
        conn = DBHelper.getConn();
        try {
            pstmt = conn.prepareStatement(sql);
            if(objects!=null) {
                for (int i = 0; i < objects.length; i++) {
                    pstmt.setObject(i+1, objects[i]);
                }
            }
            int result = pstmt.executeUpdate();//执行增删改操作
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBHelper.close(rst, pstmt, conn);
        }
        return false;
    }

    //查询封装为一个公用的方法
    public ResultSet query(String sql, Object... objects) {
        conn = DBHelper.getConn();
        try {
            pstmt = conn.prepareStatement(sql);
            if (objects != null) {
                for (int i = 0; i < objects.length; i++) {
                    pstmt.setObject(i + 1, objects[i]);
                }
            }
            rst = pstmt.executeQuery();// 执行查询
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rst; // 返回查询结果集
    }
}
