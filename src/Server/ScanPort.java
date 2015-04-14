package Server; //封裝Server

import java.net.*; //加入JAVA內建網路套件
import java.io.*;  //加入java內建IO套件

public class ScanPort //ScanPort類別,其宣告為class底下的屬性
	{ 
	ServerSocket ActionGrasp; //宣告-伺服網站名稱TCP
	DatagramSocket D_ActionGrasp; //宣告-伺服網站名稱UDP
	ScanPort ScanPort; //找IP
	Thread thread; ////宣告-TCP廣播 
	Thread threadBrocast; ////宣告-UDP廣播 
	

	// 建構函式
	public ScanPort() {	}

	public void setServerPort(int port){
		ScanPort = new ScanPort(); 
		System.out.println("監聽PORT："+port); 
		ScanPort.start(port);  //port 3579
	}

	private void start(int port) {
		try {
			// 執行緒-TCP
			ActionGrasp = new ServerSocket(port); // port由Main.class指定
			thread = new Thread(new ClientThread(ActionGrasp));
			thread.start();

			// 執行緒-UDP
			D_ActionGrasp = new DatagramSocket(8899); // 廣播部分固定port
			threadBrocast = new Thread(new BrocastThread(D_ActionGrasp,ActionGrasp));
			threadBrocast.start();
		}
		catch (IOException ioe) {
			System.out.println("Could not listen on port");
		    System.exit(-1);
		}
		catch (Exception e) {
			System.out.println("No I/O");
		    System.exit(1);
		}
	}
}






