package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

//執行緒-客戶端操作
class ClientThread implements Runnable {
	public ServerSocket ActionGrasp; 
	public Socket AG_Connection;
	private DataInputStream  in; 
	private DataOutputStream out; 

	// 建構函式
	public ClientThread(ServerSocket ActionGrasp) throws Exception {
		this.ActionGrasp = ActionGrasp;
	}

	public void run() {
		try {
			while(true) {
				// 接受用戶端的連線請求
				AG_Connection = ActionGrasp.accept();
				// 建立用戶端的輸入串流				
				in  = new DataInputStream (AG_Connection.getInputStream());
				// 建立用戶端的輸出串流
				out = new DataOutputStream(AG_Connection.getOutputStream());

				String now=Calendar.HOUR+":"+Calendar.MINUTE+":"+Calendar.SECOND+"";
				// 至客戶端接收的資料				
				String FromClient = in.readUTF();
				System.out.println("客戶端送來的訊息：\""+FromClient+"\"@"+now);
				
				ControlWMP wmp = new ControlWMP();
				ControlPPT ppt = new ControlPPT();
				ControlComputer Computer = new ControlComputer();
				Folder FD = new Folder();

				//電腦控制部分
				if(FromClient.equalsIgnoreCase("TESTTEST123123")) {
					System.out.println("!!!!!!!!!!!!!!!!!!!");
					out.writeUTF("Connected");
				}
				else if(FromClient.equalsIgnoreCase("MRCode_CC_00")) Computer.sleep(); //休眠
				else if(FromClient.equalsIgnoreCase("MRCode_CC_01")) Computer.reset(); //重新開機
				else if(FromClient.equalsIgnoreCase("MRCode_CC_02")) Computer.powerOff(); //關機

				//WMP控制部分
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_00")) wmp.Close(); //關閉播放器
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_01")) wmp.Random(); //隨機播放
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_02")) wmp.Repick(); //重複播放
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_10")) wmp.Pause(); //繼續或暫停
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_11")) wmp.fullScreen(); //全畫面-取消全畫面
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_12")) wmp.PageUP(); //上一首
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_13")) wmp.PageDown(); //下一首
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_14")) wmp.Mute(); //靜音-取消靜音
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_15")) wmp.VolumeIncrease(); //增加音量
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_16")) wmp.VolumeReduce(); //降低音量
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_17")) wmp.FastPlay(); //加速播放
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_18")) wmp.SlowPlay(); //減速播放
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_19")) wmp.StopPlay(); //停止播放

				//PPT控制部分
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_00")) ppt.Close(); //關閉簡報
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_10")) ppt.Slide(); //進入投影片模式
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_11")) ppt.Esc(); //結束簡報 ESC
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_12")) ppt.PageUP(); //上一頁
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_13")) ppt.PageDown(); //下一頁
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_14")) ppt.VolumeIncrease(); //增加音量
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_15")) ppt.VolumeReduce(); //降低音量

				//接收到客戶端廣播欲取得伺服端IP資料
				else if(FromClient.equalsIgnoreCase("MRCode_Return")) {
					ActionGrasp.getInetAddress();
					out.writeUTF(InetAddress.getLocalHost().getHostAddress());
				} 
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Music")) out.writeUTF(FD.FolderSelect(1));  //傳回音樂資料夾檔案
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Videos")) out.writeUTF(FD.FolderSelect(2));  //傳回影片資料夾檔案
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Documents")) out.writeUTF(FD.FolderSelect(3));  //傳回簡報資料夾檔案

				//客戶端要求開啟檔案
				else
				{
					int Run_ok = FD.Strat_File(FromClient.trim());
					if(Run_ok==1){
						out.writeUTF("執行檔案成功！");
					}else if(Run_ok==-1){
						out.writeUTF("執行檔案失敗！");
					}else
						out.writeUTF("錯誤的指令！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();//列印異常資訊
		} finally {//用finally語句塊確保動作執行
			try{
				if(in != null){
					in.close();//關閉輸入串流
				}
				if(out != null){
					out.close();//關閉輸入串流
				}
				if(AG_Connection != null){
					AG_Connection.close();//關閉Socket連接
				}
			}
			catch(Exception e){
				e.printStackTrace();//列印異常資訊
			}
		}
	}

}