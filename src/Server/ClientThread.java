package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

//�����-�Ȥ�ݾާ@
class ClientThread implements Runnable {
	public ServerSocket ActionGrasp; 
	public Socket AG_Connection;
	private DataInputStream  in; 
	private DataOutputStream out; 

	// �غc�禡
	public ClientThread(ServerSocket ActionGrasp) throws Exception {
		this.ActionGrasp = ActionGrasp;
	}

	public void run() {
		try {
			while(true) {
				// �����Τ�ݪ��s�u�ШD
				AG_Connection = ActionGrasp.accept();
				// �إߥΤ�ݪ���J��y				
				in  = new DataInputStream (AG_Connection.getInputStream());
				// �إߥΤ�ݪ���X��y
				out = new DataOutputStream(AG_Connection.getOutputStream());

				String now=Calendar.HOUR+":"+Calendar.MINUTE+":"+Calendar.SECOND+"";
				// �ܫȤ�ݱ��������				
				String FromClient = in.readUTF();
				System.out.println("�Ȥ�ݰe�Ӫ��T���G\""+FromClient+"\"@"+now);
				
				ControlWMP wmp = new ControlWMP();
				ControlPPT ppt = new ControlPPT();
				ControlComputer Computer = new ControlComputer();
				Folder FD = new Folder();

				//�q�������
				if(FromClient.equalsIgnoreCase("TESTTEST123123")) {
					System.out.println("!!!!!!!!!!!!!!!!!!!");
					out.writeUTF("Connected");
				}
				else if(FromClient.equalsIgnoreCase("MRCode_CC_00")) Computer.sleep(); //��v
				else if(FromClient.equalsIgnoreCase("MRCode_CC_01")) Computer.reset(); //���s�}��
				else if(FromClient.equalsIgnoreCase("MRCode_CC_02")) Computer.powerOff(); //����

				//WMP�����
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_00")) wmp.Close(); //��������
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_01")) wmp.Random(); //�H������
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_02")) wmp.Repick(); //���Ƽ���
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_10")) wmp.Pause(); //�~��μȰ�
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_11")) wmp.fullScreen(); //���e��-�������e��
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_12")) wmp.PageUP(); //�W�@��
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_13")) wmp.PageDown(); //�U�@��
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_14")) wmp.Mute(); //�R��-�����R��
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_15")) wmp.VolumeIncrease(); //�W�[���q
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_16")) wmp.VolumeReduce(); //���C���q
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_17")) wmp.FastPlay(); //�[�t����
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_18")) wmp.SlowPlay(); //��t����
				else if(FromClient.equalsIgnoreCase("MRCode_WMP_19")) wmp.StopPlay(); //�����

				//PPT�����
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_00")) ppt.Close(); //����²��
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_10")) ppt.Slide(); //�i�J��v���Ҧ�
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_11")) ppt.Esc(); //����²�� ESC
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_12")) ppt.PageUP(); //�W�@��
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_13")) ppt.PageDown(); //�U�@��
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_14")) ppt.VolumeIncrease(); //�W�[���q
				else if(FromClient.equalsIgnoreCase("MRCode_PPT_15")) ppt.VolumeReduce(); //���C���q

				//������Ȥ�ݼs�������o���A��IP���
				else if(FromClient.equalsIgnoreCase("MRCode_Return")) {
					ActionGrasp.getInetAddress();
					out.writeUTF(InetAddress.getLocalHost().getHostAddress());
				} 
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Music")) out.writeUTF(FD.FolderSelect(1));  //�Ǧ^���ָ�Ƨ��ɮ�
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Videos")) out.writeUTF(FD.FolderSelect(2));  //�Ǧ^�v����Ƨ��ɮ�
				else if(FromClient.equalsIgnoreCase("MRCode_Show_Documents")) out.writeUTF(FD.FolderSelect(3));  //�Ǧ^²����Ƨ��ɮ�

				//�Ȥ�ݭn�D�}���ɮ�
				else
				{
					int Run_ok = FD.Strat_File(FromClient.trim());
					if(Run_ok==1){
						out.writeUTF("�����ɮצ��\�I");
					}else if(Run_ok==-1){
						out.writeUTF("�����ɮץ��ѡI");
					}else
						out.writeUTF("���~�����O�I");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();//�C�L���`��T
		} finally {//��finally�y�y���T�O�ʧ@����
			try{
				if(in != null){
					in.close();//������J��y
				}
				if(out != null){
					out.close();//������J��y
				}
				if(AG_Connection != null){
					AG_Connection.close();//����Socket�s��
				}
			}
			catch(Exception e){
				e.printStackTrace();//�C�L���`��T
			}
		}
	}

}