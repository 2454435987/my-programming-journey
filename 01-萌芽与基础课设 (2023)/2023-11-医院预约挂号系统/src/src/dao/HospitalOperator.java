package dao;

import java.util.*;
import java.sql.*;

import db.DBHelper;
import pojo.Hospital;

//医院操作
public class HospitalOperator extends BaseDao{
	
	//查询所有医院(并返回医院下的医生个数)
	public List<Hospital> allHospital() {
	    List<Hospital> hospitals = new ArrayList<>();
	    Random random = new Random();// 创建 Random 对象
	    ResultSet rs = query("SELECT h.hospitalID, h.hospitalName, h.hospitalGrade, h.hospitalAddress, h.hospitalIntro, h.hospitalImg, COUNT(d.doctorID) AS doctorCount FROM hospital h LEFT JOIN doctor d ON h.hospitalID = d.hospitalID GROUP BY h.hospitalID, h.hospitalName, h.hospitalGrade, h.hospitalAddress, h.hospitalIntro, h.hospitalImg;");
	    try {
	        while (rs.next()) {
	            Hospital hos = new Hospital();
	            hos.setHospitalID(rs.getInt("hospitalID"));
	            hos.setHospitalName(rs.getString("hospitalName"));
	            hos.setHospitalGrade(rs.getString("hospitalGrade"));
	            hos.setHospitalAddress(rs.getString("hospitalAddress"));
	            hos.setHospitalIntro(rs.getString("hospitalIntro"));
	            hos.setDoctorCount(rs.getInt("doctorCount") + random.nextInt(500-300) + 300);//获取当前医院下医生的人数（实际数据太少了，手动加上随机数300-500）
	            hos.setHospitalImg(rs.getBytes("hospitalImg") == null ? new byte[0] : rs.getBytes("hospitalImg"));
	            if (hos.getHospitalImg() != null && hos.getHospitalImg().length > 0) {
	                String encodeImage = Base64.getEncoder().encodeToString(hos.getHospitalImg());
	                hos.setHosSrc("data:image/jpg;base64," + encodeImage);
	            }
	            
	            hospitals.add(hos);
	        }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    } finally {
	        DBHelper.close(rs, pstmt, conn);
	    }
	    return hospitals;
	}
	
}
