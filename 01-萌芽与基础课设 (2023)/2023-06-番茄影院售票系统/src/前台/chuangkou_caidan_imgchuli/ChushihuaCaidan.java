package chuangkou_caidan_imgchuli;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import admindenglu.AdminDengLu;
import denglu.DengLu;
import gerenzhuye.GeRenZhongXin;
import shouye.*;
import yingpianzixun.Yingpianchaxun;
import yingpianzixun.Yingpianzixun;
import zhuce.ZhuCe;

public class ChushihuaCaidan {
	public JFrame jf = new JFrame();
	String text;
	 JLabel jmzc = new JLabel();
	 JLabel jmdl = new JLabel();
	static int flag = 0;
	static JLabel jlm=null;
	public static int getYe() {
		return ye;
	}

	public static void setYe(int ye) {
		ChushihuaCaidan.ye = ye;
	}
	static int ye=0; //0为首页1为资讯页面
	public int getFlag() {
		return flag;
	}
	
	public static void setFlag() {
		flag = 1;
	}
	public static void setFlag2() {
		flag = 2;
	}
	//初始化菜单
		public void initJMenuBar() {
			
			JPanel menuPanel = new JPanel();
			menuPanel.setLayout(null); // 禁用默认的布局管理器
			
			//设置logo
			ImageIcon icon = new ImageIcon("img/logo/1.png");
			// 创建一个JLabel的对象（管理容器）
			JLabel jl = new JLabel(icon);
			// 指定容器的大小和图片位置(x,y,width,height)
			jl.setBounds(100, 0, 100, 60);
			// 把图片等比缩放到width为100px，height为60px的大小
			imgchuli(icon, jl);
			jl.addMouseListener(new MouseListener() {
				
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
					jf.dispose();
					new AdminDengLu();
				}
			});
			
			jf.getContentPane().add(jl);
			
			JLabel fqlogo = new JLabel();
			// 设置文本内容
			fqlogo.setText("番茄电影");
			fqlogo.setBounds(180, 0, 100, 60);
			Font fontlogo = new Font("宋体", Font.BOLD, 24);
			fqlogo.setFont(fontlogo);
			fqlogo.setForeground(new Color(255, 99, 71));
			jf.getContentPane().add(fqlogo);
			
			JLabel jmsy = new JLabel();
			// 设置文本内容
			jmsy.setText("首页");


			JLabel jmzx = new JLabel();
			// 设置文本内容
			jmzx.setText("影片资讯");

			
			// 设置文本内容
			jmzc.setText("注册");


			// 设置文本内容
			jmdl.setText("登录");

			// 设置字体 Font.PLAIN：普通字体
			Font font1 = new Font("微软雅黑", Font.PLAIN, 20);
			jmsy.setFont(font1);
			jmzx.setFont(font1);

			Font font2 = new Font("微软雅黑", Font.PLAIN, 14);
			jmzc.setFont(font2);
			jmdl.setFont(font2);
	//
			// 设置背景色和前景色
			Color bgColor = new Color(255, 255, 255); // 白色
			Color fgColor = new Color(0, 0, 0);
			
			if(ye==0) {
				jmsy.setBackground(new Color(255, 99, 71));// 番茄色
				jmsy.setForeground(Color.white);
			}else if(ye==1){
				jmzx.setBackground(new Color(255, 99, 71));
				jmzx.setForeground(Color.white);
				jmsy.setBackground(bgColor);
				jmsy.setForeground(fgColor);
			}
			jmzc.setBackground(bgColor);
			jmdl.setBackground(bgColor);

			jmzc.setForeground(fgColor);
			jmdl.setForeground(fgColor);

			jmsy.setHorizontalAlignment(SwingConstants.CENTER);
			jmzx.setHorizontalAlignment(SwingConstants.CENTER);
			jmzc.setHorizontalAlignment(SwingConstants.CENTER);
			jmdl.setHorizontalAlignment(SwingConstants.CENTER);

			menuPanel.add(jmsy);
			menuPanel.add(jmzx);
			menuPanel.add(jmzc);
			menuPanel.add(jmdl);

			jf.getContentPane().add(jmsy);
			jmsy.setBounds(350, 0, 100, 60);
			jmsy.setOpaque(true);

			jf.getContentPane().add(jmzx);
			jmzx.setBounds(450, 0, 125, 60);
			jmzx.setOpaque(true);

			jf.getContentPane().add(jmzc);
			jmzc.setBounds(1200, 0, 50, 60);
			jmzc.setOpaque(true);

			jf.getContentPane().add(jmdl);
			jmdl.setBounds(1250, 0, 50, 60);
			jmdl.setOpaque(true);

			menuPanel.setBackground(bgColor); // 设置背景色
			menuPanel.setBounds(0, 0, 1920, 60); // 设置菜单面板的位置和大小
			jf.getContentPane().add(menuPanel);

//			给jmdl添加点击事件
			jmdl.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
					// 按下不松
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// 划出
					jmdl.setBackground(Color.white);
					jmdl.setForeground(Color.black);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					jmdl.setBackground(new Color(255, 99, 71));
					jmdl.setForeground(Color.white);

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// 点击
					new DengLu();
					jf.dispose();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// 松开
				}

			});

//			给jmzc添加点击事件
			jmzc.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
					// 按下不松
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// 划出
					jmzc.setBackground(Color.white);
					jmzc.setForeground(Color.black);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					jmzc.setBackground(new Color(255, 99, 71));
					jmzc.setForeground(Color.white);

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					new ZhuCe();
					jf.dispose();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// 松开
				}

			});

