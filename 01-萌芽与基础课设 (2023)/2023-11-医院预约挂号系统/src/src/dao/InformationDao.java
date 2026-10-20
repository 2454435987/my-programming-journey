package dao;

import pojo.Information;

public class InformationDao extends BaseDao {
    //添加订单信息
    public static int insertOrdersInformationDao(String user,int doctorID,String date,int price,String state,String patients,String patientsCard,String patientsPhone){
        Information i = new Information();
        i.setUser(user);
        i.setDoctorID(doctorID);
        i.setDate(date);
        i.setPrice(price);
        i.setState(state);
        i.setPatients(patients);
        i.setPatientsCard(patientsCard);
        i.setPatientsPhone(patientsPhone);
        String sql=
                "INSERT INTO orders (user, doctorID, date, price,state,patients,patientsCard,patientsPhone)" +
                        "VALUES ("+i.getUser()+","+i.getDoctorID()+",'"+i.getDate()+"',"+i.getPrice()+",'"+i.getState()+"','"+i.getPatients()+"','"+i.getPatientsCard()+"','"+i.getPatientsPhone()+"');";
        BaseDao bd = new BaseDao();
        if(bd.zsg(sql)){
            return 1;//成功

        }else {
            return 0;//失败
        }
    }
}
