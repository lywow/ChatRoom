package server_interface_panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Iterator;
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

public class Group_Panel {
	static JPanel gp;
	static JButton[] gpb=new JButton[4];
	static JTextArea gpta;
	static Data_Reading group=new Data_Reading("Ⱥ���б�");
	static File file;
	static String selection="Ⱥ���б�\\";
	static JScrollPane sp;
	
	public Group_Panel()
	{
		gp=new JPanel();
		gp.setLayout(null);
		gp.setBounds(0, 0, 600, 660);
		sp=new JScrollPane(group.tree);
		sp.setBounds(0, 0, 300, 640);
		gp.add(sp);
		
		gpta=new JTextArea();gpta.setBounds(300, 0, 300, 580);gp.add(gpta);gpta.setEditable(false);
		gpta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		
		gpb[0]=new JButton("���б༭");gpb[0].setBounds(300, 580, 150, 40);gp.add(gpb[0]);
		gpb[1]=new JButton("�༭����");gpb[1].setBounds(450, 580, 150, 40);gp.add(gpb[1]);
		gpb[2]=new JButton("�½�Ⱥ��");gpb[2].setBounds(300, 620, 150, 40);gp.add(gpb[2]);
		gpb[3]=new JButton("ɾ��Ⱥ��");gpb[3].setBounds(450, 620, 150, 40);gp.add(gpb[3]);
		
		group.tree.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e) 
			{
				// ����ѡ����԰����κ����������Щ����������ġ�
				group.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
				// ��ȡѡ�нڵ�
				selection="Ⱥ���б�\\";
				gpta.setEditable(false);
				gpta.setText("");
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)group.tree.getLastSelectedPathComponent();
				if (treeNode != null) 
				{
					selection+=treeNode;
					System.out.println(selection);
					try {
						setTextArea();
					} catch (Exception e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
			}
		});
		
		gpb[0].addActionListener(new ActionListener()//���б༭��ť����
		{public void actionPerformed(ActionEvent e){gpta.setEditable(true);}});
		gpb[1].addActionListener(new ActionListener()//�༭���水ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				gpta.setEditable(false);
				try {
					saveTextArea();
				} catch (Exception e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			}
		});
		gpb[2].addActionListener(new ActionListener()//�½�Ⱥ�鰴ť����
		{
			public void actionPerformed(ActionEvent e){gpta.setEditable(false);
			try {
			    create_group();
		    } catch (Exception e1) {
		    	// TODO �Զ����ɵ� catch ��
		    	e1.printStackTrace();
		    }
		}});
		gpb[3].addActionListener(new ActionListener()//ɾ��Ⱥ�鰴ť����
		{
			public void actionPerformed(ActionEvent e){gpta.setEditable(false);
			    TreePath treepath=group.tree.getSelectionPath();
			    if (treepath!=null)
			    {
			        //��������ȡ��ѡȡ�ڵ�ĸ��ڵ�.
			        DefaultMutableTreeNode selectionNode=(DefaultMutableTreeNode)treepath.getLastPathComponent();
			        TreeNode parent=(TreeNode)selectionNode.getParent();
			        System.out.println(selectionNode);
			        if (parent!=null) 
			        {
			            //��DefaultTreeModel��removeNodeFromParent()����ɾ���ڵ㡣
			            group.newModel.removeNodeFromParent(selectionNode);
			        }
			        try {
				        delete_group(selection+selectionNode);
			        } catch (Exception e1) {
			        	// TODO �Զ����ɵ� catch ��
			        	e1.printStackTrace();
		        	}
			    }
	    	}
		});
	}
	
	public JPanel get_panel() {return gp;}
	void delete_group(String str)throws Exception
	{
		JFrame dg=new JFrame("ȷ��ɾ��Ⱥ��");
		dg.setLayout(new FlowLayout());
		JButton dgb=new JButton("ȷ��ɾ��");
		JLabel dgl=new JLabel(">>>>>>>>>>��Ⱥ���Ѿ�ɾ��<<<<<<<<<<");dgl.setVisible(false);
		dg.setSize(250, 150);
		dg.add(dgl);dg.add(dgb);
		dg.setVisible(true);dg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dgb.addActionListener(new ActionListener()//ȷ��������ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				file=new File(str);
				if(file.exists())
				{
					file.delete();
					dg.setVisible(false);
				}
				else {dgl.setVisible(true);dg.setVisible(false);}
		        for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
				{try {
					entry.getValue().send("delgroup|"+selection.substring(0,selection.length()-4));
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}}
			}
		});
	}
	void create_group()throws Exception
	{
		JFrame cg=new JFrame("������Ⱥ������");
		cg.setLayout(new FlowLayout());
		JTextField cgt=new JTextField(20);
		JButton cgb=new JButton("ȷ������");
		JLabel cgl=new JLabel(">>>δ�������Ⱥ���Ѵ���<<<");cgl.setVisible(false);
		cg.setSize(250, 150);
		cg.add(cgt);cg.add(cgl);cg.add(cgb);
		cg.setVisible(true);cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cgb.addActionListener(new ActionListener()//ȷ��������ť����
		{
			public void actionPerformed(ActionEvent e)
			{
				file=new File("Ⱥ���б�\\"+cgt.getText()+".txt");
				if(cgt.getText()!=null&&!file.exists())
				{
					try {
						file.createNewFile();
						addgroup(cgt.getText());
						TreePath treepath=group.tree.getSelectionPath();
					    if (treepath!=null)
					    {
					        //��������ȡ��ѡȡ�ڵ�ĸ��ڵ�.
					        DefaultMutableTreeNode selectionNode=(DefaultMutableTreeNode)treepath.getLastPathComponent();
					        DefaultMutableTreeNode parent=(DefaultMutableTreeNode)selectionNode.getParent();
					        
					        DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(cgt.getText()+".txt");
					        newNode.setAllowsChildren(true);
							//��DefaultTreeModel��insertNodeInto�������������½ڵ�
							group.newModel.insertNodeInto(newNode,parent,parent.getChildCount());
							for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
							{try {
								entry.getValue().send("addgroup|"+cgt.getText());
							} catch (IOException e1) {
								// TODO �Զ����ɵ� catch ��
								e1.printStackTrace();
							}}
					    }
						cg.setVisible(false);
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
				else {cgl.setVisible(true);}
			}
		});
	}
	void saveTextArea ()throws Exception
	{
		file=new File(selection);
		if (file != null) 
		{
			FileWriter fr = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fr);
			try 
			{
				String[] sss=gpta.getText().split("\\n");
				for(int i=0;sss[i]!=null;i++) 
				{bw.write(sss[i]+"\r\n");}
			}
			finally 
			{
				bw.close();
				fr.close();
			}
		}
	}
	void setTextArea() throws Exception 
	{
		file=new File(selection);
		if (file != null) 
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try 
			{
				while (br.ready()) 
				{gpta.append(br.readLine()+"\r\n");}
			}
			finally 
			{
				br.close();
				fr.close();
			}
		}
	}
	void addgroup(String str) throws IOException
	{
		file=new File("Ⱥ���б�.txt");
		if (!file.exists()) 
		{file.createNewFile();}
		FileWriter fr = new FileWriter(file,true);
		BufferedWriter bw = new BufferedWriter(fr);
		try 
		{
			{bw.write(str+"\r\n");}
		}
		finally 
		{
			bw.close();
			fr.close();
		}
		Client_Thread integ;
		Iterator<String> iter = Server_Network.clientlist.keySet().iterator();
		while (iter.hasNext()) 
		{
			integ = Server_Network.clientlist.get(iter.next());
			try {
				integ.send("addgroup|||"+str);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
}
