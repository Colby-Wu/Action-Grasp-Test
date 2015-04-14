package Server;

import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

//�����-�s�� 
class BrocastThread implements Runnable {
	public ServerSocket ActionGrasp;
	public Socket AG_Connection; 
	private DatagramSocket D_ActionGrasp;
	private DatagramPacket receive ;
	private DataOutputStream out;
	private byte[] recvBytes;

	// �غc�禡
	public BrocastThread(DatagramSocket D_ActionGrasp, ServerSocket ActionGrasp) throws Exception {
		this.D_ActionGrasp = D_ActionGrasp;
		this.ActionGrasp = ActionGrasp;
	}

	public void run() {
		try {
			while(true) {
				// �����s��
				receive = new DatagramPacket(new byte[1024], 1024);  
				D_ActionGrasp.receive(receive);
				recvBytes = Arrays.copyOfRange(
						receive.getData(),
						0,  
						receive.getLength());
				String string=new String(recvBytes);
				// ���console
				System.out.println("Server receive a brocast msg:" +string ); 
				
				// ��XServer IP
				if(!string.isEmpty()) {
					AG_Connection = new Socket(string, 3578);
					// �إߥΤ�ݪ���X��y
					out = new DataOutputStream(AG_Connection.getOutputStream());
					for (int i = 0; i < Main.allIP.size(); i++) {
						out.writeUTF(Main.allIP.get(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();//�C�L���`��T
		} finally {//��finally�y�y���T�O�ʧ@����
			try{
				if(AG_Connection != null){
					AG_Connection.close();
				}
				if(D_ActionGrasp != null){
					D_ActionGrasp.close();//������J��y
				}
			}
			catch(Exception e){
				e.printStackTrace();//�C�L���`��T
			}
		}
	}
}