package paipianGL;           //添加电影排片(最终版)
import javax.swing.*;

import shujukulianjie.Mysql;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class paipiantianjia extends Exception{
    private JLabel rownumberLabel, screeningroomLabel,  movieidLabel, ShowtimeLabel,ShowtimesLabel;
    private JTextField  rownumberField, screeningroomField,movieidField,ShowtimeField,ShowtimesField;
    private JButton addButton;
    private JButton back;
    String sql=null;
    private Connection con;
    private PreparedStatement pst;
    
     public paipiantianjia() {
   

        
        JFrame frame =new JFrame();
        frame.setTitle("添加电影排片");
        Image icon = new ImageIcon("img/logo/1.png").getImage();
        frame.setSize(600, 550);
        frame.setIconImage(icon);

        rownumberLabel = new JLabel("序号：");
        screeningroomLabel  = new JLabel("放映厅：");
        movieidLabel = new JLabel("影片编号：");
        ShowtimeLabel = new JLabel("放映日期：");
        ShowtimesLabel = new JLabel("放映时间：");
       
        rownumberLabel.setBounds(150, 50, 70, 30);
        screeningroomLabel.setBounds(150, 80, 70, 30);
        movieidLabel.setBounds(150, 110, 70, 30);
        ShowtimeLabel.setBounds(150, 140, 70, 30);
        ShowtimesLabel.setBounds(150, 170, 70, 30);

//        
        rownumberField = new JTextField();
        screeningroomField = new JTextField();
        movieidField= new JTextField();
        ShowtimeField= new JTextField();
        ShowtimesField= new JTextField();

        
//        idField.setBounds(230, 25, 200, 25);
        rownumberField.setBounds(230, 55, 200, 25);
//        imgField.setBounds(230, 85, 200, 25);
        screeningroomField.setBounds(230, 85, 200, 25);
        movieidField.setBounds(230, 115, 200, 25);
        ShowtimeField.setBounds(230, 145, 200, 25);
        ShowtimesField.setBounds(230, 175, 200, 25);

        
        addButton = new JButton("添加");
        //按钮字体颜色
        addButton.setBounds(180, 420, 100, 50);
        
        back = new JButton("取消");
      //按钮字体颜色
        back.setBounds(300, 420, 100, 50);
        back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new paipianGL();//刷新界面，弹出刷新后的界面
			}
		});
        
        JPanel panel = new JPanel(new GridLayout(16, 8));
        panel.add(back);
        

        panel.add(rownumberLabel);
        panel.add(rownumberField);

        panel.add(screeningroomLabel);
        panel.add(screeningroomField);
        
        panel.add(movieidLabel);
        panel.add(movieidField);
        
        panel.add(ShowtimeLabel);
        panel.add(ShowtimeField);
        
        panel.add(ShowtimesLabel);
        panel.add(ShowtimesField);
        


      
        
        panel.setLayout(null);
        panel.add(new JLabel("")); // 空标签用来占位
        panel.add(addButton);
        frame.getContentPane().add(panel);
        try {
        	Connection conn = Mysql.getConnection();//已连接
        	sql="INSERT INTO filmarrangement (rownumber, screeningroom, movieid,Showtime,Showtimes) VALUES (?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String rownumber =          rownumberField.getText();
                String screeningroom =   screeningroomField.getText();
                String movieid =      movieidField.getText();
                String Showtime =       ShowtimeField.getText();
                String Showtimes =     ShowtimesField.getText();

                String sql2="insert into seat(line,row,flag,rownumber) values\r\n"
                		+ "							(1,1,0,"+rownumber+"),\r\n"
                		+ "							(1,2,0,"+rownumber+"),\r\n"
                		+ "							(1,3,0,"+rownumber+"),\r\n"
                		+ "							(1,4,0,"+rownumber+"),\r\n"
                		+ "							(1,5,0,"+rownumber+"),\r\n"
                		+ "							(1,6,0,"+rownumber+"),\r\n"
                		+ "							(1,7,0,"+rownumber+"),\r\n"
                		+ "							(1,8,0,"+rownumber+"),\r\n"
                		+ "							(1,9,0,"+rownumber+"),\r\n"
                		+ "							(2,1,0,"+rownumber+"),\r\n"
                		+ "							(2,2,0,"+rownumber+"),\r\n"
                		+ "							(2,3,0,"+rownumber+"),\r\n"
                		+ "							(2,4,0,"+rownumber+"),\r\n"
                		+ "							(2,5,0,"+rownumber+"),\r\n"
                		+ "							(2,6,0,"+rownumber+"),\r\n"
                		+ "							(2,7,0,"+rownumber+"),\r\n"
                		+ "							(2,8,0,"+rownumber+"),\r\n"
                		+ "							(2,9,0,"+rownumber+"),\r\n"
                		+ "							(3,1,0,"+rownumber+"),\r\n"
                		+ "							(3,2,0,"+rownumber+"),\r\n"
                		+ "							(3,3,0,"+rownumber+"),\r\n"
                		+ "							(3,4,0,"+rownumber+"),\r\n"
                		+ "							(3,5,0,"+rownumber+"),\r\n"
                		+ "							(3,6,0,"+rownumber+"),\r\n"
                		+ "							(3,7,0,"+rownumber+"),\r\n"
                		+ "							(3,8,0,"+rownumber+"),\r\n"
                		+ "							(3,9,0,"+rownumber+"),\r\n"
                		+ "							(4,1,0,"+rownumber+"),\r\n"
                		+ "							(4,2,0,"+rownumber+"),\r\n"
                		+ "							(4,3,0,"+rownumber+"),\r\n"
                		+ "							(4,4,0,"+rownumber+"),\r\n"
                		+ "							(4,5,0,"+rownumber+"),\r\n"
                		+ "							(4,6,0,"+rownumber+"),\r\n"
                		+ "							(4,7,0,"+rownumber+"),\r\n"
                		+ "							(4,8,0,"+rownumber+"),\r\n"
                		+ "							(4,9,0,"+rownumber+"),\r\n"
                		+ "							(5,1,0,"+rownumber+"),\r\n"
                		+ "							(5,2,0,"+rownumber+"),\r\n"
                		+ "							(5,3,0,"+rownumber+"),\r\n"
                		+ "							(5,4,0,"+rownumber+"),\r\n"
                		+ "							(5,5,0,"+rownumber+"),\r\n"
                		+ "							(5,6,0,"+rownumber+"),\r\n"
                		+ "							(5,7,0,"+rownumber+"),\r\n"
                		+ "							(5,8,0,"+rownumber+"),\r\n"
                		+ "							(5,9,0,"+rownumber+");";
                try {
                    pst.setString(1, rownumber);
                    pst.setString(2, screeningroom);
                    pst.setString(3, movieid);
                    pst.setString(4, Showtime);
                    pst.setString(5, Showtimes);

                    if(pst.executeUpdate()>0) {
                    	JOptionPane.showMessageDialog(null, "电影已添加！");
                    }
                    if(Mysql.update(sql2)) {
                    	System.out.println("执行成功！");
                    }else {
                    	System.out.println("执行失败！");
                    }
                    frame.dispose();
                    new paipianGL();//刷新界面，弹出刷新后的界面
                } catch (Exception ex) {
//                    ex.printStackTrace();
                	JOptionPane.showMessageDialog(null, "添加失败！");
//                    System.out.println("添加失败！");
                }
            }
        });
		// 设置界面居中
        frame.setLocationRelativeTo(null);
		// 设置关闭模式
        frame.setDefaultCloseOperation(2);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
//        new paipiantianjia();
  }
}