package denglu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

import chuangkou_caidan_imgchuli.*;
import gerenzhuye.GeRenZhongXin;
import shouye.ShouYe;
import shujukulianjie.*;;
//注册
public class DengLufeigao {
	Font font = new Font("宋体", Font.BOLD, 24);
	Font font2 = new Font("微软雅黑", Font.PLAIN, 20);
	 //导入初始化类
	static	ChushihuaCaidan csh = new ChushihuaCaidan();
	//创建文本标签和文本框
	JLabel title=new JLabel("番茄影院登录系统");
	JLabel usernamel=new JLabel("用户名");
	JLabel usernuml=new JLabel("密码  ");
	
	static JTextField usernamet=new JTextField(18);//列宽18
	static JPasswordField  usernumt=new JPasswordField (18);//列宽18
//	static JTextField usernamet=new JTextField(18);//列宽18
//	static JPasswordField  usernumt=new JPasswordField (18);//列宽18
	  //添加登录按钮
    JButton jbutton=new JButton("立即登录");
    
	public DengLufeigao() {

		usernamel.setFont(font2);
		usernuml.setFont(font2);
		jbutton.setFont(font2);

		//创建一个容器用来储存
		JPanel jp=new JPanel();

		//将控件高度设置为 30 像素
		usernamet.setPreferredSize(new Dimension(usernamet.getPreferredSize().width, 25));
		usernumt.setPreferredSize(new Dimension(usernamet.getPreferredSize().width, 25));
		jbutton.setPreferredSize(new Dimension(120,30));
		//初始化窗体
		csh.initJFrame();
		csh.jf.remove(jp);
		jp.add(usernamel);//账号文字
		jp.add(usernamet);//账号输入框
//		jp.add(Box.createVerticalStrut(20)); // 垂直间距为 10 像素
//		jp.add(Box.createVerticalStrut(20));
		jp.add(usernuml);//密码文字
//		jp.add(Box.createVerticalStrut(20));
		jp.add(usernumt);//密码输入框
//		jp.add(Box.createVerticalStrut(20));
		 jp.add(jbutton);
		 jp.add(Box.createVerticalStrut(40));
//		jp.setSize(500,500);
		csh.jf.setLayout(new BorderLayout());
		csh.jf.add(jp);
		//jpN的高度设置为80像素，宽度自适应
		JPanel jpN = new JPanel();//添加到北容器
		title.setFont(font);
		jpN.add(title);//标题文字
		
		jpN.add(Box.createVerticalStrut(100));
		jpN.setPreferredSize(new Dimension(0,100));
		
		//将容器添加到北区域
		 csh.jf.add(jpN,BorderLayout.NORTH);
		
		//创建一个新的容器jpE
		 JPanel jpE = new JPanel();

		 //将容器添加到东区域
		 csh.jf.add(jpE, BorderLayout.EAST);
		 //设置jpE的宽度为150像素，高度自适应
		 jpE.setPreferredSize(new Dimension(100, 0));
		 
			//创建一个新的容器jpE
		 JPanel jpW = new JPanel();

		 //将容器添加到东区域
		 csh.jf.add(jpW, BorderLayout.WEST);
		 //设置jpW的宽度为150像素，高度自适应
		 jpW.setPreferredSize(new Dimension(100, 0));
		 
		 jbutton.addMouseListener(new MouseListener() {		
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Puanduan();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}


		});

		 
	   
		 
			//设置北容器的背景色为黄色
//			jpN.setBackground(new Color(255,255,0));
//			 //设置jpE的背景色为绿色
//			 jpE.setBackground(new Color(0,255,0));
//			 //设置jpW的背景色为绿色
//			 jpW.setBackground(new Color(0,255,0));
			 
		 //设置容器的背景色为黑色
//		jp.setBackground(new Color(0,0,0));
		//设置窗体宽高
		csh.jf.setSize(450, 450);
		csh.jf.setLocationRelativeTo(null);
        csh.jf.add(jp, BorderLayout.CENTER);//将组件添加到容器中间位置

		

        
		//设置容器在窗体中的位置
//		csh.jf.getContentPane().add(jp);
		//取消最大化
		csh.jf.setExtendedState(JFrame.NORMAL);
		csh.jf.setVisible(true);;
        //禁止改变窗体大小
		csh.jf.setResizable(false);
		// 设置界面置顶
//		csh.jf.setAlwaysOnTop(true);
	}
	
	//点击注册将账号密码添加到数据库
	public static void Puanduan() throws Exception {
		//与数据库连接
		Connection conn = (Connection) Mysql.getConnection();
		
		String sql = "SELECT * from public";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		//获取账号的值
		String text = usernamet.getText();
		//获取密码的值
		char[] password = usernumt.getPassword();
		String passwordString = new String(password);
		//执行查询语句
		ResultSet rs = pstmt.executeQuery();
        //判断两次密码是否一致
        if(text!=null && passwordString.length()!=0) {
        	int flag=0;
        		while(rs.next()) {
        			String user = rs.getString(1);
        			String word = rs.getString(2);
            			if(user.equals(text)&&word.equals(passwordString)) {
//                    		会在W弹出一个消息框，显示一个指定的消息文本
//                    		第一个参数是指定该消息框的父组件，null 表示该消息框不属于任何一个父组件；第二个参数是消息文本
            				JOptionPane.showMessageDialog(null, "登陆成功！");
            				flag=1;
            				ChushihuaCaidan.setFlag();
            				//把账号的值传给个人中心页面
            				GeRenZhongXin.setUser(user);
            				
            				csh.jf.dispose();
            			    // 关闭其他所有窗体
            			    Window[] windows = Window.getWindows();
            			    for (Window window : windows) {
            			        if (window != csh.jf) { // 排除当前窗体
            			            window.dispose();
            			        }
            			    }
            				try {
    							new ShouYe();
    						} catch (Exception e) {
    							// TODO 自动生成的 catch 块
    							e.printStackTrace();
    						}
            				break;
            			}
        			}
        		if(flag==0) {
        			JOptionPane.showMessageDialog(null, "账号或密码错误！");
        		}
        }else{
        	JOptionPane.showMessageDialog(null, "账号和密码不能为空！");
        }

        
        rs.close();
        pstmt.close();
        conn.close();
	}
	
}
