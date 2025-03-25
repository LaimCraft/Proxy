package ru.laimcraft.proxy.api.message.server;

import ru.laimcraft.proxy.api.message.ServerMessage;

public class LobbyServerMessage extends ServerMessage {
    public LobbyServerMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void acceptServer() {

    }
}
