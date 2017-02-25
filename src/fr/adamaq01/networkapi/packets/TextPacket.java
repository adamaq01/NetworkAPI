package fr.adamaq01.networkapi.packets;

/**
 * Created by Adamaq01 on 25/02/2017.
 */
public class TextPacket implements Packet {

    private String text;

    public TextPacket() {
        this("");
    }

    public TextPacket(String test) {
        this.text = test;
    }

    public String getTest() {
        return text;
    }

}
