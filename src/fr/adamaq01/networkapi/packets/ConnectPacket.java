package fr.adamaq01.networkapi.packets;

public class ConnectPacket implements Packet {

	private int protocol;

	public ConnectPacket() {
	}

	public ConnectPacket(int protocol) {
		this.protocol = protocol;
	}

	public int getProtocol() {
		return protocol;
	}

}
