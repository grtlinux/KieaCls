package org.tain.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
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
	private InputStream is = null;
	public ClsServerSocketThread(Socket socket) throws Exception {
		String serverFile = ClsProp.getInstance().get("server.file");
		this.brFile = new BufferedReader(new FileReader(serverFile));
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.is = this.socket.getInputStream();
		this.pw = new PrintWriter(this.socket.getOutputStream());
		this.typeSR = ClsProp.getInstance().get("type.sr");
		this.loopMSec = Long.parseLong(ClsProp.getInstance().get("loop.wait.msec"));
	}
	
	public void run() {
		try {
			String line = null;
			String msg = null;
			byte[] bMsg = new byte[1024];
			//while ((line = this.brFile.readLine()) != null) {
			//while (true) {
			for (int i=0;;i++) {
				if (this.typeSR.contains("recv")) {
					// recv header info
					this.is.read(bMsg, 0, 5);
					// recv length
					this.is.read(bMsg, 5, 4);
					int len = Integer.parseInt(new String(bMsg, 5, 4));
					// recv data
					this.is.read(bMsg, 9, len);
					
					msg = new String(bMsg, 0, 9 + len);
					System.out.println("server " + i + ". RECV >>>>> " + msg);
				}
				
				if (this.typeSR.contains("send")) {
					// send
					this.pw.println(line);
					this.pw.flush();
					System.out.println("server " + i + ". SEND >>>>> " + line);
				}
				
				Sleep.run(this.loopMSec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
