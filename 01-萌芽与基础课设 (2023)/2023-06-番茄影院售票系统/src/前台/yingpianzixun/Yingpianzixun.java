package yingpianzixun;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import shujukulianjie.Mysql;

public class Yingpianzixun {
	Connection conn = (Connection) Mysql.getConnection();
	ChushihuaCaidan csh = new ChushihuaCaidan();
	Color fgColor = new Color(255, 255, 255); 
	Font font1 = new Font("微软雅黑", Font.PLAIN, 20);
	Font font2 = new Font("微软雅黑", Font.PLAIN, 14);
	Font font3 = new Font("微软雅黑", Font.PLAIN, 40);
	static int ye=0;
	public Yingpianzixun() throws Exception {
		//初始化窗体
		csh.initJFrame();
		
		// 初始化菜单
		csh.initJMenuBar();

		//显示搜索
		csh.sousuo();
		
		//显示标题栏
//		title();
		
		//显示电影信息
		movie(); 
		
		// 让显示显示出来，建议写在最后面
		csh.jf.setVisible(true);
	}
	
	
	
	//电影信息
	public void movie() throws Exception {
		JPanel jp = new JPanel();
		jp.setBackground(new Color(100, 100, 100));
		jp.setBounds(0, 60, 1440, 40);
		//正在热映
		JLabel zzry = new JLabel("正在热映");
		zzry.setForeground(fgColor);
		zzry.setFont(font1);
		
		jp.add(zzry);
		//即将上映
		JLabel jjsy = new JLabel("即将上映");
		jjsy.setForeground(fgColor);
		jjsy.setFont(font1);
		
		if(ye==0) {
			zzry.setForeground(new Color(255, 99, 71));
		}else {
			jjsy.setForeground(new Color(255, 99, 71));
		}
		jp.add(Box.createHorizontalStrut(50));
		if(ye==0) {
			zzry.setForeground(new Color(255, 99, 71));
		}else {
			jjsy.setForeground(new Color(255, 99, 71));
		}
		jp.add(Box.createHorizontalStrut(50));
		jp.add(jjsy);
		
		
		
		String sql="SELECT * from movie";
		String sql2="SELECT * FROM movie WHERE startdate > NOW()";
//		获取StateMent对象
		Statement stmt = (Statement) conn.createStatement();
		Statement stmt2 = (Statement) conn.createStatement();
//		执行sql
		ResultSet rs = (ResultSet) stmt.executeQuery(sql);
		ResultSet rs2 = (ResultSet) stmt2.executeQuery(sql2);
//		int i=0;
		
		
		
		while (rs.next()) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {

//					获取名为 "img" 的列的二进制流数据，并将其转换为 java.io.InputStream 类型的对象
			        java.io.InputStream imgStream = rs.getBinaryStream("img");
//			      使用 ImageIO 类的静态方法 read 从输入流中读取图像数据，并返回 BufferedImage 对象
			        BufferedImage image = ImageIO.read(imgStream);
//			       将 BufferedImage 对象转换为 ImageIcon 对象，以便将其用于 JLabel 对象的图像显示。
			        JLabel label = new JLabel();
			        ImageIcon img = new ImageIcon(image);
			       
			        label.setIcon(img);
//			        label.setBounds(220+420*j,150+250*i,130, 185);
			        label.setBounds(120+420*j,150+230*i,130, 185);
			        csh.imgchuli(img,label);
			        csh.jf.getContentPane().add(label);
			        
			        final String name = rs.getString(2);
			        JLabel jl1 = new JLabel(name);
//			        jl1.setBounds(390+420*j,150+250*i,250,30);
			        jl1.setBounds(260+420*j,150+230*i,250,30);
			        jl1.setFont(font1);
			        csh.jf.getContentPane().add(jl1);
			        
			        label.addMouseListener(new MouseListener() {
						
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
							try {
								new Yingpianchaxun(name);
							} catch (Exception e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
						}
					});
			        
			        JLabel jl2 = new JLabel(rs.getString(14));
			        jl2.setBounds(260+420*j,200+230*i,250,40);
			        jl2.setFont(font3);
			        jl2.setForeground(new Color(255, 180, 82));
			        csh.jf.getContentPane().add(jl2);
			        
			        
			        JLabel jl3 = new JLabel("类型:"+rs.getString(11));
			        jl3.setBounds(260+420*j,250+230*i,250,30);
			        jl3.setFont(font1);
			        csh.jf.getContentPane().add(jl3);
			        
			        JLabel jl4 = new JLabel("上映:"+rs.getString(7));
			        jl4.setBounds(260+420*j,290+230*i,250,30);
			        jl4.setFont(font2);
			        csh.jf.getContentPane().add(jl4);
			        
			        rs.next();
			        zzry.addMouseListener(new MouseListener() {
						
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
							if(ye==1) {
								zzry.setForeground(Color.white);
							}
							
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							if(ye==1) {
								zzry.setForeground(new Color(255, 99, 71));
							}
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							ye=0;
							show( label, jl1, jl2 , jl3, jl4);
							jjsy.setForeground(Color.white);
						}
					});
					
					jjsy.addMouseListener(new MouseListener() {
						
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
							if(ye==0) {
								jjsy.setForeground(Color.white);
							}
							
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							if(ye==0) {
								jjsy.setForeground(new Color(255, 99, 71));
							}
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							ye=1;
							zzry.setForeground(Color.white);
							 hide( label, jl1, jl2 , jl3, jl4);
						}
					});
			       
				}
			}
	        break;
		}
		
		while (rs2.next()) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {

//					获取名为 "img" 的列的二进制流数据，并将其转换为 java.io.InputStream 类型的对象
			        java.io.InputStream imgStream = rs2.getBinaryStream("img");
//			      使用 ImageIO 类的静态方法 read 从输入流中读取图像数据，并返回 BufferedImage 对象
			        BufferedImage image = ImageIO.read(imgStream);
//			       将 BufferedImage 对象转换为 ImageIcon 对象，以便将其用于 JLabel 对象的图像显示。
			        JLabel label = new JLabel();
			        ImageIcon img = new ImageIcon(image);
			       
			        label.setIcon(img);
			        label.setBounds(120+420*j,150+230*i,130, 185);
			        csh.imgchuli(img,label);
			        csh.jf.getContentPane().add(label);
			        
			        final String name2 = rs2.getString(2);
			        JLabel jl1 = new JLabel(name2);
			        jl1.setBounds(260+420*j,150+230*i,250,30);
			        jl1.setFont(font1);
			        csh.jf.getContentPane().add(jl1);
			        
			        label.addMouseListener(new MouseListener() {
						
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
							try {
								new Yingpianchaxun(name2);
							} catch (Exception e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
						}
					});
			        
			        JLabel jl2 = new JLabel(rs2.getString(14));
			        jl2.setBounds(260+420*j,200+230*i,250,40);
			        jl2.setFont(font3);
			        jl2.setForeground(new Color(255, 180, 82));
			        csh.jf.getContentPane().add(jl2);
			        
			        
			        JLabel jl3 = new JLabel("类型:"+rs2.getString(11));
			        jl3.setBounds(260+420*j,250+230*i,250,30);
			        jl3.setFont(font1);
			        csh.jf.getContentPane().add(jl3);
			        
			        JLabel jl4 = new JLabel("上映:"+rs2.getString(7));
			        jl4.setBounds(260+420*j,290+230*i,250,30);
			        jl4.setFont(font2);
			        csh.jf.getContentPane().add(jl4);
			        hide( label, jl1, jl2 , jl3, jl4);
			        rs2.next();
			        zzry.addMouseListener(new MouseListener() {
						
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
							if(ye==1) {
								zzry.setForeground(Color.white);
							}
							
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							if(ye==1) {
								zzry.setForeground(new Color(255, 99, 71));
							}
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							ye=0;
							hide( label, jl1, jl2 , jl3, jl4);
							jjsy.setForeground(Color.white);
						}
					});
					
					jjsy.addMouseListener(new MouseListener() {
						
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
							if(ye==0) {
								jjsy.setForeground(Color.white);
							}
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							if(ye==0) {
								jjsy.setForeground(new Color(255, 99, 71));
							}
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							ye=1;
							show( label, jl1, jl2 , jl3, jl4);
							zzry.setForeground(Color.white);
//							 hide( label, jl1, jl2 , jl3, jl4);
						}
					});
			       
				}
			}
			break;
		}
        rs.close();
        stmt.close();
        rs2.close();
        stmt2.close();
        conn.close();
		csh.jf.getContentPane().add(jp);
	}
	
	//隐藏容器
	private void hide(Component... components) {
	    for (Component c : components) {
	        c.setVisible(false); // 隐藏组件
	    }
	}
	//显示容器
	private void show(Component... components) {
	    for (Component c : components) {
	        c.setVisible(true); // 显示组件
	    }
	}
}
