package shujukulianjie;
import java.sql.*;

public class Mysql {
	//连接数据库的URL路径
	static String url = "jdbc:mysql://localhost:3306/yyspxtdb?use";
	//数据库的账号
	static String user = "root";
	//数据库的密码
	static String password = "qwer"; 
	//加载驱动
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载驱动！");
		}
		catch(Exception e) {
			System.out.println("加载驱动失败！");
			e.printStackTrace();
		}
	}
	//获取数据库连接
	public static Connection getConnection() {
		Connection conn=null;
		try {
			return conn = DriverManager.getConnection(url,user,password);
		}catch(Exception e) {
			System.out.println("获取信息错误！");
			e.printStackTrace();
		}
		return conn;
	}
	//执行查询 ResultSet
		public static ResultSet query(String sql){
			ResultSet re=null;
			try {
				//1.连接数据库
				Connection conn=getConnection();
				//2.预处理sql语句
				PreparedStatement ps= conn.prepareStatement(sql);
				//3.执行sql语句 返回二维表格的结果姐
				re=ps.executeQuery();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return re;
		}
		//执行增删改语句
		public static boolean update(String sql){
			boolean flag=false;
			try {
				//1.连接数据库
				Connection conn=getConnection();
				//2.预处理sql语句
				PreparedStatement ps= conn.prepareStatement(sql);
				//3.执行sql语句 返回受影响行
				flag=ps.executeUpdate()>0; 
				//预处理器关闭
				ps.close();
				//关闭数据库连接
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
			} 
			return flag;
		}
		
}


