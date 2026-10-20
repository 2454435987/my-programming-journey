package goupiao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
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
import shujukulianjie.Mysql;

//影片详细查询
public class GouPiao {
	private static JLabel selectedDateLabel = null;
	ChushihuaCaidan csh = new ChushihuaCaidan();
	private String text;
	private Date date;
	 JLabel jl20=new JLabel();
	// 定义成员变量currentPanel
	private JPanel currentPanel = null;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date2) {
		this.date = date2;
	}

	//获取搜索栏里面的内容
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public GouPiao(String text) throws Exception {
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
				String sql2 = "SELECT showtime FROM filmarrangement GROUP BY showtime";

//				获取StateMent对象
//				Statement stmt = (Statement) conn.createStatement();
//				Statement stmt2 = (Statement) conn.createStatement();

//				执行sql
				ResultSet rs = (ResultSet) Mysql.query(sql);
				ResultSet rs2 = (ResultSet) Mysql.query(sql2);

				 Font font1 = new Font("微软雅黑", Font.PLAIN, 22);
		            Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
		            Font font3 = new Font("微软雅黑", Font.PLAIN, 20);
		            Color white = new Color(255,255,255);
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
				    		
				    		//获取电影放映时间的文本内容
				    		String starttime = rs.getString(7);
				    		JLabel time = new JLabel(starttime+"中国大陆");
				    		time.setFont(font2);
				    		time.setForeground(white);//白色
				    		time.setBounds(500, 200, 1500, 40);
				    		csh.jf.getContentPane().add(time);
				    		
				    		JLabel jl4 = new JLabel();
				    		// 设置文本内容
				    		jl4.setText("导演");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl4.setFont(font3);
				    		jl4.setForeground(white);//白色
				    		jl4.setBounds(500, 260, 200, 40);
				    		csh.jf.getContentPane().add(jl4);
				    		
				    		JLabel jl5 = new JLabel();
				    		//获取导演的文本内容
				    		String director = rs.getString(5);
				    		// 设置文本内容
				    		jl5.setText(director);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl5.setFont(font3);
				    		jl5.setForeground(white);//白色
				    		jl5.setBounds(500, 290, 200, 40);
				    		csh.jf.getContentPane().add(jl5);
				    		
				    		JLabel jl6 = new JLabel();
				    		// 设置文本内容
				    		jl6.setText("主演");
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl6.setFont(font3);
				    		jl6.setForeground(white);//白色
				    		jl6.setBounds(700, 260, 200, 40);
				    		csh.jf.getContentPane().add(jl6);
				    		
				    		JLabel jl7 = new JLabel();
				    		//获取演员的文本内容
				    		String actorid = rs.getString(6);
				    		// 设置文本内容
				    		jl7.setText(actorid);
				    		// 设置字体 Font.PLAIN：普通字体
				    		jl7.setFont(font3);
				    		jl7.setForeground(white);//白色
				    		jl7.setBounds(700, 290, 500, 40);
				    		csh.jf.getContentPane().add(jl7);
				    		
				    		
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
			   		JLabel jl8 = new JLabel();
		    		// 设置文本内容
		    		jl8.setText("日期：");
		    		// 设置字体 Font.PLAIN：普通字体
		    		jl8.setFont(font2);
		    		jl8.setForeground(new Color(205, 205, 205));//灰色
		    		jl8.setBounds(120, 400, 100, 40);
		    		csh.jf.getContentPane().add(jl8);
		    		
		    		
		    		//每个水平间距为260
			   		JLabel jl9 = new JLabel();
		    		// 设置文本内容
			   		jl9.setText("放映时间：");
		    		// 设置字体 Font.PLAIN：普通字体
			   		jl9.setFont(font2);
			   		jl9.setForeground(new Color(0, 0, 0));//黑色
			   		jl9.setBounds(220, 500, 100, 40);
		    		csh.jf.getContentPane().add(jl9);
		    		
			   		JLabel jl10 = new JLabel();
		    		// 设置文本内容
			   		jl10.setText("放映厅：");
		    		// 设置字体 Font.PLAIN：普通字体
			   		jl10.setFont(font2);
			   		jl10.setForeground(new Color(0, 0, 0));//黑色
			   		jl10.setBounds(490, 500, 100, 40);
		    		csh.jf.getContentPane().add(jl10);
		    		
			   		JLabel jl11 = new JLabel();
		    		// 设置文本内容
			   		jl11.setText("售价（元）：");
		    		// 设置字体 Font.PLAIN：普通字体
			   		jl11.setFont(font2);
			   		jl11.setForeground(new Color(0, 0, 0));//黑色
			   		jl11.setBounds(750, 500, 100, 40);
		    		csh.jf.getContentPane().add(jl11);
		    		
			   		JLabel jl12 = new JLabel();
		    		// 设置文本内容
			   		jl12.setText("选择购票：");
		    		// 设置字体 Font.PLAIN：普通字体
			   		jl12.setFont(font2);
			   		jl12.setForeground(new Color(0, 0, 0));//黑色
			   		jl12.setBounds(1010, 500, 100, 40);
		    		csh.jf.getContentPane().add(jl12);
		    		
		    		
		    		JPanel jp = new JPanel();
		    		jp.setBackground(new Color(0,0,0));//黑色
		    		jp.setBounds(120, 490, 1150, 1);
		    		csh.jf.getContentPane().add(jp);
		    		

		    		

		    		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM"); // 格式化为月份
		    		SimpleDateFormat sdfDay = new SimpleDateFormat("dd"); // 格式化为天
		    		int i=0;
		    		int f=0;
		    		int m=0;//控制循环只循环一次
		    		boolean isJL20Added = true; // 初始化为 true
		    		while(rs2.next()) {
		    			JLabel datel = new JLabel();
		    			datel.setOpaque(true); 
			    		// 设置文本内容
		    			Date date = rs2.getDate(1); // 获取日期
		    		    String month = sdfMonth.format(date); // 提取月份
		    		    String day = sdfDay.format(date); // 提取天
		    			datel.setText(month+"月"+day+"日");
		    			datel.setHorizontalAlignment(JLabel.CENTER);
			    		// 设置字体 Font.PLAIN：普通字体
		    			datel.setFont(font2);
		    			datel.setForeground(new Color(0, 0, 0));//黑色
		    			datel.setBounds(170+100*i, 400, 100, 40);
			    		csh.jf.getContentPane().repaint();//刷新组件  不刷新不显示
			    		
			    		csh.jf.getContentPane().add(datel);
			    	    // 强制将标签放到最顶层
			    	    csh.jf.getContentPane().setComponentZOrder(datel, 0);

			    		i++;

			    		
						String sql3 = "SELECT * FROM filmarrangement JOIN movie ON filmarrangement.movieId = movie.Id WHERE Showtime = '"+date+"' and name = '"+text+"' ORDER BY Showtimes ASC";
						ResultSet rs3 = (ResultSet) Mysql.query(sql3);
						int j=0,k=0;
			            JPanel newPanel = new JPanel();
			            newPanel.setLayout(null);
						while(m==0) {
							while(rs3.next()) {
								//时间
								JLabel jl13 = new JLabel();
								// 设置文本内容
								String time = rs3.getString(5);
								jl13.setText(time);
								// 设置字体 Font.PLAIN：普通字体
								jl13.setFont(font2);
								jl13.setForeground(new Color(0,0, 0));//黑色
								jl13.setBounds(220, 540+j*50, 100, 40);
								csh.jf.getContentPane().add(jl13);
								
								//影厅
								JLabel jl14 = new JLabel();
								// 设置文本内容
								String screeningroom = rs3.getString(2);
								jl14.setText(screeningroom);
								// 设置字体 Font.PLAIN：普通字体
								jl14.setFont(font2);
								jl14.setForeground(new Color(0,0, 0));//黑色
								jl14.setBounds(506, 540+j*50, 100, 40);
								csh.jf.getContentPane().add(jl14);
								
								//价格
								JLabel jl15 = new JLabel();
								// 设置文本内容
								jl15.setText(rs3.getString(15));
								// 设置字体 Font.PLAIN：普通字体
								jl15.setFont(font2);
								jl15.setForeground(new Color(0,0, 0));//黑色
								jl15.setBounds(770, 540+j*50, 100, 40);
								csh.jf.getContentPane().add(jl15);
								
								//立即购票
								JButton jl16 = new JButton();
								// 设置文本内容
								jl16.setText("立即购票");
								// 设置字体 Font.PLAIN：普通字体
								jl16.setFont(font2);
								jl16.setForeground(new Color(255,255, 255));//黑色
								jl16.setBackground(new Color(255, 99, 71));
								jl16.setBounds(1000, 545+j*50, 100, 30);
								
								String name = rs3.getString(7);
								int fare = rs3.getInt(15);
								int id = rs3.getInt(6);
								String movieroom = rs3.getString(2);
								
								
					            java.io.InputStream imgStream = rs.getBinaryStream("img");
//						          使用 ImageIO 类的静态方法 read 从输入流中读取图像数据，并返回 BufferedImage 对象
						            BufferedImage image = ImageIO.read(imgStream);
//						           将 BufferedImage 对象转换为 ImageIcon 对象，以便将其用于 JLabel 对象的图像显示。
						            ImageIcon img = new ImageIcon(image);
								
								jl16.addMouseListener(new MouseListener() {
									
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
											
											new XuanZuo(name,time,date,img,fare,id,movieroom);
										} catch (Exception e1) {
											// TODO 自动生成的 catch 块
											e1.printStackTrace();
										}
									}
								});
								
								csh.jf.getContentPane().add(jl16);
								
								
								//横线
					    		JPanel jp2 = new JPanel();
					    		jp2.setBackground(new Color(166, 166, 166));//黑色
					    		jp2.setBounds(120, 538+j*50, 1150, 1);
					    		csh.jf.getContentPane().add(jp2);
								j++;
								 newPanel.add(jl13);
					                newPanel.add(jl14);
					                newPanel.add(jl15);
					                newPanel.add(jl16);
					                newPanel.add(jp2);
					                // 将新面板加入主容器
					                csh.jf.getContentPane().add(newPanel);
					                newPanel.setBounds(0, 0, 1440, 900);
					                // 更新currentPanel
					                currentPanel = newPanel;
								
							}
							m++;
								
						}
			    		datel.addMouseListener(new MouseListener() {
							
							@Override
							public void mouseReleased(MouseEvent e) {
								// TODO 自动生成的方法存根
								
							}
							
							@Override
							public void mousePressed(MouseEvent e) {
									
								
							}
							
							@Override
							public void mouseExited(MouseEvent e) {
								 datel.setBackground(csh.jf.getBackground()); // 恢复为默认背景颜色
							      datel.setForeground(new Color(0, 0, 0)); // 恢复为默认前景颜色
								
							}
							
							@Override
							public void mouseEntered(MouseEvent e) {
								datel.setBackground(new Color(255, 99, 71));
								datel.setForeground(Color.white);
								
							}
							
							@Override
							public void mouseClicked(MouseEvent e) {
			    				datel.setBackground(new Color(255, 99, 71));
								datel.setForeground(Color.white);
					            // 隐藏当前面板
					            if (currentPanel != null) {
					                currentPanel.setVisible(false);
					            }
//					            jl20.setVisible(false);
//					    	    jl20 = ;
								// 设置文本内容
					    	    jl20.setText(month+"月"+day+"日");
								// 设置字体 Font.PLAIN：普通字体
					    	    jl20.setFont(font2);
					    	    jl20.setForeground(new Color(255, 99, 71));//番茄色
					    	    jl20.setBounds(120, 440, 100, 40);
					    	    csh.jf.getContentPane().add(jl20);
					    	    
					            
								try {
									Statement stmt3= (Statement) conn.createStatement();
									ResultSet rs3 = (ResultSet) stmt3.executeQuery(sql3);
									 // 创建新面板
						            JPanel newPanel = new JPanel();
						            newPanel.setLayout(null);
									int j=0;
									while(rs3.next()) {
											
											//时间
											JLabel jl13 = new JLabel();
											// 设置文本内容
											String time = rs3.getString(5);
											jl13.setText(time);
											// 设置字体 Font.PLAIN：普通字体
											jl13.setFont(font2);
											jl13.setForeground(new Color(0,0, 0));//黑色
											jl13.setBounds(220, 540+j*50, 100, 40);
											csh.jf.getContentPane().add(jl13);
											
											//影厅
											JLabel jl14 = new JLabel();
											// 设置文本内容
											jl14.setText(rs3.getString(2));
											// 设置字体 Font.PLAIN：普通字体
											jl14.setFont(font2);
											jl14.setForeground(new Color(0,0, 0));//黑色
											jl14.setBounds(506, 540+j*50, 100, 40);
											csh.jf.getContentPane().add(jl14);
											
											//价格
											JLabel jl15 = new JLabel();
											// 设置文本内容
											jl15.setText(rs3.getString(15));
											// 设置字体 Font.PLAIN：普通字体
											jl15.setFont(font2);
											jl15.setForeground(new Color(0,0, 0));//黑色
											jl15.setBounds(770, 540+j*50, 100, 40);
											csh.jf.getContentPane().add(jl15);
											
											//立即购票
											JButton jl16 = new JButton();
											// 设置文本内容
											jl16.setText("立即购票");
											// 设置字体 Font.PLAIN：普通字体
											jl16.setFont(font2);
											jl16.setForeground(new Color(255,255, 255));//黑色
											jl16.setBackground(new Color(255, 99, 71));
											jl16.setBounds(1000, 545+j*50, 100, 30);
											String name = rs3.getString(7);
											int fare = rs3.getInt(15);
											int id = rs3.getInt(6);
											String movieroom = rs3.getString(2);
											
								            java.io.InputStream imgStream = rs.getBinaryStream("img");
//									          使用 ImageIO 类的静态方法 read 从输入流中读取图像数据，并返回 BufferedImage 对象
									            BufferedImage image = ImageIO.read(imgStream);
//									           将 BufferedImage 对象转换为 ImageIcon 对象，以便将其用于 JLabel 对象的图像显示。
									            ImageIcon img = new ImageIcon(image);
											
											
											jl16.addMouseListener(new MouseListener() {
												
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
														new XuanZuo(name,time,date,img,fare,id,movieroom);
													} catch (Exception e1) {
														// TODO 自动生成的 catch 块
														e1.printStackTrace();
													}
												}
											});
											
											csh.jf.getContentPane().add(jl16);
											
											
											//横线
								    		JPanel jp2 = new JPanel();
								    		jp2.setBackground(new Color(166, 166, 166));//黑色
								    		jp2.setBounds(120, 538+j*50, 1150, 1);
								    		csh.jf.getContentPane().add(jp2);
											j++;
											
											 newPanel.add(jl13);
								                newPanel.add(jl14);
								                newPanel.add(jl15);
								                newPanel.add(jl16);
								                newPanel.add(jp2);
								                newPanel.add(jl20);
								                // 将新面板加入主容器
								                csh.jf.getContentPane().add(newPanel);
								                newPanel.setBounds(0, 0, 1440, 900);
								                // 更新currentPanel
								                currentPanel = newPanel;

								    		// 强制重绘组件
								    		csh.jf.getContentPane().repaint();
//								    		csh.jf.setVisible(true);
									}
								} catch (Exception e2) {
									System.out.println("SQL异常：" + e2.getMessage());
								}


							}
						});

		    		}
		    		// 在所有标签添加完毕后，刷新组件
		    		csh.jf.getContentPane().repaint();
		    		

	}

	public GouPiao() throws Exception {
		new GouPiao(text);
	}

	
}
