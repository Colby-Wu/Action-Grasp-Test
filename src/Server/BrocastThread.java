package Server;

import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

//執行緒-廣播 
class BrocastThread implements Runnable {
	public ServerSocket ActionGrasp;
	public Socket AG_Connection; 
	private DatagramSocket D_ActionGrasp;
	private DatagramPacket receive ;
	private DataOutputStream out;
	private byte[] recvBytes;

	// 建構函式
	public BrocastThread(DatagramSocket D_ActionGrasp, ServerSocket ActionGrasp) throws Exception {
		this.D_ActionGrasp = D_ActionGrasp;
		this.ActionGrasp = ActionGrasp;
	}

	public void run() {
		try {
			while(true) {
				// 接收廣播
				receive = new DatagramPacket(new byte[1024], 1024);  
				D_ActionGrasp.receive(receive);
				recvBytes = Arrays.copyOfRange(
						receive.getData(),
						0,  
						receive.getLength());
				String string=new String(recvBytes);
				// 顯示console
				System.out.println("Server receive a brocast msg:" +string ); 
				
				// 輸出Server IP
				if(!string.isEmpty()) {
					AG_Connection = new Socket(string, 3578);
					// 建立用戶端的輸出串流
					out = new DataOutputStream(AG_Connection.getOutputStream());
					for (int i = 0; i < Main.allIP.size(); i++) {
						out.writeUTF(Main.allIP.get(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();//列印異常資訊
		} finally {//用finally語句塊確保動作執行
			try{
				if(AG_Connection != null){
					AG_Connection.close();
				}
				if(D_ActionGrasp != null){
					D_ActionGrasp.close();//關閉輸入串流
				}
			}
			catch(Exception e){
				e.printStackTrace();//列印異常資訊
			}
		}
	}
}