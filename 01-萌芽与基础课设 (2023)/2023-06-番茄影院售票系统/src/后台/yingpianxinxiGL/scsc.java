package yingpianxinxiGL;              //删除电影(最终版)

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import shujukulianjie.Mysql;

public class scsc extends JFrame { // 删除的窗体

    private JPanel contentPane;
    private JTextField textField;
    private JLabel lblNewLabel;
    private JButton btnNewButton;

    public scsc() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        ImageIcon ico = new ImageIcon("img/logo/1.png");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 150, 450, 300);
        setIconImage(ico.getImage());
        setTitle("删除电影信息");
      //设置关闭模式
      setDefaultCloseOperation(2);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 40, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        textField = new JTextField();
        textField.setBounds(86, 68, 200, 30);
        panel.add(textField);
        textField.setColumns(10);

        lblNewLabel = new JLabel("请输入电影ID");
        lblNewLabel.setBounds(86, 30, 200, 30);
        panel.add(lblNewLabel);

        btnNewButton = new JButton("删除");
        btnNewButton.setBounds(150, 150, 100, 30);
        panel.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() { // 添加按钮点击事件
            public void actionPerformed(ActionEvent e) {
            	//始
                Connection con = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    con = Mysql.getConnection(); // 已连接
                    String sql = "DELETE FROM movie WHERE id=?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setInt(1, Integer.parseInt(textField.getText()));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(scsc.this, "记录删除成功！");
                    
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                            }
                        if (pstmt != null) {
                            pstmt.close();
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
               //终
            }
        });
        
        this.setVisible(true);}
    }










//package yingpianxinxiGL;
//
//
//import java.sql.PreparedStatement;                    //增删查改(删除)
//
//import com.mysql.jdbc.Connection;
//
//import shujukulianjie.Mysql;
//
//public class scsc {                           
//	
//	public scsc() throws Exception{
//		 java.sql.Connection conn = Mysql.getConnection();   //已连接
//		    //创建连接对象
//		      //创建预处理语句
//		      String sql = " DELETE FROM movie WHERE id=33 ";
//		      PreparedStatement stmt = conn.prepareStatement(sql);
//
//		     
//		      //执行更新
//		     System.out.println(stmt.executeUpdate());
//		      
//		      //关闭连接和语句对象
//		      stmt.close();
//		      conn.close();
//		      
//
//		      System.out.println("已成功删除电影");
//	}
//	
//}


