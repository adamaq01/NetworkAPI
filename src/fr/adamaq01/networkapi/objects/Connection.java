package fr.adamaq01.networkapi.objects;

import java.net.Socket;

public class Connection {

	private int id;
	private Socket socket;

	public Connection(int id, Socket socket) {
		this.id = id;
		this.socket = socket;
	}

	public int getId() {
		return id;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}

}
