package yingpianzixun;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.omg.CORBA.portable.InputStream;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import goupiao.GouPiao;
import shujukulianjie.Mysql;

//影片详细查询
public class Yingpianchaxun {
	ChushihuaCaidan csh = new ChushihuaCaidan();
	private String text;
	//获取搜索栏里面的内容
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Yingpianchaxun(String text) throws Exception {
		super();
		this.text = text;
		// 初始化界面
				csh.initJFrame();
				
				// 初始化菜单
				csh.initJMenuBar();

				//显示搜索
				csh.sousuo();
				

				//获取mysql中连接好的数据库
				Connection conn=(Connection) Mysql.getConnection();
				String sql = "SELECT * from movie WHERE name like '%"+text+"%'";
//				获取StateMent对象
				Statement stmt = (Statement) conn.createStatement();
//				执行sql
				ResultSet rs = (ResultSet) stmt.executeQuery(sql);
				
//					这是一个 while 循环，用于遍历 ResultSet 中的所有记录
					while (rs.next()) {
//						if(text.equals(rs.getString(2))) {
//							获取名为 "img" 的列的二进制流数据，并将其转换为 java.io.InputStream 类型的对象
				            java.io.InputStream imgStream = rs.getBinaryStream("img");
//				          使用 ImageIO 类的静态方法 read 从输入流中读取图像数据，并返回 BufferedImage 对象
				            BufferedImage image = ImageIO.read(imgStream);
//				           将 BufferedImage 对象转换为 ImageIcon 对象，以便将其用于 JLabel 对象的图像显示。
				            ImageIcon img = new ImageIcon(image);
				            JLabel label = new JLabel(img);
//				            label.setHorizontalAlignment(JLabel.CENTER);
//				            label.setVerticalAlignment(JLabel.CENTER);
				            label.setBounds(0,0,159, 227);
				            
				            JPanel label2 = new JPanel(new GridBagLayout());
				            label2.add(label, new GridBagConstraints());
				            label2.setBounds(300, 150, 169, 237);
				            label2.setBackground(Color.WHITE); // 设置背景色为白色
				            label2.setOpaque(true); // 将不透明属性设置为 true
				            csh.imgchuli(img,label);
				            csh.jf.getContentPane().add(label2);
				            
				            Font font1 = new Font("微软雅黑", Font.PLAIN, 22);
				            Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
				            Font font3 = new Font("微软雅黑", Font.PLAIN, 20);
				            Color white = new Color(255,255,255);
				            
				    		JLabel jl1 = new JLabel();
				    		//获取电影名文本内容
				    		String name = rs.getString(2);
				    		// 设置文本内容
				    		jl1.setText(name);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl1.setFont(font1);
				    		jl1.setForeground(white);//白色
				    		jl1.setBounds(500, 130, 500, 40);
				    		csh.jf.getContentPane().add(jl1);
				            
				    		JLabel jl2 = new JLabel();
				    		//获取电影类型文本内容
				    		String Moviegenre = rs.getString(11);
				    		// 设置文本内容
				    		jl2.setText("类型：" + Moviegenre);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl2.setFont(font2);
				    		jl2.setForeground(white);//白色
				    		jl2.setBounds(500, 160, 200, 40);
				    		csh.jf.getContentPane().add(jl2);
				    		
				    		JLabel jl3 = new JLabel();
				    		//获取电影国家的文本内容
				    		String country = rs.getString(12);
				    		//获取电影时长的文本内容
				    		String Movieduration = rs.getString(9);
				    		if(Movieduration==null) {
				    			Movieduration="暂无";
				    		}
				    		// 设置文本内容
				    		jl3.setText(country+"/" + Movieduration+"分钟");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl3.setFont(font2);
				    		jl3.setForeground(white);//白色
				    		jl3.setBounds(500, 180, 200, 40);
				    		csh.jf.getContentPane().add(jl3);
				    		
				    		JLabel jl4 = new JLabel();
				    		// 设置文本内容
				    		jl4.setText("导演");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl4.setFont(font3);
				    		jl4.setForeground(white);//白色
				    		jl4.setBounds(500, 210, 200, 40);
				    		csh.jf.getContentPane().add(jl4);
				    		
				    		JLabel jl5 = new JLabel();
				    		//获取导演的文本内容
				    		String director = rs.getString(5);
				    		// 设置文本内容
				    		jl5.setText(director);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl5.setFont(font3);
				    		jl5.setForeground(white);//白色
				    		jl5.setBounds(500, 240, 200, 40);
				    		csh.jf.getContentPane().add(jl5);
				    		
				    		JLabel jl6 = new JLabel();
				    		// 设置文本内容
				    		jl6.setText("主演");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl6.setFont(font3);
				    		jl6.setForeground(white);//白色
				    		jl6.setBounds(700, 210, 200, 40);
				    		csh.jf.getContentPane().add(jl6);
				    		
				    		JLabel jl7 = new JLabel();
				    		//获取演员的文本内容
				    		String actorid = rs.getString(6);
				    		// 设置文本内容
				    		jl7.setText(actorid);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl7.setFont(font3);
				    		jl7.setForeground(white);//白色
				    		jl7.setBounds(700, 240, 500, 40);
				    		csh.jf.getContentPane().add(jl7);
				    		
				    		JButton gp = new JButton("立即购票");
				    		gp.setFont(font3);
				    		gp.setForeground(white);//白色
				    		gp.setBounds(500, 300, 150, 40);
				    		gp.setBackground(new Color(255, 99, 71));//番茄色
				    		csh.jf.getContentPane().add(gp);
				    		
				    		gp.addMouseListener(new MouseListener() {
								
								@Override
								public void mouseReleased(MouseEvent e) {
									// TODO 自动生成的方法存根
									
								}
								
								@Override
								public void mousePressed(MouseEvent e) {
									// TODO 自动生成的方法存根
									
								}
								
								@Override
								public void mouseExited(MouseEvent e) {
									// TODO 自动生成的方法存根
									
								}
								
								@Override
								public void mouseEntered(MouseEvent e) {
									// TODO 自动生成的方法存根
									
								}
								
								@Override
								public void mouseClicked(MouseEvent e) {
									csh.jf.dispose();
									try {
										new GouPiao(name);
									} catch (Exception e1) {
										// TODO 自动生成的 catch 块
										e1.printStackTrace();
									}
								}
							});
				    		
				    		JLabel jl8 = new JLabel();
				    		// 设置文本内容
				    		jl8.setText("简介");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl8.setFont(font1);
				    		jl8.setForeground(new Color(0,0,0));//黑色
				    		jl8.setBounds(120, 440, 100, 40);
				    		csh.jf.getContentPane().add(jl8);
				    		
				    		JPanel jp = new JPanel();
				    		jp.setBackground(new Color(0,0,0));//黑色
				    		jp.setBounds(120, 490, 1150, 1);
				    		csh.jf.getContentPane().add(jp);
				    		
				    		JLabel jl9 = new JLabel();
				    		//获取简介的文本内容
				    		String synopsis = rs.getString(13);
				    		// 设置文本内容
//				    		将文本内容用<html><body>标签包裹，并使用style属性设置了宽度为1300px，当文本超过该宽度时会自动换行显示。
				    		jl9.setText("<html><body style='width: 900px'>" + synopsis + "</body></html>");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl9.setFont(font3);
				    		jl9.setForeground(new Color(0,0,0));//黑色
				    		jl9.setBounds(120, 260, 1300, 600);
				    		csh.jf.getContentPane().add(jl9);
				    		
				            ImageIcon icon = new ImageIcon("img/beijing/2.png");
				            //创建背景图片
				            JLabel bg = new JLabel(icon);
				            // 指定容器的大小和图片位置(x,y,width,height)
				            bg.setBounds(0, 60, 1920,310);
							// 把图片等比缩放到width为1300px，height为210px的大小
							csh.imgchuli(icon, bg);
							csh.jf.getContentPane().add(bg);
							
				    		// 让显示显示出来，建议写在最后面
				    		csh.jf.setVisible(true);
				    		break;
			
					}
				

        rs.close();
        stmt.close();
        conn.close();
        

	}

	public Yingpianchaxun() throws Exception {
		new Yingpianchaxun(text);
	}
}
