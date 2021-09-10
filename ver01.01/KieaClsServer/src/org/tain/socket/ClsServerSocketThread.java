package org.tain.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.tain.utils.ClsProp;

public class ClsServerSocketThread extends Thread {

	private BufferedReader brFile = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	public ClsServerSocketThread(Socket socket) throws Exception {
		String serverFile = ClsProp.getInstance().get("server.file");
		this.brFile = new BufferedReader(new FileReader(serverFile));
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.pw = new PrintWriter(this.socket.getOutputStream());
	}
	
	public void run() {
		try {
			String line = null;
			String msg = null;
			while ((line = this.brFile.readLine()) != null) {
				// recv length
				// recv data
				msg = this.br.readLine();
				System.out.println("RECV >>>>> " + msg);
				
				// send
				this.pw.print(line);
				this.pw.flush();
				System.out.println("SEND >>>>> " + line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
