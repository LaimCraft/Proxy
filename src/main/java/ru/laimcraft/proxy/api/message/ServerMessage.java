package ru.laimcraft.proxy.api.message;

public class ServerMessage extends Message {

    public ServerMessage(byte[] bytes) {
        super(bytes);
    }

    public void acceptServer() {}
}
