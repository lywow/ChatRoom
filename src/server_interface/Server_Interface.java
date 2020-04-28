package server_interface;

import java.awt.*;
import server_interface_panel.*;
import java.awt.event.*;
import javax.swing.*;

public class Server_Interface {
	static JFrame si;
	static User_Panel up=new User_Panel();
	static Group_Panel gp=new Group_Panel();
	static JPanel si_card=new JPanel();
	static CardLayout card=new CardLayout();
	static JButton sibu,sibg;
	static JLabel sil;
	
	Server_Interface()
	{
		si=new JFrame("����ϵͳ�������������");
		si.setSize(600, 738);
		si.setResizable(false);
		si.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		si.setLayout(null);
		si.setLocationRelativeTo(null);
		
		sibu=new JButton("�û��б�");sibu.setBounds(0, 0, 150, 40);si.add(sibu);
		sibg=new JButton("Ⱥ���б�");sibg.setBounds(150, 0, 150, 40);si.add(sibg);
		sil=new JLabel("��ǰ�û��б�");sil.setBounds(300, 0, 300, 40);si.add(sil);
		
		si_card.setLayout(card);
		si_card.setBounds(0, 40, 600, 660);
		si_card.add(up.get_panel(),"user");si_card.add(gp.get_panel(),"group");si.add(si_card);
		sibu.addActionListener(new ActionListener()//��ת������Ϣ����
		{public void actionPerformed(ActionEvent e){sil.setText("��ǰ�û��б�");card.show(si_card,"user");}});
		sibg.addActionListener(new ActionListener()//��ת�����б����
		{public void actionPerformed(ActionEvent e){sil.setText("��ǰȺ���б�");card.show(si_card,"group");}});
		
		/*up.gettree().addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e) {
				// ����ѡ����԰����κ����������Щ����������ġ�
				up.get_user_tree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
				// ��ȡѡ�нڵ�
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)up.get_user_tree().getLastSelectedPathComponent();
				if (treeNode != null) 
				{// ��ȡѡ�нڵ�ĸ��ڵ�
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeNode.getParent();
					if (!pding.equals(parent)) 
					{
						System.out.println(".......");
						selection=selection+parent+"\\"+treeNode;
						try {
							gp.setTextArea(selection);
						} catch (Exception e1) {
							// TODO �Զ����ɵ� catch ��
							e1.printStackTrace();
						}
						selection="C:\\Users\\Administrator\\Desktop\\�½��ļ���\\�û�����\\";
					}
				}
			}
		}); */
	}
	
	void server_interface_display()
	{si.setVisible(true);}
}
