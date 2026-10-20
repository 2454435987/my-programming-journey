package zhuce;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import denglu.DengLu;
import gerenzhuye.GeRenZhongXin;
import shouye.ShouYe;
import shujukulianjie.Mysql;
public class ZhuCe {
	

		Font font = new Font("宋体", Font.BOLD, 24);
		Font font2 = new Font("微软雅黑", Font.PLAIN, 20);
		Font font3 = new Font("微软雅黑", Font.PLAIN, 15);
		 //导入初始化类
		static ChushihuaCaidan csh = new ChushihuaCaidan();
		//创建文本标签和文本框
		JLabel title=new JLabel("番茄影院登录系统");
		JLabel usernamel=new JLabel("用户名");
		JLabel usernuml=new JLabel("密码  ");
		JLabel usernumlagainl=new JLabel("确认密码  ");
		JLabel back=new JLabel("返回主页");
		JLabel denglu=new JLabel("登录账号");
		
		static JTextField usernamet=new JTextField();
		static JPasswordField  usernumt=new JPasswordField();
		static JPasswordField  usernumlagain=new JPasswordField();
		
		  //添加登录按钮
	    JButton jbutton=new JButton("立即注册");
		
		public ZhuCe() {
			csh.initJFrame();
			csh.jf.setSize(1440,810);
			
			
			title.setBounds(1000, 170, 500, 50);
			title.setFont(font);
			
			//用户名
			usernamel.setBounds(930, 250, 100, 25);
			usernamel.setFont(font2);
			
			//用户名文本框
			usernamet.setBounds(1000, 250, 250, 25); 
			usernamet.setBackground(Color.WHITE);
			usernumt.setBackground(Color.WHITE);
			usernumlagain.setBackground(Color.WHITE);
			
			//密码
			usernuml.setBounds(945, 300, 100, 25);
			usernuml.setFont(font2);
			
			//密码文本框
			usernumt.setBounds(1000, 300, 250, 25);
			
			//确认密码
			usernumlagainl.setBounds(905, 350, 100, 25);
			usernumlagainl.setFont(font2);
			
			//确认密码文本框
			usernumlagain.setBounds(1000, 350, 250, 25);
			
			jbutton.setFont(font2);
			jbutton.setBounds(1030, 400, 150, 40);
			
			
			back.setFont(font3);
			back.setBounds(1190, 450, 70, 40);
			
			denglu.setFont(font3);
			denglu.setBounds(960, 450, 70, 40);
			
			
			jbutton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Tianjia();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			});
			
			denglu.addMouseListener(new MouseListener() {
				
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
					denglu.setForeground(new Color(0,0,0));
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					denglu.setForeground(new Color(255,255,255));
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					csh.jf.dispose();
					try {
						new DengLu();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			});
			
			back.addMouseListener(new MouseListener() {
				
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
					back.setForeground(new Color(0,0,0));
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					back.setForeground(new Color(255,255,255));
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					csh.jf.dispose();
					try {
						new ShouYe();
					} catch (Exception e1) {
//					 	JOptionPane.showMessageDialog(null, "账号已经被注册！");
						e1.printStackTrace();
					}
				}
			});
//			
//			
//			
			ImageIcon img = new ImageIcon("img/beijing/3.png");
			JLabel backgroundimg = new JLabel();
			backgroundimg.setSize(1440,810);
//			
//			
			csh.imgchuli(img, backgroundimg);
			csh.jf.setVisible(true);
			
			// 将控件添加到背景图片上
			backgroundimg.add(usernamel);
			backgroundimg.add(usernamet);
			backgroundimg.add(usernuml);
			backgroundimg.add(usernumt);
			backgroundimg.add(usernumlagainl);
			backgroundimg.add(usernumlagain);
			backgroundimg.add(jbutton);
			backgroundimg.add(back);
			backgroundimg.add(denglu);
			backgroundimg.add(title);

			csh.jf.setContentPane(backgroundimg);
			csh.jf.getContentPane().setLayout(null);
		}
		
		//点击注册将账号密码添加到数据库
		public static void Tianjia() throws SQLException {
			//与数据库连接
			Connection conn = (Connection) Mysql.getConnection();
			
			String sql = "INSERT INTO public (user, password) VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			//获取账号的值
			String text = usernamet.getText();
			//获取密码的值
			char[] password = usernumt.getPassword();
			String passwordString = new String(password);
			//获取密码again的值
			char[] password2 = usernumlagain.getPassword();
			String passwordString2 = new String(password2);
			
	        // 设置参数值
	        pstmt.setString(1, text);
	        pstmt.setString(2, passwordString);
	        try {
	        	   //判断两次密码是否一致
		        if(text!=null && passwordString.length()!=0 && passwordString.equals(passwordString2)) {
		        			//执行增加语句
		            		pstmt.executeUpdate();
//		            		会在W弹出一个消息框，显示一个指定的消息文本
//		            		第一个参数是指定该消息框的父组件，null 表示该消息框不属于任何一个父组件；第二个参数是消息文本
		                	JOptionPane.showMessageDialog(null, "注册成功！");
		                	csh.jf.dispose();
		                	new DengLu();
		        }else{
		        	JOptionPane.showMessageDialog(null, "账号和密码不能为空或两次密码不一致！");
		        }
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "账号已经被注册！");
				// TODO: handle exception
			}
	        pstmt.close();
	        conn.close();
		}
}
