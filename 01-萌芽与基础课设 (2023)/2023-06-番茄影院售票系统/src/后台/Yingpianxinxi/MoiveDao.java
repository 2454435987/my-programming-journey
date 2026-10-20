package Yingpianxinxi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import shujukulianjie.Mysql;


public class MoiveDao {
  public static boolean insert(Moive m) {
	  String sql="insert into movie values(null,'%s',null,%.2f,'%s','%s','%s','%s',%d,%.2f,'%s','%s','%s','%s')";
	  sql=String.format(sql,m.getName(),m.getHot(),m.getDirector(),m.getActorid(),m.getStartdate(),m.getEnddate(),m.getMovieduration(),m.getFare(),m.getMoviegenre(),m.getCountry(),m.getSynopsis(),m.getScore());
	  System.out.println(sql);
	return Mysql.update(sql);
  }
  public static boolean update(Moive m) { 
//		String sql = "update employee SET empName='%s',empSex ='%s',empNumber='%s',empAge ='%d' ,strator='%s',ip='%s',class1='%s' WHERE empID =%d";
//		sql=String.format(sql,p.getempName(),p.getempSex(),p.getempNumber(),p.getempAge(),p.getstrator(),p.getip(),p.getclass1() ,p.getempID() );
		  String sql="update movie set name='%s',hot=%.2f,director='%s',actorid='%s',startdate='%s',enddate='%s',Movieduration=%d,fare=%.2f,Moviegenre='%s',country='%s',synopsis='%s',score='%s' where id=%d";
		  sql=String.format(sql,m.getName(),m.getHot(),m.getDirector(),m.getActorid(),m.getStartdate(),m.getEnddate(),m.getMovieduration(),m.getFare(),m.getMoviegenre(),m.getCountry(),m.getSynopsis(),m.getScore(),m.getId());
		  System.out.println(sql);
		return Mysql.update(sql);
	} 
  public static boolean delete(int id) {
		String sql = "delete from movie where id =" + id;
		return Mysql.update(sql);
	}
  public static  List<Moive> select(String name){
	  System.out.println(name);
	  String sql="select * from movie WHERE name like '%"+name+"%'"; 
		List<Moive> list =new ArrayList<Moive>();
		try {
//			Connection conn=Mysql.getConnection(); 
//			PreparedStatement ps=conn.prepareStatement(sql);
			System.out.println(sql);
			ResultSet re=Mysql.query(sql);
			
			while (re.next()) {
				Moive one=new Moive();
				one.setId(re.getInt(1));
				one.setName(re.getString(2));
				one.setHot(re.getDouble(4));
				one.setDirector(re.getString(5));
				one.setActorid(re.getString(6));
				one.setStartdate(re.getString(7));
				one.setEnddate(re.getString(8));
				one.setMovieduration(re.getInt(9));
				one.setFare(re.getDouble(10));
				one.setMoviegenre(re.getString(11));
				one.setCountry(re.getString(12));
				one.setSynopsis(re.getString(13));
				one.setScore(re.getString(14));
				list.add(one);
				
			}
		    re.close();
		} catch (SQLException e){
			// TODO: handle exception
			e.printStackTrace(); 
		}
		return list;
  }
  
 
}

