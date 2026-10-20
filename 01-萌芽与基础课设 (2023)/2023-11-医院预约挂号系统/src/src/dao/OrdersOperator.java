package dao;
import java.util.*;
import java.sql.*;
import java.io.*;

import db.DBHelper;
import pojo.*;

public class OrdersOperator extends BaseDao{

	public List<Orders> allOrders() {
		//用来判断是否过期
		List<Orders> ordersList = new ArrayList<>();
		ResultSet rs = query("SELECT o.ordersID, o.user, o.patients, o.patientsCard, patientsPhone, o.doctorID, d.doctorName, dep.departmentType, h.hospitalName, o.date, o.price, o.state FROM orders o JOIN doctor d ON o.doctorID = d.doctorID JOIN department dep ON d.departmentID = dep.departmentID JOIN hospital h ON d.hospitalID = h.hospitalID where state='正在进行'");
		try {
			while(rs.next()) {
				Orders or = new Orders();
				or.setOrdersID(rs.getInt("ordersID"));
				or.setUser(rs.getInt("user"));
				or.setPatients(rs.getString("patients"));
				or.setPatientsCard(rs.getString("patientsCard"));
				or.setPatientsPhone(rs.getString("patientsPhone"));
				or.setDoctorID(rs.getInt("doctorID"));
				or.setDoctorName(rs.getString("doctorName"));
				or.setDepartmentType(rs.getString("departmentType"));
				or.setHospitalName(rs.getString("hospitalName"));
				or.setDate(rs.getString("date"));
				or.setPrice(rs.getInt("price"));
				or.setState(rs.getString("state"));
				ordersList.add(or);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return ordersList;
	}

	//查询当前用户所有订单
	public List<Orders> allOrders(String user) {
		//用来判断是否过期
		String sql="UPDATE orders o\n" +
				"JOIN doctor d ON o.doctorID = d.doctorID \n" +
				"JOIN department dep ON d.departmentID = dep.departmentID \n" +
				"JOIN hospital h ON d.hospitalID = h.hospitalID \n" +
				"SET o.state = '已过期'\n" +
				"WHERE o.user = '"+user+"'\n" +
				"AND o.state != '已取消'\n" +
				"AND o.date < CURDATE();";
		System.out.println(sql);
//		int n = ;
		System.out.println(zsg(sql));
		List<Orders> ordersList = new ArrayList<>();
		ResultSet rs = query("SELECT o.ordersID, o.user, o.patients, o.patientsCard, patientsPhone, o.doctorID, d.doctorName, dep.departmentType, h.hospitalName, o.date, o.price, o.state FROM orders o JOIN doctor d ON o.doctorID = d.doctorID JOIN department dep ON d.departmentID = dep.departmentID JOIN hospital h ON d.hospitalID = h.hospitalID WHERE o.user = ?", user);
		try {
			while(rs.next()) {
				Orders or = new Orders();
				or.setOrdersID(rs.getInt("ordersID"));
				or.setUser(rs.getInt("user"));
				or.setPatients(rs.getString("patients"));
				or.setPatientsCard(rs.getString("patientsCard"));
				or.setPatientsPhone(rs.getString("patientsPhone"));
				or.setDoctorID(rs.getInt("doctorID"));
				or.setDoctorName(rs.getString("doctorName"));
				or.setDepartmentType(rs.getString("departmentType"));
				or.setHospitalName(rs.getString("hospitalName"));
				or.setDate(rs.getString("date"));
				or.setPrice(rs.getInt("price"));
				or.setState(rs.getString("state"));
				ordersList.add(or);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return ordersList;
	}

	//获取订单号对应的订单详细数据
	public Orders getIDByOrders(int ordersID) {
		Orders or = new Orders();
		ResultSet rs = query("SELECT o.ordersID, o.user, o.patients, o.patientsCard, patientsPhone, o.doctorID, d.doctorName, dep.departmentType, h.hospitalName, o.date, o.price, o.state FROM orders o JOIN doctor d ON o.doctorID = d.doctorID JOIN department dep ON d.departmentID = dep.departmentID JOIN hospital h ON d.hospitalID = h.hospitalID WHERE o.ordersID = ?", ordersID);
		try {
			while(rs.next()) {
				or.setOrdersID(rs.getInt("ordersID"));
				or.setUser(rs.getInt("user"));
				or.setPatients(rs.getString("patients"));
				or.setPatientsCard(rs.getString("patientsCard"));
				or.setPatientsPhone(rs.getString("patientsPhone"));
				or.setDoctorID(rs.getInt("doctorID"));
				or.setDoctorName(rs.getString("doctorName"));
				or.setDepartmentType(rs.getString("departmentType"));
				or.setHospitalName(rs.getString("hospitalName"));
				or.setDate(rs.getString("date"));
				or.setPrice(rs.getInt("price"));
				or.setState(rs.getString("state"));
				return or;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return or;
	}

	//状态查询
	public List<Orders> getOrdersByStatus(String user,String state){
		List<Orders> ordersList = new ArrayList<>();
		// 将原始字符串转换为指定的字符编码(直接传过来的不一定是utf-8)
		state = convertEncoding(state,"UTF-8");
		if(state.equals("全部")){
			ordersList=allOrders(user);
			return ordersList;
		}

		ResultSet rs = query("SELECT o.ordersID, o.user, o.patients, o.patientsCard, patientsPhone, o.doctorID, d.doctorName, dep.departmentType, h.hospitalName, o.date, o.price, o.state FROM orders o JOIN doctor d ON o.doctorID = d.doctorID JOIN department dep ON d.departmentID = dep.departmentID JOIN hospital h ON d.hospitalID = h.hospitalID WHERE o.user = ? AND o.state = ?" ,user ,state);
		try {
			while(rs.next()) {
				Orders or = new Orders();
				or.setOrdersID(rs.getInt("ordersID"));
				or.setUser(rs.getInt("user"));
				or.setPatients(rs.getString("patients"));
				or.setPatientsCard(rs.getString("patientsCard"));
				or.setPatientsPhone(rs.getString("patientsPhone"));
				or.setDoctorID(rs.getInt("doctorID"));
				or.setDoctorName(rs.getString("doctorName"));
				or.setDepartmentType(rs.getString("departmentType"));
				or.setHospitalName(rs.getString("hospitalName"));
				or.setDate(rs.getString("date"));
				or.setPrice(rs.getInt("price"));
				or.setState(rs.getString("state"));
				ordersList.add(or);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBHelper.close(rs, pstmt, conn);
		}
		return ordersList;
	}


	//订单删除（只有"已完成"或者"已取消"的订单才能删除）
	public boolean deleteOrders(int ordersID) {
		return zsg("DELETE FROM orders WHERE ordersID = ?", ordersID);
	}

	//订单取消（将"正在进行"修改状态为"已取消"）
	public boolean updateOrders(int ordersID) {
		String state = "已取消";
		return zsg("UPDATE orders SET state=? WHERE ordersID = ?",state, ordersID);
	}
	public boolean updateOrders2(int ordersID) {
		String state = "已完成";
		return zsg("UPDATE orders SET state=? WHERE ordersID = ?",state, ordersID);
	}


	// 将原始字符串转换为指定的字符编码
	public static String convertEncoding(String originalString, String targetEncoding) {
		try {
			// 将原始字符串转换为指定的字符编码
			byte[] bytes = originalString.getBytes(targetEncoding);
			// 将字节数组重新转换为字符串
			return new String(bytes, targetEncoding);
		} catch (UnsupportedEncodingException e) {
			// 如果指定的字符编码不支持，打印异常信息并返回原始字符串
			e.printStackTrace();
			return originalString;
		}
	}
}
