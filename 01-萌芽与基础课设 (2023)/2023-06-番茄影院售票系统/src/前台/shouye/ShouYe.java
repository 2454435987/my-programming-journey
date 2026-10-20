package shouye;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import chuangkou_caidan_imgchuli.ChushihuaCaidan;
import shujukulianjie.Mysql;
import yingpianzixun.Yingpianchaxun;
import yingpianzixun.Yingpianzixun;

//首页
public class ShouYe {
	ChushihuaCaidan csh = new ChushihuaCaidan();
	public ShouYe() throws Exception {
		// 初始化界面
		csh.initJFrame();

		// 初始化菜单
		csh.initJMenuBar();

		// 初始化正在热映标签  
		initJTitle();

		// 初始化图片
		initImage();
		
		//今日票房
		JinRiPiaoFang();
		
		//显示搜索
		csh.sousuo();
		
		// 让显示显示出来，建议写在最后面
		csh.jf.setVisible(true);
		
		//设置关闭模式
		csh.jf.setDefaultCloseOperation(2);
	}
	//今日票房
	public void JinRiPiaoFang() throws Exception {
		Yingpianchaxun yp = new Yingpianchaxun();
//		连接数据库并运行sql语句
		Connection conn = (Connection) Mysql.getConnection();
		String sql="SELECT name from movie where id=?";
		String sql2="SELECT hot from movie where id=?";
		PreparedStatement pstatm = (PreparedStatement) conn.prepareStatement(sql);
		PreparedStatement pstatm2 = (PreparedStatement) conn.prepareStatement(sql2);
		ResultSet rs = null;
		//循环输出12个票房内容
		for (int i = 0; i < 10; i++) {
			//创建一个容器
			JPanel jp = new JPanel();
			jp.setBounds(850,140+(68*i),450,59);
			
			//创建一个容器放入文字
			JLabel jl1 = new JLabel();
			jl1.setText(i+1+" ");
			// 设置字体 Font.PLAIN：普通字体
			Font font = new Font("微软雅黑", Font.PLAIN, 25);
			jl1.setFont(font);
			//设置前景色（字体颜色）
			if(i<3) {
				jl1.setForeground(new Color(255, 99, 71));//番茄色
			}else if(i<6) {
				jl1.setForeground(new Color(255, 99, 71,128));//半透明番茄色
			}else if(i<9) {
				jl1.setForeground(new Color(148, 148, 144));//灰色
			}else {
				jl1.setForeground(new Color(148, 148, 144,128));//半透明灰色
				
			}

			//创建一个容器放入文字
			JLabel jl2 = new JLabel();
//			把i的值给sql中第一个问号
			pstatm.setInt(1, i+1);
			//执行sql语句
			rs = (ResultSet) pstatm.executeQuery();
			
			while(rs.next()) {
				final String name = rs.getString(1);
				jp.addMouseListener(new MouseListener() {
					
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
						jp.setBackground(null);
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						
						jp.setBackground(new Color(255,255,255));
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							csh.jf.dispose();
							new Yingpianchaxun(name);
						} catch (Exception e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
					}
				});
				jl2.setText(name);
			}
			

			// 设置字体 Font.PLAIN：普通字体
			jl2.setFont(font);
			//设置前景色（字体颜色）
			jl2.setForeground(new Color(0, 0, 0));//黑色
			
			//创建一个容器放入文字
			JLabel jl3 = new JLabel();
			
//			把i的值给sql中第一个问号
			pstatm2.setInt(1, i+1);
			//执行sql语句
			rs = (ResultSet) pstatm2.executeQuery();
			String hot = null;
			while(rs.next()) {
				hot = rs.getString(1);
			}
			jl3.setText("  "+hot+"万");
			Font font3 = new Font("微软雅黑", Font.PLAIN, 20);
			
			// 设置字体 Font.PLAIN：普通字体
			jl3.setFont(font3);
			//设置前景色（字体颜色）
			jl3.setForeground(new Color(255, 99, 71));//番茄色
			jp.add(jl1);jp.add(jl2);jp.add(jl3);
			jl1.setHorizontalAlignment(SwingConstants.LEFT);

			csh.jf.getContentPane().add(jp);
			
		}
		
	}
	
	public void initImage() throws Exception {
		Yingpianchaxun ypcx = new Yingpianchaxun();
		// 创建一个图片ImageIcon的对象
		Connection conn = (Connection) Mysql.getConnection();
		String sql="SELECT name from movie where id=?";
		PreparedStatement pstam = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = null;
		int count = 1;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				
				pstam.setInt(1, count);
				rs = (ResultSet) pstam.executeQuery();
//				"img/shouye_img/1.jpg"
				ImageIcon icon = new ImageIcon("img/shouye_img/" + count + ".jpg");
				// 创建一个JLabel的对象（管理容器）
				JLabel jl = new JLabel(icon);
				// 指定容器的大小和图片位置(x,y,width,height)
//				jl.setBounds(100 + (200 * j), 140 + (280 * i), 159, 227);
				jl.setBounds(120 + (230 * j), 130 + (240 * i), 140, 199);
				// 把图片等比缩放到width为159px，height为227px的大小
				csh.imgchuli(icon, jl);
				while(rs.next()) {
					JLabel jl1 = new JLabel();
					// 设置文本内容
					final String name=rs.getString(1);
					jl1.setText(name);
					Font font = new Font("宋体", Font.PLAIN, 12);
					jl1.setFont(font);
					csh.jf.getContentPane().add(jl1);
					jl1.setBounds(120 + (230 * j), 328 + (240 * i), 140, 25);
					jl1.setOpaque(true);
					jl1.setBackground(Color.white);
					//水平和垂直居中文本
					jl1.setHorizontalAlignment(SwingConstants.CENTER);
					jl1.setVerticalAlignment(SwingConstants.CENTER);
//					
//					JPanel jp = new JPanel();
//					jp.add(jl1);
//					jp.setLayout(null);
//					jp.setOpaque(true);
//					jl.add(jp); // 将渐变色面板添加到JLabel上
					jl1.addMouseListener(new MouseListener() {
						
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
							jl1.setBackground(Color.white);
							jl1.setForeground(Color.black);
							
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							jl1.setBackground(new Color(255, 99, 71));
							jl1.setForeground(Color.white);
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							try {
								csh.jf.dispose();
								new Yingpianchaxun(name);
							} catch (Exception e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
							
						}
					});
					csh.jf.getContentPane().add(jl1);
					csh.jf.getContentPane().add(jl);
				}
	
				count++;
			}
		}

		pstam.close();
		rs.close();
		conn.close();
		
	}

	public void initJTitle() {
		JLabel jl1 = new JLabel();
		// 设置文本内容
		jl1.setText("正在热映");
		// 设置字体 Font.PLAIN：普通字体
		Font font = new Font("微软雅黑", Font.PLAIN, 25);
		jl1.setFont(font);
		jl1.setForeground(new Color(255, 99, 71));//番茄色
		jl1.setBounds(120, 80, 100, 40);
		//将jl1添加到初始容器
		csh.jf.getContentPane().add(jl1);
		
		JLabel jl3 = new JLabel();
		jl3.setText("今日票房");
		// 设置字体 Font.PLAIN：普通字体
		jl3.setFont(font);
		jl3.setForeground(new Color(255, 99, 71));//番茄色
		jl3.setBounds(850, 80, 100, 40);
		//将jl1添加到初始容器
		csh.jf.getContentPane().add(jl3);
		
		

		JLabel jl2 = new JLabel();
		// 设置文本内容
		jl2.setText("查看更多>");
		jl2.setForeground(new Color(255, 99, 71));//番茄色
		csh.jf.getContentPane().add(jl2);
		jl2.setBounds(665, 100, 60, 20);
		jl2.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//取消下边框
				jl2.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				//鼠标悬浮显示下边框
				jl2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 99, 71)));
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ChushihuaCaidan.setYe(1);
				try {
					csh.jf.dispose();
					new Yingpianzixun();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
