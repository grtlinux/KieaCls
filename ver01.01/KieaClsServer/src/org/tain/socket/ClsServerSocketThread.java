package org.tain.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.tain.utils.ClsProp;
import org.tain.utils.Sleep;

public class ClsServerSocketThread extends Thread {

	private BufferedReader brFile = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private String typeSR = null;
	private long loopMSec = -1;
	
	public ClsServerSocketThread(Socket socket) throws Exception {
		String serverFile = ClsProp.getInstance().get("server.file");
		this.brFile = new BufferedReader(new FileReader(serverFile));
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.pw = new PrintWriter(this.socket.getOutputStream());
		this.typeSR = ClsProp.getInstance().get("type.sr");
		this.loopMSec = Long.parseLong(ClsProp.getInstance().get("loop.wait.msec"));
	}
	
	public void run() {
		try {
			String line = null;
			String msg = null;
			while ((line = this.brFile.readLine()) != null) {
				if (this.typeSR.contains("recv")) {
					// recv length
					// recv data
					msg = this.br.readLine();
					System.out.println("server RECV >>>>> " + msg);
				}
				
				if (this.typeSR.contains("send")) {
					// send
					this.pw.println(line);
					this.pw.flush();
					System.out.println("server SEND >>>>> " + line);
				}
				
				Sleep.run(this.loopMSec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
