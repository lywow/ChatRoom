package server_interface;

import java.io.File;

public class Working {
	static Server_Network sn=new Server_Network();
	
	public Server_Network getsn()
	{
		return sn;
	}
	public static void main(String args[])
	{
		File file=new File("Ⱥ���б�");
		if(!file.exists()) {file.mkdirs();}
		file=new File("�û��б�");
		if(!file.exists()) {file.mkdirs();}
		System.out.println("����������");
		new Thread(sn).start();
	}
}
