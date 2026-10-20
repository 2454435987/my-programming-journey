package dao;

import pojo.Doctor;
import pojo.Hospital;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao extends BaseDao{
    //获取医生的信息
    public  List<Doctor> getDoctor(String departmentType,int hospitalID){
        List<Doctor> list = new ArrayList<Doctor>();
        String sql;

        // 将原始字符串转换为指定的字符编码(直接传过来的不一定是utf-8)
        departmentType = convertEncoding(departmentType,"UTF-8");
        System.out.println(departmentType.equals("全部"));
        if(departmentType.equals("全部")){
            sql="SELECT d.*,dm.departmentType FROM doctor d " +
                    "JOIN department dm ON d.departmentID = dm.departmentID " +
                    "JOIN hospital h on h.hospitalID = d.hospitalID\n" +
                    "WHERE h.hospitalID="+hospitalID +";";
        }else if(departmentType.equals("其他")){
            sql="SELECT d.*,dm.departmentType FROM doctor d " +
                    "JOIN department dm ON d.departmentID = dm.departmentID " +
                    "JOIN hospital h on h.hospitalID = d.hospitalID\n" +
                    "WHERE dm.departmentType!='内科' and dm.departmentType!='外科' and dm.departmentType!='妇产科' and h.hospitalID="+hospitalID +";";

//            sql="SELECT * from doctor where hospitalID="+hospitalID+" and";
        }else{
            sql="SELECT d.*,dm.departmentType FROM doctor d " +
                    "JOIN department dm ON d.departmentID = dm.departmentID " +
                    "JOIN hospital h on h.hospitalID = d.hospitalID\n" +
                    "WHERE  dm.departmentType='"+departmentType+"' and h.hospitalID="+hospitalID +";";
        }
        System.out.println(sql);

        ResultSet rs = query(sql);
        try {
            while (rs.next()){
                Doctor d = new Doctor(rs.getInt(1),rs.getInt(2),rs.getString(10),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getBytes(9));
//                System.out.println(d.toString());
                list.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        return list;
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
