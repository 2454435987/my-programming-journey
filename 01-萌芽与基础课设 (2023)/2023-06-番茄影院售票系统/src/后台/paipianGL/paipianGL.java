package paipianGL;                 //排片管理(最终版)

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import shujukulianjie.Mysql;

public class paipianGL extends JFrame {         //查询，查看所有电影

    private JPanel contentPane;
    private JTable table;

       public paipianGL() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280,400);
        setTitle("电影排片管理");
        Image icon = new ImageIcon("img/logo/1.png").getImage();
        setIconImage(icon);
      //设置关闭模式
        setDefaultCloseOperation(2);
       setResizable(false);
    // 设置界面居中
       setLocationRelativeTo(null); 
       
        JPanel icon1 = new JPanel();
        JButton button1 = new JButton("+");
        
        button1.addMouseListener(new MouseAdapter() {
            @Override
  public void mouseClicked(MouseEvent e) {
            	new paipiantianjia();
         }
         }  );
        
        icon1.add(button1);
        button1.setBackground(new Color(255,255,255));
        button1.setForeground(new Color(255, 0, 0));
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        contentPane.add(icon1, BorderLayout.SOUTH);

        // 创建表格
        table = new JTable();
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        try {
            java.sql.Connection conn = Mysql.getConnection(); // 已连接
            String sql = "SELECT rownumber, screeningroom, movieid, Showtime,Showtimes FROM filmarrangement";
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
    	    String[] columnNames = {"序号","放映厅","影片编号","放映日期","放映时间"};

    	    
    	    
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

