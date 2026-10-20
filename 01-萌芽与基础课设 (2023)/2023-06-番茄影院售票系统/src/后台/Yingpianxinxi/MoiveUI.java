package Yingpianxinxi;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.mysql.jdbc.ResultSet;

import shujukulianjie.Mysql;
import yingpianxinxiGL.AddMovie;
import yingpianxinxiGL.UpdateMovie;


public class MoiveUI extends JFrame {
	List<Moive> list;
	JTable table;
	JTextField txtId = new JTextField(20);
	static String name="";
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MoiveUI() {
		this.setSize(1280, 600);
		JPanel top = new JPanel();

		JLabel labelId = new JLabel("电影名称：");

		JButton btncha = new JButton("查询");
		JButton btnDelete = new JButton("删除");
		JButton btnUpdate = new JButton("修改");
		JButton btnAdd = new JButton("添加");

		top.add(labelId);
		top.add(txtId);
		top.add(btncha);
		top.add(btnDelete);
		top.add(btnUpdate);
		top.add(btnAdd);

		this.add(top, BorderLayout.NORTH);
		showTable();
		
		btncha.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setName(txtId.getText());
				System.out.println(getName());
				dispose();
				new MoiveUI();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddMovie();
				dispose();
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int n = table.getSelectedRow();
					Moive m = list.get(n);
					new UpdateMovie(m);
					dispose();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "请选中一行数据！");
				}

			}
		});
		
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int answer = JOptionPane.showConfirmDialog(null, "确定要删除吗？");
				int index = table.getSelectedRow();
				if (answer == 0) {
					if (index >= 0) {
						int id = list.get(index).getId();
						boolean n = MoiveDao.delete(id);
						if (n) {
							JOptionPane.showMessageDialog(null, "删除成功！");
							new MoiveUI();
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "删除失败！");
						}
					} else {
						JOptionPane.showMessageDialog(null, "请选中一行数据！");
					}
				}

			}

		});
		setTitle("影片管理");
		// 设置界面居中
		setLocationRelativeTo(null);
		// 设置关闭模式
		setDefaultCloseOperation(2);
		ImageIcon img = new ImageIcon("img/logo/1.png");
		setIconImage(img.getImage());
		this.setVisible(true);
	}

	public void showTable() {
//		if(list==null) {
			list = MoiveDao.select(getName());
//		}
		String[] head = { "电影编号", "电影名", "票房", "导演", "演员", "起始日期", "结束日期", "电影时长","票价","类型 ","国家","简介","评分"};
		Object[][] data = new Object[list.size()][13];
		for (int i = 0; i < list.size(); i++) {
			data[i][0] = list.get(i).getId();
			data[i][1] = list.get(i).getName();
			data[i][2] = list.get(i).getHot();
			data[i][3] = list.get(i).getDirector();
			data[i][4] = list.get(i).getActorid();
			data[i][5] = list.get(i).getStartdate();
			data[i][6] = list.get(i).getEnddate();
			data[i][7] = list.get(i).getMovieduration();
			data[i][8] = list.get(i).getFare();
			data[i][9] = list.get(i).getMoviegenre();
			data[i][10] = list.get(i).getCountry();
			data[i][11] = list.get(i).getSynopsis();
			data[i][12] = list.get(i).getScore();

		}
		table = new JTable(data, head);
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;


		JScrollPane jsp = new JScrollPane(table, v, h);
		this.add(jsp, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new MoiveUI();
	}

}
