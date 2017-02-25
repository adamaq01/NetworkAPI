package fr.adamaq01.networkapi.packets;

public class TestPacket implements Packet {

	private String test;

	public TestPacket() {
	}

	public TestPacket(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

}
