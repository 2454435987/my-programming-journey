package dao;

import java.sql.ResultSet;
import java.util.*;

import db.DBHelper;
import pojo.Doctor;

//医生操作
public class DoctorOperator extends BaseDao{

	//查询所有医生(包含外键查询对应医院名称)
	public List<Doctor> allDoctor(){
		List<Doctor> doctors = new ArrayList<>();
		ResultSet rs = query("SELECT d.*, h.hospitalName FROM doctor d JOIN hospital h ON d.hospitalID = h.hospitalID");
		try {
			while(rs.next()) {
				Doctor doc = new Doctor();
				doc.setDoctorID(rs.getInt("doctorID"));
				doc.setHospitalID(rs.getInt("hospitalID"));
				doc.setDepartmentID(rs.getInt("departmentID"));
				doc.setHospitalName(rs.getString("hospitalName"));
				doc.setDoctorName(rs.getString("doctorName"));
				doc.setTitle(rs.getString("title"));
				doc.setGoodAt(rs.getString("goodAt"));
				doc.setProfessional(rs.getString("professional"));
				doc.setOutpatientCharge(rs.getInt("outpatientCharge"));
				doctors.add(doc);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return doctors;
	}
	
	//通过id值获取对应医生的信息(还有外键查询的医院名称和科室名称)
	public Doctor getIdByDoctor(String id) {
		ResultSet rs = query("SELECT d.doctorID, d.hospitalID ,h.hospitalName ,d.departmentID , dept.departmentType, d.doctorName, d.title, d.goodAt, d.professional, d.outpatientCharge, d.doctorImg FROM doctor d JOIN hospital h ON d.hospitalID = h.hospitalID JOIN department dept ON d.departmentID = dept.departmentID WHERE d.doctorID=?", id);
		Doctor doc = new Doctor();
		try {
			while(rs.next()) {
				doc.setDoctorID(rs.getInt("doctorID"));
				doc.setHospitalID(rs.getInt("hospitalID"));
				doc.setHospitalName(rs.getString("hospitalName"));
				doc.setDepartmentID(rs.getInt("departmentID"));
				doc.setDepartmentType(rs.getString("departmentType"));
				doc.setDoctorName(rs.getString("doctorName"));
				doc.setTitle(rs.getString("title"));
				doc.setGoodAt(rs.getString("goodAt"));
				doc.setProfessional(rs.getString("professional"));
				doc.setOutpatientCharge(rs.getInt("outpatientCharge"));
				return doc;
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return doc;
	}
}
