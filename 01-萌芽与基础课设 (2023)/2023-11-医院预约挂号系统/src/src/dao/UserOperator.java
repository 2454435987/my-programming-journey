package dao;

import java.io.*;
import java.sql.*;
import java.util.Base64;

import pojo.User;
import db.DBHelper;

//用户操作
public class UserOperator extends BaseDao{
	
    //用户注册，查询账户在sql中是否已存在
    public boolean checkExistence(String user) {
        String sql = "SELECT * FROM user WHERE user=?";
        ResultSet rs = query(sql, user);
        try {
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(rs, pstmt, conn);
        }
        return false;
    }
    
    //用户注册界面数据的保存
    public boolean saveUser(User us) {
        return zsg("insert into user(user, pwd, name, gender, identity, mobile) values(?,?,?,?,?,?)", us.getUser(), us.getPwd(), us.getName(), us.getGender(), us.getIdentity(), us.getMobile());
    }
    
    //用户登录，匹配数据中保存的账号密码
    public boolean authenticateUser(User us) {
        String sql = "SELECT * FROM user WHERE user=? AND pwd=?";
        ResultSet rs = query(sql, us.getUser(), us.getPwd());
        try {
            return rs != null && rs.next();//true登录成功
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(rs, pstmt, conn);
        }
        return false;//登录失败
    }

    //通过查找用户名，获取对应字段的其他数据
    public User getUserByName(String user) {
        ResultSet rs = query("SELECT * FROM user WHERE user=?", user);
        try {
            while(rs.next()) {
                User u = new User();
                u.setUser(rs.getString("user"));
                u.setPwd(rs.getString("pwd"));
                u.setName(rs.getString("name"));
                u.setGender(rs.getString("gender"));
                u.setIdentity(rs.getString("identity"));
                u.setMobile(rs.getString("mobile"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(rs, pstmt, conn);
        }
        return null;
    }
    
    //修改新密码
  	public boolean updatePassword(String user, String pwd) {
  		return zsg("UPDATE user SET pwd = ? WHERE user = ?", pwd, user);
  	}
  	
  	//注销账号(根据登录的账号名)
  	public boolean cancelAccount(String user) {
          return zsg("DELETE FROM user WHERE user = ?", user);
      }
  	
  	//更新个人信息(账号不能修改)
  	public boolean updateText(String user, String name, String gender, String identity, String mobile) {
  		return zsg("UPDATE user SET name=?, gender=?, identity=?, mobile=? WHERE user=?", name,gender,identity,mobile,user);
  	}
  	
  	//通过查找用户名，将图片保存到该用户名tx字段；
    public boolean saveImage(InputStream inputStream,String user) {
        byte[] bytes;
        try {
            bytes = readAllBytes(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }// 读取整个文件
        return zsg("UPDATE user SET userTx=? WHERE user=?", new ByteArrayInputStream(bytes), user);
    }
    
    //查找当前登录用户的头像（返回图片src）
  	public String getTxSrcByName(String user) {
  	    ResultSet rs = new BaseDao().query("SELECT userTx FROM user WHERE user=?", user);
  	    try {
  	        if (rs.next()) {
  	        	User us = new User();
  	        	us.setUserTx(rs.getBytes("userTx") == null ? new byte[0] : rs.getBytes("userTx"));
	            if (us.getUserTx() != null && us.getUserTx().length > 0) {
	                String encodedImage = Base64.getEncoder().encodeToString(us.getUserTx());
	                us.setTxSrc("data:image/jpg;base64," + encodedImage);
	            }
	            return us.getTxSrc();//返回头像的图片地址
  	        } else {
  	            return null;
  	        }
  	    } catch (SQLException e) {
  	        e.printStackTrace();
  	        return null;
  	    }
  	}
  	
    //从输入流中读取所有的字节
    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
