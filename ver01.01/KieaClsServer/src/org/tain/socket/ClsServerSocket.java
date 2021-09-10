package org.tain.socket;

import java.net.ServerSocket;
import java.net.Socket;

import org.tain.utils.ClsProp;

public class ClsServerSocket {

	private int hostPort = -1;
	
	public ClsServerSocket() throws Exception {
		this.hostPort = Integer.parseInt(ClsProp.getInstance().get("host.port"));
	}
	
	private ServerSocket serverSocket = null;
	
	public void execute() throws Exception {
		this.serverSocket = new ServerSocket(this.hostPort);
		
		while (true) {
			Socket socket = this.serverSocket.accept();
			Thread thread = new ClsServerSocketThread(socket);
			thread.start();
		}
	}
}
