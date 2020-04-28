package server_interface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import server_interface_panel.User_Panel;

public class Client_Thread  implements Runnable{
	BufferedReader ii;
	PrintStream oo;
	Socket client;
	String key;
	File file;
	Thread light;
	
	Client_Thread(Socket cli) throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
		Calendar calendar = Calendar.getInstance();  
		Date date0 = calendar.getTime();  
		String date = sdf.format(date0);
		key=date;
		client=cli;
		ii=new BufferedReader(new InputStreamReader(client.getInputStream()));
		oo=new PrintStream(client.getOutputStream());
		light=new Thread(this);light.start();
	}

	public void run() 
	{
		String message=null;
		while(true)
		{
			try {
				if(!client.isClosed())
				{message=ii.readLine();}
				else
				{break;}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			if(message!=null)
			{
				message.trim();
				System.out.println("��"+key+"���գ�"+message);
				message_analysis(message);
			}
		}
		System.out.println(key+"�Ͽ������ӣ����߳��ѹر�");
		light.stop();
	}
	public void message_analysis(String mes)
	{
		String fort[]=mes.split("\\|",4);
		if(fort[0].equals("register"))//�û�ע��
		{
			file=new File("�û��б�\\"+fort[1]+".txt");
			try {
			    if(!file.exists())
			    {
			    	registration(mes);
			    	send("register|yes");
			    	System.out.println(fort[1]);
			    }
			    else
			    {
			    	send("register|non");
			    }
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("login"))//��¼��֤
		{
			file=new File("�û��б�\\"+fort[1]+".txt");
			try {
				String lo=vilidate(fort[1]);
				if(file.exists()&&mes.equals(lo))
				{
					Server_Network.clientlist.put(fort[1], this);
					Server_Network.clientlist.remove(key);
					key=fort[1];
					send("login|"+"yes|"+fort[1]+"|"+fort[2]);
					for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
					{
						if(!entry.getKey().equals(key))
						{entry.getValue().send("online|"+key);}
					}
				}
				else
				{
					send("login|non");
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("friend"))//�����б��������
		{
			try {
				send(friendlist());
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("group"))//Ⱥ���б��������
		{
			try {
				send(grouplist());
				Iterator<Entry<String, Client_Thread>> it = Server_Network.clientlist.entrySet().iterator();
				while (it.hasNext()) 
				{
					Entry<String, Client_Thread> entry = it.next();
					if(!entry.getKey().equals(key))
					{
						send("online|"+entry.getKey());
					}
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
		if(fort[0].equals("private"))//˽����Ϣ����ת��
		{
			try {
				Server_Network.clientlist.get(fort[2]).send(mes);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("public"))//Ⱥ����Ϣ����ת��
		{
			for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
			{try {
				if(!entry.getKey().equals(key))
				{entry.getValue().send(mes);;}
				
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}}
		}
		if(fort[0].equals("addfriend"))//��Ӻ���
		{
			file=new File("�û��б�\\"+fort[1]+".txt");
			try {
				if(file.exists())
				{
				    send("addfriend|yes|"+fort[1]);
				    addfriend(key,fort[1]);
				    addfriend(fort[1],key);
				    if(Server_Network.clientlist.containsKey(fort[1]))
				    {
				    	Client_Thread cth=Server_Network.clientlist.get(fort[1]);
				    	cth.send("addfriend|yes|"+key);
				    }
				}
				else
				{
					send("addfriend|non");
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("change"))//������Ϣ�޸�
		{
			try {
				registration(fort[1]+"|"+fort[2]);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("offline"))//�û�����
		{
			Server_Network.clientlist.remove(key);
			for(ConcurrentHashMap.Entry<String, Client_Thread> entry: Server_Network.clientlist.entrySet()) 
			{try {
				entry.getValue().send(mes);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}}
			try {
				client.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("*****offline"))//δ��¼�û�����
		{
			Server_Network.clientlist.remove(key);
			try {
				client.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		if(fort[0].equals("delself"))//�û�ע��
		{
			delFolder("�û�����\\"+key);
			delFolder("�û��б�\\"+key+".txt");
		}
	}
	
	public void send(String str) throws IOException
	{
		System.out.println("��"+key+"���ͣ�"+str);
		oo.println(str);
		oo.flush();
	}
	
	String vilidate(String sss) throws IOException
	{
		file=new File("�û��б�\\"+sss+".txt");
		if(file.exists())
		{
		    FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str="login";
			while(br.ready())
			{str=str+"|"+br.readLine();}
			br.close();
			fr.close();
			return str;
		}
		else
		{return null;}
	}
	
	void registration(String str) throws IOException
	{
		String sss[]=str.split("\\|");
		file=new File("�û��б�\\"+sss[1]+".txt");
		if(!file.exists()) {file.createNewFile();}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		try 
		{
			for(int i=1;i<sss.length;i++) 
			{bw.write(sss[i]+"\r\n");}
		}
		finally 
		{
			bw.close();
			fw.close();
		}
	}
	
	void addfriend(String user,String friend) throws IOException
	{
		file=new File("�û�����\\"+user+"\\�����б�.txt");
		if(!file.exists())
		{file.createNewFile();}
		FileWriter fw = new FileWriter(file,true);
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(friend);
		}
		finally 
		{
			bw.close();
			fw.close();
		}
	}
	
	String friendlist() throws IOException
	{
		String str="friend";String sss[];String yub;
		file=new File("�û�����\\"+key);
		if(!file.exists()) {file.mkdirs();}
		file=new File("�û�����\\"+key+"\\�����б�.txt");
		if(!file.exists()) {file.createNewFile();}
	    FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready())
		{
			yub=br.readLine();
			if(friend_exist(yub))
			{str=str+"|"+yub;}
		}
		br.close();
		fr.close();
		
		sss=str.split("\\|");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=1;i<sss.length;i++)
		{bw.write(sss[i]+"\r\n");}
		bw.close();
		fw.close();
		
		return str;
	}
	String grouplist() throws IOException
	{
		String str="group";String sss[];String yub;
		file=new File("Ⱥ���б�.txt");
		if(!file.exists()) {file.createNewFile();}
	    FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		while(br.ready())
		{
			yub=br.readLine();
			if(new File("Ⱥ���б�\\"+yub+".txt").exists())
			{str=str+"|"+yub;}
		}
		br.close();
		fr.close();
		
		sss=str.split("\\|");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=1;i<sss.length;i++)
		{bw.write(sss[i]+"\r\n");}
		bw.close();
		fw.close();
		
		return str;
	}
	
	Boolean friend_exist(String name)//�û������б�����÷���-1
	{
		File file1=new File("�û��б�\\"+name+".txt");
		if(file1.exists()) {return true;}
		else {System.out.println(name+"�Ѿ�ע����");return false;}
	}
	
	public void delFolder(String folderPath) //ע���÷���
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
}
