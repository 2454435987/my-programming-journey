package gerenzhuye;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import shouye.ShouYe;
import shujukulianjie.Mysql;

//个人中心
public class GeRenZhongXin {
	private static String user;

	static int ye=0; //0为个人信息1为我的订单

	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		GeRenZhongXin.user = user;
	}
	 Font font1 = new Font("微软雅黑", Font.PLAIN, 22);
     Font font2 = new Font("微软雅黑", Font.PLAIN, 16);
     Font font3 = new Font("微软雅黑", Font.PLAIN, 20);
	ChushihuaCaidan csh = new ChushihuaCaidan();
	Connection conn = (Connection) Mysql.getConnection();
	
	public GeRenZhongXin() throws Exception {

		// 初始化界面
		csh.initJFrame();

		// 初始化菜单
		csh.initJMenuBar();

		//显示搜索
		csh.sousuo();
		
		//设置整个容器的颜色为白色
		csh.jf.getContentPane().setBackground(new Color(255,255,255));
		
		
		String sql = "SELECT * from orders \r\n" + 
				"JOIN movie ON orders.movieId = movie.Id \r\n" + 
				"where user='"+getUser()+"'";
		ResultSet rs=null;
		if(getUser()!=null) {
			Statement stamt = (Statement) conn.createStatement();
			 rs= stamt.executeQuery(sql);
		}

		int i=0;

		
			// 灰色部分
			JPanel jp = new JPanel();
			jp.setBounds(250, 0, 200, 900);
			jp.setBackground(new Color(238, 238, 238));
			// 使用自由布局
			jp.setLayout(null);
//			csh.jf.getContentPane().add(jp);
			
			JLabel jl1 = new JLabel("个人中心");
			jl1.setBounds(55, 80, 100, 40);
			jl1.setFont(font1);
			
			JLabel jl2 = new JLabel("个人信息");
			// 文字水平居中
			jl2.setHorizontalAlignment(JLabel.CENTER);
			jl2.setBounds(0, 140, 200, 60);
			jl2.setFont(font3);
			jl2.setOpaque(true);
			

			JLabel jl3 = new JLabel("我的订单");
			// 文字水平居中
			jl3.setHorizontalAlignment(JLabel.CENTER);
			jl3.setBounds(0, 200, 200, 60);
			jl3.setFont(font3);
			jl3.setOpaque(true);

			jl2.setBackground(new Color(255, 99, 71));// 番茄色
			jl2.setForeground(Color.white);
			// 灰色的线
			JLabel jl5 = new JLabel();
			jl5.setBounds(500, 140, 800, 1);
			jl5.setOpaque(true);
			jl5.setForeground(new Color(255, 99, 71));
			csh.jf.getContentPane().add(jl5);

			// 个人信息部分
			JLabel jl4 = new JLabel("个人信息");
			// 文字水平居中
			jl4.setHorizontalAlignment(JLabel.CENTER);
			jl4.setBounds(500, 80, 100, 40);
			jl4.setFont(font3);
			jl4.setForeground(new Color(255, 99, 71));
			csh.jf.getContentPane().add(jl4);

			JLabel tcdl = new JLabel("退出登录");
			// 文字水平居中
			tcdl.setHorizontalAlignment(JLabel.CENTER);
			tcdl.setBounds(1200, 80, 100, 40);
			tcdl.setFont(font2);
			tcdl.setForeground(new Color(255, 99, 71));
			tcdl.addMouseListener(new MouseListener() {
				
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
					tcdl.setForeground(new Color(0,0,0));
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					tcdl.setForeground(new Color(255, 99, 71));
					// TODO 自动生成的方法存根
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					csh.setFlag2();
					csh.jf.dispose();
					try {
						new ShouYe();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			});
			
			csh.jf.getContentPane().add(tcdl);
			ImageIcon icon = new ImageIcon("img/tubiao/头像.png");
			// 创建一个JLabel的对象（管理容器）
			JLabel jl6 = new JLabel(icon);
			// 指定容器的大小和图片位置(x,y,width,height)
			jl6.setBounds(500, 200, 200, 200);
			// 把图片等比缩放到width为159px，height为227px的大小
			csh.imgchuli(icon, jl6);
			csh.jf.getContentPane().add(jl6);
			
			JLabel nc = new JLabel("昵称：");
			nc.setBounds(800, 200, 100, 40);
			nc.setFont(font3);
			csh.jf.getContentPane().add(nc);
			
			//获取数据库的用户表数据
			String sql3 = "SELECT * from public where user="+getUser()+"";
			ResultSet rs3 = Mysql.query(sql3);
			
			String pname="user123";
			if(rs3.next()) {
				pname = rs3.getString(3);
			}
			//指针回移动一行
			rs3.previous();
		    // 创建文本框并设置初始值
		    JTextField txtnc = new JTextField(pname);
		    txtnc.setFont(font3);
		    txtnc.setBounds(870, 200, 250, 40); // 设置文本框的位置和大小
		    csh.jf.getContentPane().add(txtnc);
			
			JLabel sexl = new JLabel("性别：");
			sexl.setBounds(800, 250, 100, 40);
			sexl.setFont(font3);
			csh.jf.getContentPane().add(sexl);
			


		    // 创建单选按钮
			JRadioButton jrr1 = new JRadioButton("男");
		    jrr1.setFont(font2);
		    jrr1.setBounds(870, 250, 50, 40); // 设置单选按钮的位置和大小
		    jrr1.setBackground(new Color(255,255,255));
		    JRadioButton jrr2 = new JRadioButton("女");
		    jrr2.setFont(font2);
		    jrr2.setBackground(new Color(255,255,255));
		    jrr2.setBounds(920, 250, 50, 40); // 设置单选按钮的位置和大小
		    
		    // 创建按钮组并将单选按钮添加到其中
		    ButtonGroup genderGroup = new ButtonGroup();
		    genderGroup.add(jrr1);
		    genderGroup.add(jrr2);
		    
		   
		    csh.jf.getContentPane().add(jrr1);
		    csh.jf.getContentPane().add(jrr2);
		    
			//用来判断男女
			String sex="男";
		    if(rs3.next()) {
		       sex=rs3.getString(4);
		    }
		    if(sex.equals("男")) {
		    	jrr1.setSelected(true);
		    }else {
		    	jrr2.setSelected(true);
		    }
			//指针回移动一行
			rs3.previous();


		    //生日
		    JLabel birth = new JLabel("生日：");
			birth.setBounds(800, 300, 100, 40);
			birth.setFont(font3);
			csh.jf.getContentPane().add(birth);
			
		    // 创建下拉框并设置选项
		    String[] years = {"1990", "1991", "1992", "1993", "1994","1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023"}; // 年份选项
		    String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}; // 月份选项
		    String[] days = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}; // 日选项
		    JComboBox<String> yearBox = new JComboBox<>(years);
		    JLabel yearl = new JLabel("年");
		    JComboBox<String> monthBox = new JComboBox<>(months);
		    JLabel monthl = new JLabel("月");
		    JComboBox<String> dayBox = new JComboBox<>(days);
		    JLabel dayl = new JLabel("日");
		    yearBox.setFont(font2);
		    yearl.setFont(font3);
		    monthBox.setFont(font2);
		    monthl.setFont(font3);
		    dayBox.setFont(font2);
		    dayl.setFont(font3);
		    
		    SimpleDateFormat sdYear = new SimpleDateFormat("yyyy"); // 格式化为年份
		    SimpleDateFormat sdMonth = new SimpleDateFormat("MM"); // 格式化为月份
    		SimpleDateFormat sdDay = new SimpleDateFormat("dd"); // 格式化为天
    		Date dated=null;
    		if(rs3.next()) {
    			dated = rs3.getDate(5); // 获取日期
    			String yeard = sdYear.format(dated); // 提取年份
    		    String monthd = sdMonth.format(dated); // 提取月份
    		    String dayd = sdDay.format(dated); // 提取
    		    
    		    yearBox.setSelectedItem(yeard);
    		    monthBox.setSelectedItem(monthd);
    		    dayBox.setSelectedItem(dayd);
    		}
    		//指针回移动一行
			rs3.previous();
			

		    
		    //设置背景色
		    yearBox.setBackground(new Color(255,255,255));
		    monthBox.setBackground(new Color(255,255,255));
		    dayBox.setBackground(new Color(255,255,255));
		 
		    
		    csh.jf.getContentPane().add(yearl);
		    csh.jf.getContentPane().add(monthl);
		    csh.jf.getContentPane().add(dayl);
		    // 将下拉框添加到窗体中
		    csh.jf.getContentPane().add(yearBox);
		    csh.jf.getContentPane().add(monthBox);
		    csh.jf.getContentPane().add(dayBox);

		    // 设置下拉框的位置和大小
		    yearBox.setBounds(870, 300, 80, 40);
		    yearl.setBounds(960, 300, 50, 40);//年
		    monthBox.setBounds(1000, 300, 80, 40);
		    monthl.setBounds(1090, 300, 50, 40);//月
		    dayBox.setBounds(1130, 300, 80, 40);
		    dayl.setBounds(1220, 300, 80, 40);//日

			
		    //个性签名
		    JLabel Signaturel = new JLabel("个性签名：");
		    Signaturel.setBounds(760, 350, 100, 40);
		    Signaturel.setFont(font3);
			csh.jf.getContentPane().add(Signaturel);
		    
			String txtSignature="这个人很懒";
			if(rs3.next()) {
				txtSignature=rs3.getString(6);
			}
			//指针回移动一行
			rs3.previous();
		    // 创建文本框并设置初始值
		    JTextField txtSignaturel = new JTextField(txtSignature);
		    txtSignaturel.setFont(font3);
		    txtSignaturel.setBounds(870, 350, 500, 40); // 设置文本框的位置和大小
		    csh.jf.getContentPane().add(txtSignaturel);
		    
		    //保存按钮
		    JButton bc = new JButton("保存");
		    bc.setFont(font3);
		    bc.setBounds(870, 430, 100, 40);
		    bc.setBackground(new Color(255, 99, 71));
		    bc.setForeground(new Color(255, 255, 255));
		    csh.jf.getContentPane().add(bc);
		    
		  
		   
		    bc.addMouseListener(new MouseListener() {
				
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
				    //获取昵称
				    String getname = txtnc.getText();
				    //获取性别
				 // 判断 jrr1 是否被选中
				    char getsex=jrr1.isSelected()?'男':'女';
				    //获取年月日
				    String selectedYear = (String) yearBox.getSelectedItem();
				    String selectedMonth = (String) monthBox.getSelectedItem();
				    String selectedDay = (String) dayBox.getSelectedItem();
				    //获取个性签名
				    String gxqm = txtSignaturel.getText();
					  String up="UPDATE public SET name = '"+getname+"', sex = '"+getsex+"', birthday = '"+selectedYear+"-"+selectedMonth+"-"+selectedDay+"', Signature = '"+gxqm+"' WHERE user='"+getUser()+"'";
					 boolean flag =Mysql.update(up);
					 if(flag) {
						 JOptionPane.showMessageDialog(null, "保存成功！");
					 }else {
						 JOptionPane.showMessageDialog(null, "保存失败！");
						 
					 }
				}
			});
		    
		    
		    
			JLabel jp11 = new JLabel();
			// 使用自由布局
			jp11.setLayout(null);
			jp11.setBounds(580,220,1000,4000);
			jp11.setBackground(new Color(255,255,255));

			// 我的订单部分
			JLabel jl7 = new JLabel("我的订单");
			// 文字水平居中
			jl7.setHorizontalAlignment(JLabel.CENTER);
			jl7.setBounds(500, 80, 100, 40);
			jl7.setFont(font3);
			jl7.setForeground(new Color(255, 99, 71));
			csh.jf.getContentPane().add(jl7);
			hide(jl7);
			
			if(user!=null) {
				while (rs.next()) {
					int Ordernumber = rs.getInt(2);
					String sql2="SELECT * from odrseat WHERE Ordernumber="+Ordernumber+";";
					
					Statement stamt2 = (Statement) conn.createStatement();
					ResultSet rs2 = stamt2.executeQuery(sql2);
					int j=0;
					while(rs2.next()) {
						JLabel jl11 = new JLabel(rs2.getString(2)+"排"+rs2.getString(3)+"座");
						// 文字水平居中
						jl11.setHorizontalAlignment(JLabel.CENTER);
						jl11.setBounds(j*80, 120*i, 100, 40);
						jl11.setFont(font2);
						jp11.add(jl11);
						j++;
					}
//					csh.jf.getContentPane().setComponentZOrder(jp11, 0);
					csh.jf.getContentPane().add(jp11);

				
				// 总价格
				int sumprice = rs.getInt(5);
				String name = rs.getString(9);
				// 影厅
				String moiveroom = rs.getString(6);
				// 订单编号
				String ordernum = rs.getString(2);
				//放映日期
				Date date = rs.getDate(3); // 获取日期
				// 放映时间
				String time = rs.getString(4);
			
				// 总价格
				JLabel jl8 = new JLabel("总票价:" +sumprice+"元");
				// 文字水平居中
				jl8.setHorizontalAlignment(JLabel.CENTER);
				jl8.setBounds(1200, 180+120*i, 100, 40);
				jl8.setFont(font2);
				jl8.setForeground(new Color(255, 99, 71));
				csh.jf.getContentPane().add(jl8);

				//电影名

				JLabel jl9 = new JLabel(name);
				// 文字水平居中
//				jl9.setHorizontalAlignment(JLabel.CENTER);
				jl9.setBounds(520, 180+120*i, 300, 40);
				jl9.setFont(font2);
				csh.jf.getContentPane().add(jl9);
				
				//影厅
				JLabel jl10 = new JLabel(moiveroom+"号厅");
				// 文字水平居中
				jl10.setHorizontalAlignment(JLabel.CENTER);
				jl10.setBounds(680, 180+120*i, 100, 40);
				jl10.setFont(font2);
				csh.jf.getContentPane().add(jl10);
				
				//座位号
				JLabel jl14 = new JLabel("座位号：");
				// 文字水平居中
				jl14.setHorizontalAlignment(JLabel.CENTER);
				jl14.setBounds(500, 220+120*i, 100, 40);
				jl14.setFont(font2);
				csh.jf.getContentPane().add(jl14);
				

				
				//订单编号
				JLabel jl13 = new JLabel("订单编号："+ordernum);
				// 文字水平居中
				jl13.setHorizontalAlignment(JLabel.CENTER);
				jl13.setBounds(1040, 180+120*i, 200, 40);
				jl13.setFont(font2);
				csh.jf.getContentPane().add(jl13);
				
	    		SimpleDateFormat sdfMonth = new SimpleDateFormat("M"); // 格式化为月份
	    		SimpleDateFormat sdfDay = new SimpleDateFormat("dd"); // 格式化为天
	    		
	    		
				//放映时间
			    String month = sdfMonth.format(date); // 提取月份
			    String day = sdfDay.format(date); // 提取天
				
				JLabel jl12 = new JLabel("放映日期："+month+"月"+day+"日   时间："+ time);
				// 文字水平居中
				jl12.setHorizontalAlignment(JLabel.CENTER);
				jl12.setBounds(720, 180+120*i, 400, 40);
				jl12.setFont(font2);
				
				i++;
				csh.jf.getContentPane().add(jl12);
				
				
				jl2.addMouseListener(new MouseListener() {

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
//						if (ye == 1) {
//							jl2.setBackground(new Color(238, 238, 238));
//							jl2.setForeground(Color.black);
//						}

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// 划入
//						if (ye == 1) {
//							jl2.setBackground(new Color(255, 99, 71));
//							jl2.setForeground(Color.white);
//						}

					}

					@Override
					public void mouseClicked(MouseEvent e) {
//						ye = 0;
//						jl2.setBackground(new Color(255, 99, 71));// 番茄色
//						jl2.setForeground(Color.white);
//						jl3.setBackground(new Color(238, 238, 238));
//						jl3.setForeground(new Color(0, 0, 0));
						hide(jl7, jl8,jl9,jl10,jp11,jl12,jl13,jl14);
						show(jl4, jl6,nc,txtnc,sexl,jrr1,jrr2,birth,yearl,monthl,dayl,yearBox,monthBox,dayBox,Signaturel,txtSignaturel,bc);
					}
				});

				jl3.addMouseListener(new MouseListener() {

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


					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// 划入


					}

					@Override
					public void mouseClicked(MouseEvent e) {
						hide(jl4, jl6,nc,txtnc,sexl,jrr1,jrr2,birth,yearl,monthl,dayl,yearBox,monthBox,dayBox,Signaturel,txtSignaturel,bc);
						show(jl7, jl8,jl9,jl10,jp11,jl12,jl13,jl14);
					}
				});


				hide(jl7, jl8,jl9,jl10,jp11,jl12,jl13,jl14);
			}
			}
			jl2.addMouseListener(new MouseListener() {

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
					if (ye == 1) {
						jl2.setBackground(new Color(238, 238, 238));
						jl2.setForeground(Color.black);
					}

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					if (ye == 1) {
						jl2.setBackground(new Color(255, 99, 71));
						jl2.setForeground(Color.white);
					}

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					ye = 0;
					jl2.setBackground(new Color(255, 99, 71));// 番茄色
					jl2.setForeground(Color.white);
					jl3.setBackground(new Color(238, 238, 238));
					jl3.setForeground(new Color(0, 0, 0));
					hide(jl7);
					show(jl4, jl6,nc,txtnc,sexl,jrr1,jrr2,birth,yearl,monthl,dayl,yearBox,monthBox,dayBox,Signaturel,txtSignaturel,bc);
				}
			});

			jl3.addMouseListener(new MouseListener() {

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
					if (ye == 0) {
						jl3.setBackground(new Color(238, 238, 238));
						jl3.setForeground(Color.black);
					}

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					if (ye == 0) {
						jl3.setBackground(new Color(255, 99, 71));
						jl3.setForeground(Color.white);

					}

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					ye = 1;
					jl3.setBackground(new Color(255, 99, 71));
					jl3.setForeground(Color.white);
					jl2.setBackground(new Color(238, 238, 238));
					jl2.setForeground(new Color(0, 0, 0));
					hide(jl4, jl6,nc,txtnc,sexl,jrr1,jrr2,birth,yearl,monthl,dayl,yearBox,monthBox,dayBox,Signaturel,txtSignaturel,bc);
					show(jl7);
				}
			});
			
			
			
			jp.add(jl1);
			jp.add(jl2);
			jp.add(jl3);
			csh.jf.getContentPane().add(jp);
		// 让显示显示出来，建议写在最后面
		csh.jf.setVisible(true);
		
		//设置关闭模式
		csh.jf.setDefaultCloseOperation(2);
	}
	//隐藏控件
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
