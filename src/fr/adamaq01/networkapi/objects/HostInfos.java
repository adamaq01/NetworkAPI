package fr.adamaq01.networkapi.objects;

public class HostInfos {

	private String host;
	private int port;

	public HostInfos(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public static HostInfos fromIPandPort(String ip, int port) {
		return new HostInfos(ip, port);
	}

}
