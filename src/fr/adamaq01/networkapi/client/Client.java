package fr.adamaq01.networkapi.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.adamaq01.networkapi.objects.HostInfos;
import fr.adamaq01.networkapi.packets.ConnectPacket;
import fr.adamaq01.networkapi.packets.Packet;
import fr.adamaq01.networkapi.packets.DisconnectPacket;

public abstract class Client implements Runnable {

	private Socket socket;
	private Thread thread;
	private HostInfos infos;
	private ObjectMapper mapper;
	private int protocol;

	public Client(String name, HostInfos infos, int protocol) {
		this.thread = new Thread(this);
		this.thread.setName(name);
		this.infos = infos;
		this.mapper = new ObjectMapper();
		this.protocol = protocol;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void run() {
		while (true) {
			try {
				if (socket.getInputStream().available() >= 1) {
					byte[] bytes = new byte[1234];
					socket.getInputStream().read(bytes);

					String datas = new String(bytes);
					String className = datas.split(":")[0];
					Class<? extends Packet> clazz = (Class<? extends Packet>) Class.forName(className);
					String serialized = datas.replaceFirst(className + ":", "");

					Packet packet = mapper.readValue(serialized, clazz);
					onPacketReceive(packet);
					if (packet instanceof DisconnectPacket) {
						socket.close();
						thread.stop();
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendPacket(Packet packet) throws IOException {
		try {
			String serialized = mapper.writeValueAsString(packet);
			String datas = packet.getClass().getName() + ":" + serialized;
			socket.getOutputStream().write(datas.getBytes());
			socket.getOutputStream().flush();
		} catch (JsonProcessingException e) {
			socket.close();
		}
	}

	public void connect() {
		try {
			socket = new Socket(infos.getHost(), infos.getPort());
			socket.setSoTimeout(10000);
			socket.setKeepAlive(true);
			thread.start();
			while (true) {
				if (socket.isConnected()) {
					System.out.println("Sent Connect Packet !");
					sendPacket(new ConnectPacket(protocol));
					break;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("L'ip précisée est introuvable.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void onPacketReceive(Packet packet);

}
