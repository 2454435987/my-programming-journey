 package dingdanGL;        //订单管理

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import shujukulianjie.Mysql;

public class dingdanguanli extends JFrame {         //查询，查看所有电影订单

    private JPanel contentPane;
    private JTable table;

       public dingdanguanli() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setTitle("电影订单管理");
        Image icon = new ImageIcon("img/logo/1.png").getImage();
        setIconImage(icon);
      //设置关闭模式
        setDefaultCloseOperation(2);
        // 设置界面居中
        setLocationRelativeTo(null); 
        
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // 创建表格
        table = new JTable();
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        try {
            java.sql.Connection conn = Mysql.getConnection(); // 已连接
            String sql = "SELECT movieid, Ordernum, showtime, showtimes,seatnum,Screeningroom,user FROM orders";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // 设置表头和数据
            table.setModel(buildTableModel(rs));

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cxcx frame = new cxcx();      //此法不行,加this即可
        this.setVisible(true);
    }

    // 将结果集转换为TableModel
       public static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
    	    ResultSetMetaData metaData = rs.getMetaData();
    	    int columnCount = metaData.getColumnCount();

    	    // 创建表头
    	    String[] columnNames = {"影片编号","订单编号","放映日期","放映时间","总票价","放映厅","用户账号"} ;
//    	    for (int i = 1; i <= columnCount; i++) {
//    	        columnNames[i - 1] = metaData.getColumnName(i);
//    	    }

    	    // 创建数据集合，动态添加数据
    	    List<Object[]> dataList = new ArrayList<>();
    	    while (rs.next()) {
    	        Object[] rowData = new Object[columnCount];
    	        for (int i = 1; i <= columnCount; i++) {
    	            rowData[i - 1] = rs.getString(i);
    	        }
    	        dataList.add(rowData);
    	    }

    	    // 将数据集合转换为二维数组
    	    Object[][] data = new Object[dataList.size()][columnCount];
    	    for (int i = 0; i < dataList.size(); i++) {
    	        data[i] = dataList.get(i);
    	    }
    	        
    	    return new javax.swing.table.DefaultTableModel(data, columnNames);
    	}
}