package fr.adamaq01.networkapi.packets;

public class DisconnectPacket implements Packet {

	private String message;

	public DisconnectPacket() {
	}

	public DisconnectPacket(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
