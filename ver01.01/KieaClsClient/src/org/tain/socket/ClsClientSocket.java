package org.tain.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.tain.utils.ClsProp;
import org.tain.utils.Sleep;

public class ClsClientSocket {

	private String hostIp = null;
	private int hostPort = -1;
	
	public ClsClientSocket() throws Exception {
		String clientFile = ClsProp.getInstance().get("client.file");
		this.hostIp = ClsProp.getInstance().get("host.ip");
		this.hostPort = Integer.parseInt(ClsProp.getInstance().get("host.port"));
		
		this.brFile = new BufferedReader(new FileReader(clientFile));
		this.socket = new Socket(this.hostIp, this.hostPort);
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.pw = new PrintWriter(this.socket.getOutputStream());
		this.loopMSec = Long.parseLong(ClsProp.getInstance().get("loop.wait.msec"));
	}
	
	private BufferedReader brFile = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	private long loopMSec = -1;
	
	public void execute() throws Exception {
		String line = null;
		String msg = null;
		while ((line = this.brFile.readLine()) != null) {
			// send
			this.pw.println(line);
			this.pw.flush();
			System.out.println("client SEND >>>>> " + line);
			
			// recv length
			// recv data
			msg = this.br.readLine();
			System.out.println("client RECV >>>>> " + msg);
			
			Sleep.run(this.loopMSec);
		}
	}
}
