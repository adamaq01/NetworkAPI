package fr.adamaq01.networkapi.objects;

import fr.adamaq01.networkapi.packets.Packet;

/**
 * Created by Adamaq01 on 25/02/2017.
 */
public interface ServerHandler {

    public void onCommand(String command, String[] args);

    public void onPacketReceive(Connection connection, Packet packet);

    public void onConnect(Connection connection);

    public void onDisconnect(Connection connection);

}
