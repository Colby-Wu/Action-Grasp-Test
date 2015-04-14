package Server; //�ʸ�Server

import java.net.*; //�[�JJAVA���غ����M��
import java.io.*;  //�[�Jjava����IO�M��

public class ScanPort //ScanPort���O,��ŧi��class���U���ݩ�
	{ 
	ServerSocket ActionGrasp; //�ŧi-���A�����W��TCP
	DatagramSocket D_ActionGrasp; //�ŧi-���A�����W��UDP
	ScanPort ScanPort; //��IP
	Thread thread; ////�ŧi-TCP�s�� 
	Thread threadBrocast; ////�ŧi-UDP�s�� 
	

	// �غc�禡
	public ScanPort() {	}

	public void setServerPort(int port){
		ScanPort = new ScanPort(); 
		System.out.println("��ťPORT�G"+port); 
		ScanPort.start(port);  //port 3579
	}

	private void start(int port) {
		try {
			// �����-TCP
			ActionGrasp = new ServerSocket(port); // port��Main.class���w
			thread = new Thread(new ClientThread(ActionGrasp));
			thread.start();

			// �����-UDP
			D_ActionGrasp = new DatagramSocket(8899); // �s�������T�wport
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






