package goupiao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import gerenzhuye.GeRenZhongXin;
import shujukulianjie.Mysql;

public class XuanZuo {
	ChushihuaCaidan csh = new ChushihuaCaidan();
	
	Connection conn = (Connection) Mysql.getConnection();
	 Font font1 = new Font("微软雅黑", Font.PLAIN, 22);
     Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
     Font font3 = new Font("微软雅黑", Font.PLAIN, 20);
     int n=0;
     int index1=1,index2=0;
     int[][] linerow = new int[6][9];
     int rownumber = 1;
     static int sumfare=0;
	public XuanZuo() {
		
	}
	public XuanZuo(String name,String time,Date date,ImageIcon img,int fare,int id,String movieroom) throws Exception {
		// 初始化界面
		csh.initJFrame();

		// 初始化菜单
		csh.initJMenuBar();

		
		//显示搜索
		csh.sousuo();
		
		//购票部分
		JPanel jp = new JPanel();
		jp.setBackground(Color.WHITE);
		jp.setLayout(null);
//		jp.setPreferredSize(new Dimension(486, 619));
		jp.setBounds(80,100,486, 619);
		
        JLabel label = new JLabel(img);
        label.setBounds(115,140,189, 256);
        csh.imgchuli(img,label);
        csh.jf.getContentPane().add(label);
		
        //名字
        JLabel jl1 = new JLabel(name);
        jl1.setBounds(244,50,100, 40); 
        jl1.setFont(font1);
        csh.jf.getContentPane().add(jl1);
        
        //电影时间
        JLabel datel = new JLabel("电影日期:" + date);
        datel.setBounds(244,100,200, 40); 
        datel.setFont(font2);
        csh.jf.getContentPane().add(datel);
        
        //电影时间
        JLabel jl2 = new JLabel("电影时间:" + time);
        jl2.setBounds(244,140,200, 40); 
        jl2.setFont(font2);
        csh.jf.getContentPane().add(jl2);
        
        //票价
        JLabel jl3 = new JLabel("票价：");
        jl3.setBounds(244,175,100, 40); 
        jl3.setFont(font2);
        csh.jf.getContentPane().add(jl3);
        
        //价格
        
        JLabel jl4 = new JLabel(0 + "元");
        jl4.setBounds(290,175,100, 40); 
        jl4.setFont(font2);
        jl4.setForeground(new Color(255, 99, 71));
        csh.jf.getContentPane().add(jl4);
        
        //提示
        JLabel jl5 = new JLabel("请点击右侧作为图片进行选座");
        jl5.setBounds(132,300,300, 40); 
        jl5.setFont(font2);
        csh.jf.getContentPane().add(jl5);
        
		JButton gp = new JButton("立即购票");
		gp.setFont(font3);
		gp.setForeground(new Color(255,255,255));//白色
		gp.setBounds(137, 360, 193, 40);
		gp.setBackground(new Color(255, 99, 71));//番茄色
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
				int f=0;
	    		//获取最后一行的Ordernum订单编号
	    		String sql = "SELECT * from orders ORDER BY Ordernum DESC LIMIT 1";
	    		Statement sta;
	    		int Ordernum=0;
				try {
					sta = (Statement) conn.createStatement();
					ResultSet rs = (ResultSet) sta.executeQuery(sql);
		    		
		    		if(rs.next()) {
		    			Ordernum = rs.getInt(2);
		    		}
		    		Ordernum+=1;
				} catch (SQLException e2) {
					// TODO 自动生成的 catch 块
					e2.printStackTrace();
				}

				
				// 使用嵌套循环遍历数组
				for (int i = 0; i < linerow.length; i++) {
				    for (int j = 0; j < linerow[i].length; j++) {
				    	if(linerow[i][j]!=0) {

				    		String up = "UPDATE seat SET flag = 1 WHERE line="+i+" and row="+linerow[i][j]+" and rownumber="+rownumber+"";
								
				    		try {
				    			Statement stmt = (Statement) conn.createStatement();
				    			f = stmt.executeUpdate(up);
				    			String up2 = "INSERT INTO orders(movieid,Ordernum,showtime,showtimes,seatnum,Screeningroom,USER) VALUES\r\n" + 
					    				"("+id+","+Ordernum+",'"+date+"','"+time+"',"+sumfare+",'"+movieroom+"','"+GeRenZhongXin.getUser()+"')";
				    			Statement stmt2 = (Statement) conn.createStatement();
				    			stmt2.executeUpdate(up2);
				    			System.out.println(stmt2.executeUpdate(up2));
							} catch (Exception e2) {
								System.out.println("跳过执行");
								System.out.println(e2);
							}finally {
								
							}
				    	}
				    }
				}
				// 使用嵌套循环遍历数组
				for (int i = 0; i < linerow.length; i++) {
				    for (int j = 0; j < linerow[i].length; j++) {
				    	if(linerow[i][j]!=0) {

				    		
				    		String up3 = "INSERT INTO odrseat(line,row,ordernumber) VALUES ("+i+","+linerow[i][j]+","+Ordernum+");";
				    		try {
							Statement stmt3 = (Statement) conn.createStatement();
							stmt3.executeUpdate(up3);
							System.out.println(up3);
//					    		System.out.println(up2);
								
							} catch (SQLException e1) {
//								System.out.println("不执行！");
								e1.printStackTrace();
							}finally {
								
							}
				    		System.out.print(i+","+linerow[i][j] + " ");
				    	}
				    }
				}
				
				
				
				if(f>0) {
					JOptionPane.showMessageDialog(null, "购买成功！");
					Ordernum++;
				    // 关闭当前窗口
				    csh.jf.dispose();
					try {
						new XuanZuo(name,time,date,img,fare,id,movieroom);
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
		});
		
		csh.jf.getContentPane().add(gp);
        

		
		// 添加控件到JPanel
		jp.add(jl1);
		jp.add(datel);
		jp.add(jl2);
		jp.add(jl3);
		jp.add(jl4);
		jp.add(jl5);
		jp.add(gp);

		
		//座位部分
		JPanel jp2 = new JPanel();
		jp2.setBackground(new Color(255,255,255));
		jp2.setLayout(null);
		jp2.setBounds(616,100,737, 619);
		
		//已售座位
        JLabel jl6 = new JLabel();
        jl6.setBounds(40,10,40, 40); 
        //设置为不透明的
        jl6.setOpaque(true);
        //设置边框
        jl6.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jl6.setBackground(new Color(255, 0,0 ));//红色
        csh.jf.getContentPane().add(jl6);
		
        //提示
        JLabel jl7 = new JLabel("已售座位");
        jl7.setBounds(90,10,150, 40); 
        jl7.setFont(font3);
        csh.jf.getContentPane().add(jl7);
        
		//未售座位
        JLabel jl8 = new JLabel();
        jl8.setBounds(240,10,40, 40); 
        //设置为不透明的
        jl8.setOpaque(true);
        //设置边框
        jl8.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        csh.jf.getContentPane().add(jl8);
		
        //提示
        JLabel jl9 = new JLabel("未售座位");
        jl9.setBounds(290,10,150, 40); 
        jl9.setFont(font3);
        csh.jf.getContentPane().add(jl9);
        
		//电影银幕
        JLabel jl10 = new JLabel();
        jl10.setBounds(40,55,650, 30); 
        //设置为不透明的
        jl10.setOpaque(true);
        //设置边框
        jl10.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        csh.jf.getContentPane().add(jl10);
        
        //提示
        JLabel jl11 = new JLabel("电影银幕");
        jl11.setBounds(340,85,650, 30); 
        jl11.setFont(font3);
        csh.jf.getContentPane().add(jl11);
        
        for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				//判断已售的座位
				String sql = "SELECT * from seat where rownumber=(SELECT rownumber FROM filmarrangement where movieid="+id+" and Showtime='"+date+"' and Showtimes='"+time+"')";
				String sql2 = "SELECT line,row from seat WHERE line="+(i+1)+" and row="+(j+1)+"";
				Statement stmt= (Statement) conn.createStatement();
				ResultSet rs = (ResultSet) stmt.executeQuery(sql);
				
		        JLabel jl12 = new JLabel();
		        jl12.setBounds(40+76*j,125+76*i,40, 40); 
		        //设置为不透明的
		        jl12.setOpaque(true);
		        //设置边框
		        jl12.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		        while(rs.next()) {
		        	rownumber = rs.getInt(5);
		        	//获取行
		        	int line = rs.getInt(2);
		        	//获取列
		        	int row = rs.getInt(3);
		        	//获取flag值
		        	int flag = rs.getInt(4);
		        	if(line==i+1&&row==j+1&&flag==1) {
		        		jl12.setBackground(new Color(255,0,0));
		        	}
		        	
		        }
		        
		        jl12.addMouseListener(new MouseListener() {
					
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
//						System.out.println(sql2);
						ResultSet rs2 = null;
						try {
							Statement stmt2= (Statement) conn.createStatement();
							rs2 = (ResultSet) stmt2.executeQuery(sql2);
						} catch (SQLException e2) {
							// TODO 自动生成的 catch 块
							e2.printStackTrace();
						}
						//判断当前颜色是否已经改变
				        if (jl12.getBackground().equals(new Color(238, 238, 238))) {
				        	try {
				        		

								if(rs2.next()) {
									//获取行
									int line = rs2.getInt(1);
									//获取列
									int row = rs2.getInt(2);
//									index1=line;
									linerow[line][row-1] = row;
//									System.out.println(line+","+linerow[line][row-1]);

									// 使用嵌套循环遍历数组
//									for (int i = 0; i < linerow.length; i++) {
//									    for (int j = 0; j < linerow[i].length; j++) {
//									    	if(linerow[i][j]!=0) {
//									    		System.out.print(i+","+linerow[i][j] + " ");
//									    	}
//									    }
//									    System.out.println(); // 换行
//									}
								}
							} catch (SQLException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
								System.out.println("未执行");
							}

				            //如果没有改变，则改变颜色为红色
				            jl12.setBackground(new Color(0, 195, 22));
				            
				            n++;
//				            System.out.println(n);
				        } else if(jl12.getBackground().equals(new Color(0, 195, 22))){
				            //如果已经改变，则恢复原来的颜色
				            jl12.setBackground(new Color(238, 238, 238));
				            try {
								if(rs2.next()) {
									//获取行
									int line = rs2.getInt(1);
									int row = rs2.getInt(2);
						            linerow[line][row-1]=0;
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

				            
	
				            n--;
				        }
				        sumfare = fare*n ;	
						jl4.setText(sumfare + "元");
					}
				});
		        
		        csh.jf.getContentPane().add(jl12);
		        jp2.add(jl12);
			}
		}
        
        jp2.add(jl6);
        jp2.add(jl7);
        jp2.add(jl8);
        jp2.add(jl9);
        jp2.add(jl10);
        jp2.add(jl11);
		csh.jf.getContentPane().add(jp);
		csh.jf.getContentPane().add(jp2);
		// 让显示显示出来，建议写在最后面
		csh.jf.setVisible(true);
		
		//设置关闭模式
		csh.jf.setDefaultCloseOperation(2);
		
	}
}
