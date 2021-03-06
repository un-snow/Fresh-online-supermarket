package cn.edu.zucc.sxwc.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import cn.edu.zucc.sxwc.comtrol.example.Goodsorder;
import cn.edu.zucc.sxwc.comtrol.example.Orderxq;
import cn.edu.zucc.sxwc.model.BeanGoodsorder;
import cn.edu.zucc.sxwc.model.BeanOrderxq;
import cn.edu.zucc.sxwc.model.BeanUser;
import cn.edu.zucc.sxwc.util.BaseException;
import cn.edu.zucc.sxwc.util.DBUtil;
import cn.edu.zucc.sxwc.util.DbException;

public class Frmorder extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private JButton btnNewButton = new JButton("确认收货");
	private JButton btnNewButton_1 = new JButton("请求退货");
	private Object tblTitle[]=BeanGoodsorder.GoodsorderTitles;
	private Object tblgorderData[][];
	DefaultTableModel tabgordermod=new DefaultTableModel();
	private JTable gordertable=new JTable(tabgordermod);
	List<BeanGoodsorder> allgorder=null;
	private BeanGoodsorder curgorder=null;
	List<BeanOrderxq>allorder=null;
	
	private Object tblorderTitle[]=BeanOrderxq.OrderxqTitles;
	private Object tblorderData[][];
	DefaultTableModel tabordermod=new DefaultTableModel();
	private JTable ordertable=new JTable(tabordermod);
	
	private void reloadgorderTable(){
		try {
			Goodsorder gorder=new Goodsorder();
			allgorder=gorder.loadGorder();
		} catch (BaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		tblgorderData =  new Object[allgorder.size()][BeanGoodsorder.GoodsorderTitles.length];
		for(int i=0;i<allgorder.size();i++){
			for(int j=0;j<BeanGoodsorder.GoodsorderTitles.length;j++)
				{
					tblgorderData[i][j]=allgorder.get(i).getCell(j);
				}
			}
			tabgordermod.setDataVector(tblgorderData,tblTitle);
			this.gordertable.validate();
			this.gordertable.repaint();
	}
	private void reloadorderTable(int goodsid){
		if(goodsid<0) return;
		curgorder=allgorder.get(goodsid);
		try {
			Orderxq order=new Orderxq();
			allorder=order.loadorder(curgorder);
		} catch (BaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		tblorderData =  new Object[allorder.size()][BeanOrderxq.OrderxqTitles.length];
		for(int i=0;i<allorder.size();i++){
			for(int j=0;j<BeanOrderxq.OrderxqTitles.length;j++)
				{
					tblorderData[i][j]=allorder.get(i).getCell(j);
				}
			}
			tabordermod.setDataVector(tblorderData,tblorderTitle);
			this.ordertable.validate();
			this.ordertable.repaint();
	}
	
	public Frmorder(Frame f, String s, boolean b) {
		super(f,s,b);
		setTitle("我的订单");
		setBounds(100, 100, 790, 514);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 772, 375);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.reloadgorderTable();
		this.getContentPane().add(contentPanel);;
		//getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		JScrollPane scrollPane1 = new JScrollPane(this.ordertable);
		scrollPane1.setBounds(371, 0, 399, 297);
		contentPanel.add(scrollPane1);
		JScrollPane scrollPane = new JScrollPane(this.gordertable);
		scrollPane.setBounds(0, 0, 371, 297);
		contentPanel.add(scrollPane);
		btnNewButton_1.setBounds(407, 411, 113, 27);
		getContentPane().add(btnNewButton_1);
		btnNewButton.setBounds(38, 411, 113, 27);
		getContentPane().add(btnNewButton);
		this.gordertable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i=Frmorder.this.gordertable.getSelectedRow();
				if(i<0) {
					return;
				}
				Frmorder.this.reloadorderTable(i);
			}
		});
		this.btnNewButton.addActionListener(this);
		this.btnNewButton_1.addActionListener(this);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.btnNewButton) {
			int i=this.gordertable.getSelectedRow();
			if(i<0) {
				JOptionPane.showMessageDialog(null,  "请先选择商品订单","提示",JOptionPane.ERROR_MESSAGE);
				return;
			}
			BeanGoodsorder gorder=this.allgorder.get(i);
			if(JOptionPane.showConfirmDialog(this,"确定要收货吗？","确认",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				try {
					(new Goodsorder()).update(gorder.getOrderid(), BeanUser.currentLoginUser.getUserid());
					this.reloadgorderTable();
				} catch (BaseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
				
			}
	}
		else if(e.getSource() == this.btnNewButton_1) {
			int i=this.gordertable.getSelectedRow();
			if(i<0) {
				JOptionPane.showMessageDialog(null,  "请先选择商品订单","提示",JOptionPane.ERROR_MESSAGE);
				return;
			}
			BeanGoodsorder gorder=this.allgorder.get(i);
			if(JOptionPane.showConfirmDialog(this,"确定要退货吗？","确认",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				try {
					(new Goodsorder()).returngoods(gorder.getOrderid(), BeanUser.currentLoginUser.getUserid());
					this.reloadgorderTable();
				} catch (BaseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
}
}
