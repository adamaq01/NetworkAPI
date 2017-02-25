package fr.adamaq01.networkapi.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.adamaq01.networkapi.objects.Connection;
import fr.adamaq01.networkapi.objects.ServerHandler;

public class ConnectThread implements Runnable {

	private Thread thread;
	private Server server;

	public ConnectThread(String name, Server server) throws IOException {
		thread = new Thread(this);
		thread.setName(name);
		this.server = server;
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = server.getSocket().accept();
				List<Socket> sockets = new ArrayList<>();
				for (Connection connection : server.getConnections().values()) {
					sockets.add(connection.getSocket());
				}
				if (!sockets.contains(socket)) {
					server.connections++;
					Connection connection = new Connection(server.connections, socket);
					server.getConnections().put(connection.getId(), connection);
					server.getConsole().info(
							socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " has connected !");
					server.onConnect(connection);
					for(ServerHandler handler : server.getHandlers()) {
						handler.onConnect(connection);
					}
				}
			} catch (IOException e) {
			}
			try {
				thread.sleep(1000 / server.getTPS());
			} catch (InterruptedException e) {
			}
		}
	}

	public void start() {
		thread.start();
	}

}
