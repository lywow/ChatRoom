package server_interface_panel;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Data_Reading extends JFrame {
	private static final long serialVersionUID = -1877527354792619586L;
	
	JTree tree;
	DefaultTreeModel newModel;
	DefaultMutableTreeNode Node;
	DefaultMutableTreeNode temp;
	String path;//��Ҫ������Ŀ¼
	public Data_Reading(String path) 
	{
		Node=traverseFolder(path);
		newModel=new DefaultTreeModel(Node);
		tree=new JTree(newModel);
		tree.setEditable(true);
		System.out.println("������һ����");
	}
	public DefaultMutableTreeNode traverseFolder(String path) {
		DefaultMutableTreeNode fujiedian = new DefaultMutableTreeNode(new File(path).getName());
        File file = new File(path);
       
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                if(file.isDirectory()) {//����ǿ��ļ���
                	DefaultMutableTreeNode dn=new DefaultMutableTreeNode(file.getName(), false);
                	return dn;
                }
            }else{
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                    	//��Ŀ¼�Ļ������ɽڵ㣬���������Ľڵ�
                    	fujiedian.add(traverseFolder(file2.getAbsolutePath()));
                    }else{
                    	//���ļ��Ļ�ֱ�����ɽڵ㣬���Ѹýڵ�ӵ���Ӧ���ڵ���
                    	temp=new DefaultMutableTreeNode(file2.getName());
                    	fujiedian.add(temp);
                    }
                }
            }
        } else {//�ļ�������
            return null;
        }
		return fujiedian;
    }
}
