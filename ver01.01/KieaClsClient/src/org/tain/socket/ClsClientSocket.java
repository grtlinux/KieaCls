package org.tain.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.tain.tasks.ResReader;
import org.tain.utils.ClsProp;
import org.tain.utils.Sleep;

public class ClsClientSocket {

	private String hostIp = null;
	private int hostPort = -1;
	private String typeSR = null;
	
	private String resDatFile = "/Users/kang-air/KANG/cls_config/res.dat";
	
	public ClsClientSocket() throws Exception {
		String clientFile = ClsProp.getInstance().get("client.file");
		this.hostIp = ClsProp.getInstance().get("host.ip");
		this.hostPort = Integer.parseInt(ClsProp.getInstance().get("host.port"));
		this.typeSR = ClsProp.getInstance().get("type.sr");
		
		if (!"".equals(clientFile) && clientFile != null)
			this.brFile = new BufferedReader(new FileReader(clientFile));
		this.socket = new Socket(this.hostIp, this.hostPort);
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.pw = new PrintWriter(this.socket.getOutputStream());
		this.loopMSec = Long.parseLong(ClsProp.getInstance().get("loop.wait.msec"));
		
		this.resDatFile = ClsProp.getInstance().get("res.data.file");
	}
	
	private BufferedReader brFile = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	private long loopMSec = -1;
	
	public void executeReq() throws Exception {
		System.out.println(">>>>> execute_Req <<<<<");
		String line = null;
		String msg = null;
		while ((line = this.brFile.readLine()) != null) {
			if (this.typeSR.contains("send")) {
				// send
				this.pw.println(line);
				this.pw.flush();
				System.out.println("client SEND >>>>> " + line);
			}
			
			if (this.typeSR.contains("recv")) {
				// recv length
				// recv data
				msg = this.br.readLine();
				System.out.println("client RECV >>>>> " + msg);
			}
			
			Sleep.run(this.loopMSec);
		}
	}
	
	public void executeRes() throws Exception {
		System.out.println(">>>>> execute_Res <<<<< " + this.resDatFile);
		
		ResReader resReader = new ResReader(this.resDatFile);
		
		while (true) {
			String res = resReader.readLine();
			System.out.println(">>>>> res: [" + res + "]");
			
			if (this.typeSR.contains("send")) {
				// send
				this.pw.println(res);
				this.pw.flush();
				System.out.println("client SEND >>>>> " + res);
			}
		}
	}
}
