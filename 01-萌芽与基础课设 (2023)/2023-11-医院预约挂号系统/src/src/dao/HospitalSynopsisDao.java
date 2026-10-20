package dao;

import pojo.Hospital;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalSynopsisDao extends HospitalDao{
    public List<Hospital> getHospital(int hospitalID){
        List<Hospital> list = new ArrayList<Hospital>();
        String sql="SELECT * from hospital where hospitalID= "+hospitalID+"";
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
}