//			给jmzx添加点击事件
			jmzx.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
					// 按下不松
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// 划出
					if(ye==0) {
						jmzx.setBackground(Color.white);
						jmzx.setForeground(Color.black);
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					if(ye==0) {
						jmzx.setBackground(new Color(255, 99, 71));
						jmzx.setForeground(Color.white);
					}

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					jf.dispose();
					// 点击
					try {
						ye=1;
						new Yingpianzixun();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// 松开
				}

			});

//			给jmsy添加点击事件
			jmsy.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
					// 按下不松
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// 划出
//					jmsy.setBackground(Color.white);
					if(ye==1) {
						jmsy.setBackground(Color.white);
						jmsy.setForeground(Color.black);
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// 划入
					if(ye==1) {
						jmsy.setBackground(new Color(255, 99, 71));
						jmsy.setForeground(Color.white);
					}

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// 点击
					jf.dispose();
					try {
						ye=0;
						new ShouYe();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// 松开
				}

			});
			if(flag==1) {
				hideJmdl();
				grtx();
			}
		}
	
	//初始化搜索栏
	public void sousuo() throws Exception {
		JLabel img =new JLabel();
		JTextField sousuo=new JTextField();
		sousuo.setBounds(875,15,200,30);
		jf.getContentPane().add(sousuo);
		jf.getContentPane().setComponentZOrder(sousuo, 0);
		sousuo.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        		jf.dispose();
						text = sousuo.getText(); // 获取输入控件里面的文字
						try {
							new Yingpianchaxun(text);
						} catch (Exception e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
						
					

		        	
		            String text = sousuo.getText();
		        }
		    }
		});

		ImageIcon icon = new ImageIcon("img/tubiao/1.png");
		// 创建一个JLabel的对象（管理容器）
		JLabel jl = new JLabel(icon);
		// 指定容器的大小和图片位置(x,y,width,height)
		jl.setBounds(1075 , 5 , 50, 50);
		// 把图片等比缩放到width为50px，height为50px的大小
		imgchuli(icon, jl);
		jl.addMouseListener(new MouseListener() {
			
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
//				点击根据文本框的内容进行查询
					try {
						jf.dispose();
						text = sousuo.getText(); // 获取输入控件里面的文字
						new Yingpianchaxun(text);
						
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
			}
		});
		
		jf.getContentPane().add(jl);
		jf.getContentPane().setComponentZOrder(jl, 0);
		
	}
	
	// 对图片的大小进行等比缩放(图片地址,容器名称)
	public void imgchuli(ImageIcon icon, JLabel jl) {
		// 获取图片的宽度和高度
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		// 计算图片的宽高比例
		double iconRatio = (double) iconWidth / iconHeight;
		// 计算容器的宽高比例
		double containerRatio = (double) jl.getWidth() / jl.getHeight();
		// 比较图片和容器的宽高比例，按比例缩放图片并重新设置图片
		if (iconRatio > containerRatio) {
			int height = (int) (jl.getWidth() / iconRatio);
//				   Image.SCALE_SMOOTH：用于指定图像缩放方式的常量之一，速度慢，质量高，适合要求缩放质量较高的场景
//				    对 icon 对象所持有的图片进行缩放，缩放后的图片替换原来的图片
			icon.setImage(icon.getImage().getScaledInstance(jl.getWidth(), height, Image.SCALE_SMOOTH));
		} else {
			int width = (int) (jl.getHeight() * iconRatio);
			icon.setImage(icon.getImage().getScaledInstance(width, jl.getHeight(), Image.SCALE_SMOOTH));
		}
		jl.setIcon(icon);
	}
	
	//初始化界面
	public void initJFrame() {
		// 创建一个首页主界面设置界面的宽高
		jf.setSize(1440, 900);
		// 设置窗体的最佳大小
		jf.setPreferredSize(new Dimension(1440, 900));
		// 让窗体最大化
//		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// 设置界面的标题
		jf.setTitle("番茄电影网");
		// 设置界面置顶
//		this.setAlwaysOnTop(true);
		if(flag==1) {
			grtx();
		}
		if(flag==2) {
			hidetx();
			show();
		}
		// 设置界面居中
		jf.setLocationRelativeTo(null);
		// 设置关闭模式
//		jf.setDefaultCloseOperation(3);
		// 取消默认的居中放置，只有取消了才会按照XY的形式添加组件
		jf.setLayout(null);
		//设置窗体左上角的小图标
		ImageIcon img = new ImageIcon("img/logo/1.png");
		jf.setIconImage(img.getImage());
		
		//禁止改变窗体大小
		jf.setResizable(false);
	}
	
	
	public void grtx() {
		ImageIcon icon = new ImageIcon("img/tubiao/头像.png");
		// 创建一个JLabel的对象（管理容器）
		jlm = new JLabel(icon);
		// 指定容器的大小和图片位置(x,y,width,height)
		jlm.setBounds(1200, 5, 50, 50);
		// 把图片等比缩放到width为159px，height为227px的大小
		imgchuli(icon, jlm);
		
		jlm.addMouseListener(new MouseListener() {
			
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
				jf.dispose();
				try {
					new GeRenZhongXin();
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}
		});
		
		jf.getContentPane().add(jlm);
	}
	 public void hideJmdl() {
		 
			 jmzc.setVisible(false);
			 jmdl.setVisible(false); // 调用setVisible方法隐藏jmdl
	  }
	 public void show() {
		 
		 jmzc.setVisible(true);
		 jmdl.setVisible(true); // 调用setVisible方法隐藏jmdl
  }
	 public void hidetx() {
		 jlm.setVisible(false);
  }
}
	
