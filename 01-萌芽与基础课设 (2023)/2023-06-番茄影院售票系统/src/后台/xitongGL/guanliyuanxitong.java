package xitongGL;
import javax.swing.*;

import Yingpianxinxi.MoiveUI;
import dingdanGL.dingdanguanli;
import paipianGL.paipianGL;
import shouye.ShouYe;
import yonghuGL.yonghuguanli;

import java.awt.Color;
import java.awt.event.*;

public class guanliyuanxitong {
	public guanliyuanxitong(){
   
      JFrame frame =new JFrame();
      ImageIcon ico=new ImageIcon("img/logo/1.png");
       frame.setSize(1350,900);
         frame.setTitle("后台系统管理界面");
         frame.setIconImage(ico.getImage());
       //设置关闭模式
         frame.setDefaultCloseOperation(2);
         // 设置界面居中
         frame.setLocationRelativeTo(null); 
         
         
        JPanel panel = new JPanel();
        JButton button = new JButton("排片管理");
        JButton button1 = new JButton("用户管理");
        JButton button2 = new JButton("订单管理");
        JButton button3 = new JButton("影片信息管理");
        JButton button4 = new JButton("返回首页");
        
        button.addMouseListener(new MouseAdapter() {
                               @Override
                     public void mouseClicked(MouseEvent e) {
                     new paipianGL();
                            }
                            }  );
        
        button1.addMouseListener(new MouseAdapter() {
            @Override
  public void mouseClicked(MouseEvent e) {
            	new yonghuguanli();
  //System.out.println("按钮被单击了");
         }
         }  );
        
        
        button2.addMouseListener(new MouseAdapter() {
            @Override
  public void mouseClicked(MouseEvent e) {
 new dingdanguanli();
         }
         }  );
        
        
        button3.addMouseListener(new MouseAdapter() {
            @Override
  public void mouseClicked(MouseEvent e) {
            	new MoiveUI();
            	
  //System.out.println("按钮被单击了");
         }
         }  );
        
        button4.addMouseListener(new MouseAdapter() {
            @Override
  public void mouseClicked(MouseEvent e) {
            	try {
            		frame.dispose();
					new ShouYe();
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
            	
         }
         }  );
        
        
        panel.setLayout(null);
        button.setBounds(70, 200, 200, 75);
        panel.add(button);
        button.setBackground(new Color(255,255,255));
      //设置前景色（字体颜色）
        button.setForeground(new Color(255, 0, 0));
        
        button1.setBounds(370, 200, 200, 75);
        panel.add(button1);
        button1.setBackground(new Color(255,255,255));
        button1.setForeground(new Color(255, 0, 0));
        
        button2.setBounds(670, 200, 200, 75);
        panel.add(button2);
        button2.setBackground(new Color(255,255,255));
        button2.setForeground(new Color(255, 0, 0));
        
        button3.setBounds(970, 200, 200, 75);
        panel.add(button3);
        button3.setBackground(new Color(255,255,255));
        button3.setForeground(new Color(255, 0, 0));
        
        button4.setBounds(1200, 780, 105, 25);
        panel.add(button4);
        button4.setBackground(new Color(255,255,255));
        button4.setForeground(new Color(255, 0, 0));
        
        panel.setBounds(100, 200, 100, 100);
        panel.setBackground(new Color(255,255,255));
        frame.getContentPane().add(panel);
        
        
      
        
    //    frame.setContentPane(panel);

        frame.setVisible(true);
    }

	public static void main(String[] args) {
		guanliyuanxitong jfe=new guanliyuanxitong();
	}

//	public void setVisible(boolean b) {
//		// TODO 自动生成的方法存根
//		
//	}	


	}
	








//public static void main1(String[] args) {
//	guanliyuanxitong jfe=new guanliyuanxitong();
//}





//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//public class guanliyuanxitong  {     //管理员登录后的第一界面 
//
//	 guanliyuanxitong(){
//		// TODO 自动生成的方法存根
//		JFrame frame =new JFrame();
//		ImageIcon ico=new ImageIcon("img/logo/1.png");
//		frame.setBounds(200, 100, 1350, 900);
//		frame.setTitle("后台系统管理界面");
//		frame.setIconImage(ico.getImage());
//	  frame.setVisible(true);
//		
//		
//	}

