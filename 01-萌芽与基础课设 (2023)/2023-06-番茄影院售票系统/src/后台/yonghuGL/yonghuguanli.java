package yonghuGL;                   //用户管理(最终版)

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import shujukulianjie.Mysql;

public class yonghuguanli extends JFrame {         //查询，查看所有电影

    private JPanel contentPane;
    private JTable table;

       public yonghuguanli() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setTitle("用户管理：查看");
        
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
            String sql = "SELECT user , password, name, sex, birthday,Signature FROM public";
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

        this.setVisible(true);
    }

    // 将结果集转换为TableModel
       public static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
    	    ResultSetMetaData metaData = rs.getMetaData();
    	    int columnCount = metaData.getColumnCount();

    	    // 创建表头
    	    String[] columnNames = {"用户账号","用户密码","用户名","性别","生日","个人简介"};
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














//    public static void main(String[] args) {
//    EventQueue.invokeLater(new Runnable() {
//        public void run() {
//            try {
//            	cxcx frame = new cxcx();
//               frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    });
//   }
