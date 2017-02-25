package fr.adamaq01.networkapi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.adamaq01.networkapi.objects.Connection;
import fr.adamaq01.networkapi.objects.Console;
import fr.adamaq01.networkapi.packets.Packet;

public abstract class Server implements Runnable {

	private Thread thread;
	private Console console;
	private ServerSocket socket;
	private int port;
	private String logo;
	private int tps;
	protected ObjectMapper mapper;
	protected int protocol;

	private LinkedHashMap<Integer, Connection> connected = new LinkedHashMap<>();
	protected int connections = 0;

	public Server(String name, int port, int protocol, int tps) throws IOException {
		this.tps = tps;
		this.thread = new Thread(this);
		this.thread.setName(name);
		this.port = port;
		this.mapper = new ObjectMapper();
		this.logo = "[" + name + "]";
		this.console = new Console(name) {
			@Override
			public void onCommand(String command, String[] args) {
				if (command.equalsIgnoreCase("stop"))
					stop();
				Server.this.onCommand(command, args);
			}
		};
		this.protocol = protocol;
	}

	@Override
	public void run() {
		try {
			new Thread(new ConnectThread(getLogo(), this)).start();
			new Thread(new ListenPacketThread(getLogo(), this)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			socket = new ServerSocket(port);
			console.info("Serveur prêt aux connections");
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread.start();
		console.info("Serveur ouvert.");
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		thread.stop();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		console.info("Serveur fermé.");
		System.exit(0);
	}

	public void info(String message) {
		console.info(logo + " " + message);
	}

	public void error(String message) {
		console.error(logo + " " + message);
	}

	public Console getConsole() {
		return console;
	}

	public String getLogo() {
		return logo;
	}

	public int getTPS() {
		return tps;
	}

	public LinkedHashMap<Integer, Connection> getConnections() {
		return connected;
	}

	public ServerSocket getSocket() {
		return socket;
	}

	public void sendPacket(Connection connection, Packet packet) throws IOException {
		String serialized = mapper.writeValueAsString(packet);
		String data = packet.getClass().getName() + ":" + serialized;
		connection.getSocket().getOutputStream().write(data.getBytes());
		connection.getSocket().getOutputStream().flush();
	}

	public abstract void onCommand(String command, String[] args);

	public abstract void onPacketReceive(Connection connection, Packet packet);

	public abstract void onConnect(Connection connection);

	public abstract void onDisconnect(Connection connection);

}
