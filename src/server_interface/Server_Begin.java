/*package server_interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server_Begin {
	JFrame sbi;
	JButton sbb1,sbb2;
	JTextField sbt;
	String pass="00000";//����������
	JLabel lll=new JLabel(">>>>>>>>>������������<<<<<<<<<");
	int i=0;
	
	Server_Begin()
	{
		sbi=new JFrame("��������������");
		sbb1=new JButton("����������");
		sbb2=new JButton("�رձ�����");
		sbt=new JTextField(20);
		sbi.setLayout(new FlowLayout());
		sbi.add(new JLabel("�����뿪��������Կ��"));
		
		lll.setVisible(false);
		sbi.add(sbt);
		sbi.add(lll);
		sbi.add(sbb1);
		sbi.add(sbb2);
		sbi.setSize(250, 180);
		sbi.setResizable(false);
		sbi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void server_begin_display()
	{
		sbi.setVisible(true);
		sbb1.addActionListener(new ActionListener()//������������ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				if(pass.equals(sbt.getText()))
				{
					i=1;
				}
				else {lll.setVisible(true);}
			}
		});
		sbb2.addActionListener(new ActionListener()//�˳���������ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				sbi.setVisible(false);
			}
		});
	}
	
	public int get_i()
	{return i;}
	
	public void beginclose()
	{
		sbi.setVisible(false);
	}
}*/
