package fr.adamaq01.networkapi.example;

import fr.adamaq01.networkapi.objects.Connection;
import fr.adamaq01.networkapi.packets.Packet;
import fr.adamaq01.networkapi.packets.TextPacket;
import fr.adamaq01.networkapi.server.Server;

import java.io.IOException;

/**
 * Created by Adamaq01 on 25/02/2017.
 */
public class TestServer extends Server {

    public TestServer() throws IOException {
        super("TestServer", 9063, 4, 128);
    }

    @Override
    public void onCommand(String command, String[] args) {
        info("Hello, nice command: " + command);
    }

    @Override
    public void onPacketReceive(Connection connection, Packet packet) {
        if(packet instanceof TextPacket) {
            try {
                sendPacket(connection, new TextPacket("Nice text!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnect(Connection connection) {
        info("HEY! :)");
    }

    @Override
    public void onDisconnect(Connection connection) {
        info("BYE! :(");
    }
}
