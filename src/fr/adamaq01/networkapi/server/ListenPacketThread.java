package fr.adamaq01.networkapi.server;

import java.io.IOException;

import fr.adamaq01.networkapi.objects.Connection;
import fr.adamaq01.networkapi.packets.ConnectPacket;
import fr.adamaq01.networkapi.packets.Packet;
import fr.adamaq01.networkapi.packets.DisconnectPacket;

public class ListenPacketThread implements Runnable {

	private Thread thread;
	private Server server;

	public ListenPacketThread(String name, Server server) throws IOException {
		thread = new Thread(this);
		thread.setName(name);
		this.server = server;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void run() {
		while (true) {
			for (Connection connection : server.getConnections().values()) {
				try {
					if (connection.getSocket().getInputStream().available() >= 1) {
						byte[] bytes = new byte[1024];
						connection.getSocket().getInputStream().read(bytes);

						String datas = new String(bytes);
						String className = datas.split(":")[0];
						Class<? extends Packet> clazz = (Class<? extends Packet>) Class.forName(className);
						String serialized = datas.replaceFirst(className + ":", "");

						Packet packet = server.mapper.readValue(serialized, clazz);

						if (packet instanceof ConnectPacket) {
							if (server.protocol != ((ConnectPacket) packet).getProtocol()) {
								String message = "";
								if (server.protocol > ((ConnectPacket) packet).getProtocol()) {
									message = "Client outdated !";
								} else {
									message = "Server outdated !";
								}
								server.sendPacket(connection, new DisconnectPacket(message));
								server.onDisconnect(server.getConnections().remove(connection.getId()));
								continue;
							}
						} else {
							server.onPacketReceive(server.getConnections().get(connection.getId()), packet);
						}
					}
				} catch (Exception e) {
					if (e instanceof ClassNotFoundException) {
						server.getConsole()
								.error("Un objet a été recu, mais n'hérite pas de la classe Packet ou n'existe pas !");
						try {
							server.sendPacket(connection, new DisconnectPacket("Un ONI à été envoyé !"));
						} catch (IOException e1) {
						}
					}
					server.onDisconnect(server.getConnections().remove(connection.getId()));
				}
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
