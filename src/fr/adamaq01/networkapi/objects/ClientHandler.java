package fr.adamaq01.networkapi.objects;

import fr.adamaq01.networkapi.packets.Packet;

/**
 * Created by Adamaq01 on 25/02/2017.
 */
public interface ClientHandler {

    public void onPacketReceive(Packet packet);

}
