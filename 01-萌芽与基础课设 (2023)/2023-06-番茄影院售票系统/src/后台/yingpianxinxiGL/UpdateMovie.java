package yingpianxinxiGL;           //添加电影(最终版)
import javax.swing.*;

import Yingpianxinxi.Moive;
import Yingpianxinxi.MoiveDao;
import Yingpianxinxi.MoiveUI;
import shujukulianjie.Mysql;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateMovie extends Exception{
    private JLabel idLabel, nameLabel,  imgLabel, hotLabel,directorLabel,actoridLabel,startdateLabel,enddateLabel,MoviedurationLabel,fareLabel,MoviegenreLabel,countryLabel,synopsisLabel,scoreLabel;
    private JTextField  idField, nameField,imgField,hotField,directorField,actoridField,startdateField,enddateField,MoviedurationField,fareField,MoviegenreField,countryField,synopsisField,scoreField;
    private JButton addButton;
    private JButton back;
    String sql=null;
    private Connection con;
    private PreparedStatement pst;
    
     public UpdateMovie(Moive m) {
        
        JFrame frame =new JFrame();
        frame.setTitle("添加电影");
        Image icon = new ImageIcon("img/logo/1.png").getImage();
        frame.setSize(600, 550);
        frame.setIconImage(icon);

//        idLabel = new JLabel("电影编号：");
        nameLabel = new JLabel("电影名：");
//        imgLabel  = new JLabel("图片：");
        hotLabel  = new JLabel("票房：");
        directorLabel = new JLabel("导演：");
        actoridLabel = new JLabel("演员：");
        startdateLabel = new JLabel("上映时间：");
        enddateLabel = new JLabel("下映时间：");
        MoviedurationLabel= new JLabel("电影时长：");
        fareLabel= new JLabel("电影单价：");
        MoviegenreLabel = new JLabel("类型：");
        countryLabel = new JLabel("国家：");
        synopsisLabel = new JLabel("简介：");
        scoreLabel = new JLabel("评分：");
       
//        idLabel.setBounds(150, 20, 70, 30);
        nameLabel.setBounds(150, 50, 70, 30);
        hotLabel.setBounds(150, 80, 70, 30);
        directorLabel.setBounds(150, 110, 70, 30);
        actoridLabel.setBounds(150, 140, 70, 30);
        startdateLabel.setBounds(150, 170, 70, 30);
        enddateLabel.setBounds(150, 200, 70, 30);
        MoviedurationLabel.setBounds(150, 230, 70, 30);
        fareLabel.setBounds(150, 260, 70, 30);
        MoviegenreLabel.setBounds(150, 290, 70, 30);
        countryLabel.setBounds(150,320, 70, 30);
        synopsisLabel.setBounds(150, 350, 70, 30);
        scoreLabel.setBounds(150, 380, 70, 30);
        
//        idField = new JTextField();
        nameField = new JTextField(m.getName());
//        imgField = new JTextField();
        hotField = new JTextField(m.getHot()+"");
        directorField= new JTextField(m.getDirector());
        actoridField= new JTextField(m.getActorid());
        startdateField= new JTextField(m.getStartdate());
        enddateField= new JTextField(m.getEnddate());
        MoviedurationField= new JTextField(m.getMovieduration()+"");
        fareField= new JTextField(m.getFare()+"");
        MoviegenreField= new JTextField(m.getMoviegenre());
        countryField= new JTextField(m.getCountry());
        synopsisField= new JTextField(m.getSynopsis());
        scoreField= new JTextField(m.getScore());
        
        nameField.setBounds(230, 55, 200, 25);
        hotField.setBounds(230, 85, 200, 25);
        directorField.setBounds(230, 115, 200, 25);
        actoridField.setBounds(230, 145, 200, 25);
        startdateField.setBounds(230, 175, 200, 25);
        enddateField.setBounds(230, 205, 200, 25);
        MoviedurationField.setBounds(230, 235, 200, 25);
        fareField.setBounds(230, 265, 200, 25);
        MoviegenreField.setBounds(230, 295, 200, 25);
        countryField.setBounds(230, 325, 200, 25);
        synopsisField.setBounds(230, 355, 200, 25);
        scoreField.setBounds(230, 385, 200, 25);
        
        
        addButton = new JButton("修改");
        addButton.setBounds(180, 420, 100, 50);
        
        back = new JButton("返回");
        back.setBounds(300, 420, 100, 50);
        back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new MoiveUI();
			}
		});
        
        JPanel panel = new JPanel(new GridLayout(16, 8));
        panel.add(back);
        
//        panel.add(idLabel);
//        panel.add(idField);
        
        panel.add(nameLabel);
        panel.add(nameField);
        
//        panel.add(imgLabel);
//        panel.add(imgField);
        
        panel.add(hotLabel);
        panel.add(hotField);
        
        panel.add(directorLabel);
        panel.add(directorField);
        
        panel.add(actoridLabel);
        panel.add(actoridField);
        
        panel.add(startdateLabel);
        panel.add(startdateField);
        
        panel.add(enddateLabel);
        panel.add(enddateField);
        
        panel.add(MoviedurationLabel);
        panel.add(MoviedurationField);
        
        panel.add(fareLabel);
        panel.add(fareField);
        
        panel.add(MoviegenreLabel);
        panel.add(MoviegenreField);
        
        panel.add(countryLabel);
        panel.add(countryField);
        
        panel.add(synopsisLabel);
        panel.add(synopsisField);
        
        panel.add(scoreLabel);
        panel.add(scoreField);
        
      
        
        panel.setLayout(null);
        panel.add(new JLabel("")); // 空标签用来占位
        panel.add(addButton);
        frame.getContentPane().add(panel);


        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String name =          nameField.getText();
               String hot =           hotField.getText();
               String director =      directorField.getText();
               String actorid =       actoridField.getText();
               String startdate =     startdateField.getText();
               String enddate =       enddateField.getText();
               String Movieduration = MoviedurationField.getText();
               String fare =          fareField.getText();
               String Moviegenre =    MoviegenreField.getText();
               String country =       countryField.getText();
               String synopsis =      synopsisField.getText();
               String score =         scoreField.getText();
            	Moive m2 = new Moive();
            	m2.setId(m.getId());
            	m2.setName(name);
            	m2.setHot(Double.parseDouble(hot));
            	m2.setDirector(director);
            	m2.setActorid(actorid);
            	m2.setStartdate(startdate);
            	m2.setEnddate(enddate);
            	m2.setMovieduration(Integer.parseInt(Movieduration));
            	m2.setFare(Double.parseDouble(fare));
            	m2.setMoviegenre(Moviegenre);
            	m2.setCountry(country);
            	m2.setSynopsis(synopsis);
            	m2.setScore(score);
            	
            	boolean f = MoiveDao.update(m2);
				if(f) {
					JOptionPane.showMessageDialog(null, "修改成功！");
					new MoiveUI();
					frame.dispose();
				}else {
					JOptionPane.showMessageDialog(null, "修改失败！");
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

//    public static void main(String[] args) {
//        new UpdateMovie();
//  }
//	private void add(JPanel panel) {
//		// TODO 自动生成的方法存根
//		
//	}
//	private void setDefaultCloseOperation(int exitOnClose) {
//		// TODO 自动生成的方法存根
//		
//	}
//	private void setSize(int i, int j) {
//		// TODO 自动生成的方法存根
//		
//	}
//	private void setTitle(String string) {
//		// TODO 自动生成的方法存根
//		
//	}
}
