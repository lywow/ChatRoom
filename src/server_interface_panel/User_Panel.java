package server_interface_panel;

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import server_interface.Client_Thread;
import server_interface.Server_Network;
import server_interface.Working;

public class User_Panel {
	static JPanel up=new JPanel();
	static JPanel up_other=new JPanel();
	static JTextField[] upt= {new JTextField(20),new JTextField(20)};
	static JButton upb1,upb2,upb3;
	static Data_Reading user=new Data_Reading("�û��б�");
	static String selection=null;
	static JScrollPane sp;
	static JLabel upl=new JLabel();
	File file;
	
	public User_Panel()
	{
		up.setLayout(null);
		sp=new JScrollPane(user.tree);
		sp.setBounds(0, 0, 300, 660);
		up.setBounds(0, 0, 600, 660);
		up.add(sp);
		
		up_other.setBounds(300, 0, 300, 620);
		up_other.setLayout(new FlowLayout());
		up_other.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		up_other.add(new JLabel("�� �ţ�"));up_other.add(upt[0]);
		up_other.add(new JLabel("�� �룺"));up_other.add(upt[1]);
		up.add(up_other);
		for(int i=0;i<2;i++) {upt[i].setEditable(false);}

		upb1=new JButton("���б༭");up_other.add(upb1);
		upb2=new JButton("�༭����");up_other.add(upb2);
		up_other.add(new JLabel("-----------------------------------------------------------------------------------"));
		upb3=new JButton("ɾ������û�");up_other.add(upb3);up_other.add(upl);upl.setVisible(false);
		
		upb3.addActionListener(new ActionListener()//ɾ��yonghu��ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				if(selection.length()>5)
				{
					if(Server_Network.clientlist.containsKey(selection))
					{
						delFolder("�û��б�\\"+selection+".txt");
						delFolder("�û�����\\"+selection);
						for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
						{try {
							entry.getValue().send("delfriend|"+selection);
						} catch (IOException e1) {
							// TODO �Զ����ɵ� catch ��
							e1.printStackTrace();
						}}
					    TreePath treepath=user.tree.getSelectionPath();
					    if (treepath!=null)
					    {
					        //��������ȡ��ѡȡ�ڵ�ĸ��ڵ�.
					        DefaultMutableTreeNode selectionNode=(DefaultMutableTreeNode)treepath.getLastPathComponent();
					        TreeNode parent=(TreeNode)selectionNode.getParent();
					        System.out.println(selectionNode);
					        if (parent!=null) 
					        {
					            //��DefaultTreeModel��removeNodeFromParent()����ɾ���ڵ㡣
					        	user.newModel.removeNodeFromParent(selectionNode);
					        }
					    }
					}
					else {upl.setText(">>>>>>>>>���ߵ��˺Ų���ɾ��<<<<<<<<<");}
				}
				else {upl.setText(">>>>>>>>>>û��ѡ���κ��û�<<<<<<<<<<");}
				upl.setVisible(false);
			}
		});
		upb1.addActionListener(new ActionListener()//���б༭��ť����
		{public void actionPerformed(ActionEvent e){for(int i=0;i<2;i++) {upt[i].setEditable(true);}}});
		upb2.addActionListener(new ActionListener()//ȷ���༭��ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					user_save();
				} catch (Exception e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				for(int i=0;i<2;i++) {upt[i].setEditable(false);}
			}
		});
		
		user.tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e) 
			{
				// ����ѡ����԰����κ����������Щ����������ġ�
				user.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
				// ��ȡѡ�нڵ�
				for(int i=0;i<2;i++) 
				{upt[i].setText("");}
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)user.tree.getLastSelectedPathComponent();
				if (treeNode != null) 
				{
					selection=treeNode.toString();
					selection=selection.substring(0, selection.length()-4);
					try {
						user_reading();
					} catch (Exception e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
			}
		}); 
	}
	void user_save()throws Exception
	{
		file=new File("�û��б�\\"+selection+".txt");
		if (file != null) 
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			try 
			{
				for(int i=0;i<2;i++) 
				{bw.write(upt[i].getText() + "\r\n");}
			}
			finally 
			{
				bw.close();
				fw.close();
			}
		}
	}
	
	void user_reading() throws Exception 
	{
		file=new File("�û��б�\\"+selection+".txt");
		if (file != null) 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try 
			{
				for(int i=0;i<2&&br.ready();i++) 
				{upt[i].setText(br.readLine());}
			}
			finally 
			{
				br.close();
				fr.close();
			}
		}
	}
	
	
	public void delFolder(String folderPath) //ɾ���û��ļ�����
	{
	    try {  
	        delAllFile(folderPath); //ɾ����������������  
	        String filePath = folderPath;  
	        filePath = filePath.toString();  
	        java.io.File myFilePath = new java.io.File(filePath);  
	        myFilePath.delete(); //ɾ�����ļ���  
	     } catch (Exception e) {  
	       e.printStackTrace();   
	     }  
	}
	public boolean delAllFile(String path) 
	{  
		boolean flag = false;  
	    File file = new File(path);  
	    if (!file.exists()) 
	    {return flag;}  
	    if (!file.isDirectory()) 
	    {return flag;}  
	    String[] tempList = file.list();  
	    File temp = null;  
	    for (int i = 0; i < tempList.length; i++) 
	    {
		    if (path.endsWith(File.separator)) 
		    {  
		    	temp = new File(path + tempList[i]);  
		    } else {temp = new File(path + File.separator + tempList[i]);  }  
		          
		    if (temp.isFile())
		    {temp.delete();}  
		    if (temp.isDirectory()) 
		    {
		    	delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�  
		    	delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���  
		        flag = true;  
		    } 
	    }  
	    return flag;  
	}  
	
	public JPanel get_panel() {return up;}
}
