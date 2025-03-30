package ru.laimcraft.proxy.api.message.server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.laimcraft.proxy.api.message.ServerMessage;

public class TestServerMessage extends ServerMessage {
    public TestServerMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void acceptServer() {
        Player player = Bukkit.getPlayer("limeworld");
        if(player == null) return;
        player.sendMessage("TestServerMessage");
    }
}
