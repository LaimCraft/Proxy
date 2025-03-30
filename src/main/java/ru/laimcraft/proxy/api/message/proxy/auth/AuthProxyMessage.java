package ru.laimcraft.proxy.api.message.proxy.auth;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.api.message.ProxyMessage;

import java.util.Optional;

public class AuthProxyMessage extends ProxyMessage {

    public AuthProxyMessage(byte[] bytes) {
        super(bytes);
    }

    public AuthProxyMessage(String login) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF(login);
        super(byteArrayDataOutput.toByteArray());
    }

    @Override
    public void acceptProxyServer() {
        ByteArrayDataInput dataInput = getByteArrayDataInput();
        String username = dataInput.readUTF();
        Optional<Player> player = Proxy.server.getPlayer(username);
        if(player.isEmpty()) return;
        Proxy.authPlayers.put(username, player.get());
    }
}