package dao;

import pojo.Hospital;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HospitalDao extends BaseDao{
//    SELECT * from hospital LIMIT 3,3;
    public int page=1;//页面
    public int pagerow=5;//每行数据

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagerow() {
        return pagerow;
    }

    public void setPagerow(int pagerow) {
        this.pagerow = pagerow;
    }
    //分页
    public  List<Hospital> pagination(int page,String sql){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql2=sql+" limit "+(page-1)*pagerow+","+pagerow+"";
//        System.out.println(sql2);
        ResultSet rs = query(sql2);
        try {
             while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
             }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        return list;
    }
    //分类之后的总页数
    public  List<Hospital> pagination(String sql){
        List<Hospital> list = new ArrayList<Hospital>();
//        System.out.println(sql);
        ResultSet rs = query(sql);
        try {
                while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        return list;
    }


    public  List<Hospital> pagination(){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql="SELECT * from hospital limit "+(page-1)*pagerow+","+pagerow+"";
        ResultSet rs = query(sql);
        try {
            while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        return list;
    }
    public  List<Hospital> getHospital(){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql="SELECT * from hospital";
        System.out.println("无参数:" +sql);
        ResultSet rs = query(sql);
        try {
            while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        return list;
    }
    public   List<Hospital> getHospital(String hospitalName){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql="SELECT * from hospital where  hospitalName like '%"+hospitalName+"%'";
        System.out.println(sql);
        ResultSet rs = query(sql);
        try {
            while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);


        return list;
    }

    public  StringBuffer ListHospitalFront(String hospitalName){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql="SELECT * from hospital where  hospitalName like '%"+hospitalName+"%'";
        ResultSet rs = query(sql);
        try {
            while (rs.next()){
                Hospital hp = new Hospital(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getBytes(7));
                list.add(hp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db.DBHelper.close(rs,null,null);
        //总页数
//        int  page = list.size()%5==0?list.size()/5:list.size()/5+1;

        return ListHospitalFront(1,sql);
    }

    //用来分类
    public StringBuffer ListHospitalFront(int page,String sql){

        StringBuffer sb = new StringBuffer();
        HospitalDao h = new HospitalDao();
        List<Hospital> hospitals = h.pagination(page,sql);

        for (int i = 0; i < hospitals.size(); i++) {
//          sb.append(hospitals.get(i).getH_name()+"\r\n");
            String base64Image;
            base64Image = convertToBase64(hospitals.get(i).getHospitalImg());

//          sb.append("<img src='data:image/jpeg;base64," + base64Image + "'/>");
            int hospitalID= hospitals.get(i).getHospitalID();
            String hospitalName = hospitals.get(i).getHospitalName();
            String hospitalAddress = hospitals.get(i).getHospitalAddress();
            String hospitalGrade = hospitals.get(i).getHospitalGrade();
            String hospitalType = hospitals.get(i).getHospitalType();
            try {
                sb.append(" <li> ");
                sb.append("<a href=\"hospitalSynopsis.jsp?hospitalID="+hospitalID+"\">");
                if(base64Image!=null){
                    sb.append("<div class=\"box_img\"><img src='data:image/jpeg;base64,"+base64Image+"'/></div>");
                }

                sb.append(" <div class=\"content\">");
                sb.append(" <h2>"+hospitalName+"</h2>") ;
                sb.append(" <p><span>"+hospitalType+"</span><span>"+hospitalGrade+"</span></p>") ;
                sb.append("<p>地址："+hospitalAddress+"</p>") ;
                sb.append("</div>");
                sb.append("</a>");
                sb.append("</li>");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        }
        return sb;
    }
    //获取医院信息列表(前台)
    public StringBuffer ListHospitalFront(){
        StringBuffer sb = new StringBuffer();
        HospitalDao h = new HospitalDao();
        List<Hospital> hospitals = h.pagination();
        for (int i = 0; i < hospitals.size(); i++) {
//            sb.append(hospitals.get(i).getH_name()+"\r\n");
            String base64Image;
            base64Image = convertToBase64(hospitals.get(i).getHospitalImg());

//            sb.append("<img src='data:image/jpeg;base64," + base64Image + "'/>");
            int hospitalID= hospitals.get(i).getHospitalID();
            String hospitalName = hospitals.get(i).getHospitalName();
            String hospitalAddress = hospitals.get(i).getHospitalAddress();
            String hospitalGrade = hospitals.get(i).getHospitalGrade();
            String hospitalType = hospitals.get(i).getHospitalType();
            try {
                sb.append(" <li> ");
                sb.append("<a href=\"hospitalSynopsis.jsp?hospitalID="+hospitalID+"\">");
                if(base64Image!=null){
                    sb.append("<div class=\"box_img\"><img src='data:image/jpeg;base64,"+base64Image+"'/></div>");
                }

                sb.append(" <div class=\"content\">");
                sb.append(" <h2>"+hospitalName+"</h2>") ;
                sb.append(" <p><span>"+hospitalType+"</span><span>"+hospitalGrade+"</span></p>") ;
                sb.append("<p>地址："+hospitalAddress+"</p>") ;
                sb.append("</div>");
                sb.append("</a>");
                sb.append("</li>");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        }
        return sb;
    }

    private static String convertToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }

}
