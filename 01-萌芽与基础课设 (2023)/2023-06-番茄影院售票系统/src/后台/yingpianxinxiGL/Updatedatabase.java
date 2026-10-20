package yingpianxinxiGL;             //修改(最终版)

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import shujukulianjie.Mysql;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Updatedatabase extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldid;//1
    private JTextField textFieldname;//2
    private JTextField textFieldhot;//3
    private JTextField textFielddirector;//4
    private JTextField textFieldactor;//5
    private JTextField textFieldstartdate;//6
    private JTextField textFieldenddate;//7
    private JTextField textFieldMovieduration;//8
    private JTextField textFieldfare;//9
    private JTextField textFieldMoviegenre;//10
    private JTextField textFieldcountry;//11
    private JTextField textFieldsynopsis;//12
    private JTextField textFieldscore;//13
    
    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    Updatedatabase frame = new Updatedatabase();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the frame.
     */
     public Updatedatabase() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 700);
        setLocationRelativeTo(null);//窗体界面居中
        Image icon = new ImageIcon("img/logo/1.png").getImage();
        setIconImage(icon);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
      //设置关闭模式
        setDefaultCloseOperation(2);
        
        JLabel lblEnterid = new JLabel("请输入要更新的记录id：");//1
        lblEnterid.setBounds(200, 30, 160, 16);
        contentPane.add(lblEnterid);

        JLabel lblname = new JLabel("影片名：");//2
        lblname.setBounds(285, 70, 160, 16);
        contentPane.add(lblname);

        JLabel lblhot = new JLabel("热度：");//3
        lblhot.setBounds(296, 110, 160, 16);
        contentPane.add(lblhot);

        JLabel lbldirector = new JLabel("导演：");//4
        lbldirector.setBounds(296, 150, 160, 16);
        contentPane.add(lbldirector);

        JLabel lblactor = new JLabel("主演：");//5
        lblactor.setBounds(296, 190, 160, 16);
        contentPane.add(lblactor);
        
        JLabel lblstartdate = new JLabel("上映时间：");//6
        lblstartdate.setBounds(272, 230, 160, 16);
        contentPane.add(lblstartdate);
        
        JLabel lblenddate = new JLabel("下映时间：");//7
        lblenddate.setBounds(272, 270, 160, 16);
        contentPane.add(lblenddate);
        
        JLabel lblMovieduration = new JLabel("时长：");//8
        lblMovieduration.setBounds(296, 310, 160, 16);
        contentPane.add(lblMovieduration);
        
        JLabel lblfare = new JLabel("票价：");//9
        lblfare.setBounds(296, 350, 160, 16);
        contentPane.add(lblfare);
        
        JLabel lblMoviegenre = new JLabel("类型：");//10
        lblMoviegenre.setBounds(296, 390, 160, 16);
        contentPane.add(lblMoviegenre);
        
        JLabel lblcountry = new JLabel("国家：");//11
        lblcountry.setBounds(296, 430, 160, 16);
        contentPane.add(lblcountry);
        
        JLabel lblsynopsis = new JLabel("简介：");//12
        lblsynopsis.setBounds(296, 470, 160, 16);
        contentPane.add(lblsynopsis);
        
        JLabel lblscore = new JLabel("评分：");//13
        lblscore.setBounds(296, 510, 160, 16);
        contentPane.add(lblscore); 
        
        //
        textFieldid = new JTextField();
        textFieldid.setBounds(360, 25, 130, 26);//1
        contentPane.add(textFieldid);
        textFieldid.setColumns(10);

        textFieldname = new JTextField();
        textFieldname.setBounds(360, 65, 130, 26);//2
        contentPane.add(textFieldname);
        textFieldname.setColumns(10);

        textFieldhot = new JTextField();
        textFieldhot.setBounds(360, 105, 130, 26);//3
        contentPane.add(textFieldhot);
        textFieldhot.setColumns(10);

        textFielddirector = new JTextField();
        textFielddirector.setBounds(360, 145, 130, 26);//4
        contentPane.add(textFielddirector);
        textFielddirector.setColumns(10);
        
        textFieldactor = new JTextField();
        textFieldactor.setBounds(360, 185, 130, 26);//5
        contentPane.add(textFieldactor);
        textFieldactor.setColumns(10);
        
        textFieldstartdate = new JTextField();
        textFieldstartdate.setBounds(360, 225, 130, 26);//6
        contentPane.add(textFieldstartdate);
        textFieldstartdate.setColumns(10);
        
        textFieldenddate = new JTextField();
        textFieldenddate.setBounds(360, 265, 130, 26);//7
        contentPane.add(textFieldenddate);
        textFieldenddate.setColumns(10);
        
        textFieldMovieduration = new JTextField();
        textFieldMovieduration.setBounds(360, 305, 130, 26);//8
        contentPane.add(textFieldMovieduration);
        textFieldMovieduration.setColumns(10);
        
        textFieldfare = new JTextField();
        textFieldfare.setBounds(360, 345, 130, 26);//9
        contentPane.add(textFieldfare);
        textFieldfare.setColumns(10);
        
        textFieldMoviegenre = new JTextField();
        textFieldMoviegenre.setBounds(360, 385, 130, 26);//10
        contentPane.add(textFieldMoviegenre);
        textFieldMoviegenre.setColumns(10);
        
        textFieldcountry = new JTextField();
        textFieldcountry.setBounds(360, 425, 130, 26);//11
        contentPane.add(textFieldcountry);
        textFieldcountry.setColumns(10);
        
        textFieldsynopsis = new JTextField();
        textFieldsynopsis.setBounds(360, 465, 130, 26);//12
        contentPane.add(textFieldsynopsis);
        textFieldsynopsis.setColumns(10);
        
        textFieldscore = new JTextField();
        textFieldscore.setBounds(360, 505, 130, 26);//13
        contentPane.add(textFieldscore);
        textFieldscore.setColumns(10);

        JButton btnUpdate = new JButton("更新");
        
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(textFieldid.getText());//1
                String name = textFieldname.getText();//2
                String hot = textFieldhot.getText();//3
                String director = textFielddirector.getText();//4
                String actor = textFieldactor.getText();//5
                String startdate = textFieldstartdate.getText();//6
                String enddate = textFieldenddate.getText();//7
                String Movieduration = textFieldMovieduration.getText();//8
                String fare = textFieldfare.getText();//9
                String Moviegenre = textFieldMoviegenre.getText();//10
                String country = textFieldcountry.getText();//11
                String synopsis = textFieldsynopsis.getText();//12
                String score = textFieldscore.getText();//13
                updateData(id, name, hot, director, actor, startdate, enddate, Movieduration, fare, Moviegenre, country, synopsis, score);
            }
        });
        btnUpdate.setBounds(370, 550, 130, 26);  //修改按钮大小
        contentPane.add(btnUpdate);
        setVisible(true);
    }

    /**
     * 更新数据
     * @param id 要更新的记录id
     * @param name 新的参数1
     * @param hot 新的参数2
     * @param director 新的参数3
     * ......
     */
    private void updateData(int id, String name, String hot, String director, String actor, String startdate, String enddate, String Movieduration, String fare, String Moviegenre, String country, String synopsis, String score) {
        Connection connection = null;

        try {
            // 连接数据库
        	connection = Mysql.getConnection();//已连接

            // 创建SQL语句
            String sql = "UPDATE movie SET name=?, hot=?, director=?,actorid=? ,  startdate=? ,  enddate=? ,  Movieduration=?  , fare=? , Moviegenre=?  , country=? , synopsis=? ,  score=?  WHERE id = ?";

            // 创建PreparedStatement对象
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, hot);
            preparedStatement.setString(3, director);
            preparedStatement.setString(4, actor);
            preparedStatement.setString(5, startdate);
            preparedStatement.setString(6, enddate);
            preparedStatement.setString(7, Movieduration);
            preparedStatement.setString(8, fare);
            preparedStatement.setString(9, Moviegenre);
            preparedStatement.setString(10, country);
            preparedStatement.setString(11, synopsis);
            preparedStatement.setString(12, score);
            
            preparedStatement.setInt(13, id);

           
            // 执行更新操作
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println(sql);
            if (rowsUpdated > 0) {
                System.out.println("数据已更新成功");
                JOptionPane.showMessageDialog(Updatedatabase.this, "记录修改成功！");
            }
        } catch (SQLException ex) {
            System.out.println("更新数据时出现异常" + ex.getMessage());
        } finally {
            try {
                // 关闭数据库连接
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("关闭连接时出现异常" + ex.getMessage());
            }
        }
    }
    
}


