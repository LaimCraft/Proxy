package ru.laimcraft.proxy.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.components.Servers;

import java.util.concurrent.CompletableFuture;

public class ToLobby implements SimpleCommand {

    @Override
    public void execute(Invocation data) {
        Player player = (Player) data.source();
        if(player.getCurrentServer().get().getServerInfo().getName().equals("lobby")) return;
        RegisteredServer server = Proxy.server.getServer("lobby").get();

        ConnectionRequestBuilder connectionRequestBuilder =
                player.createConnectionRequest(server);
        CompletableFuture<ConnectionRequestBuilder.Result> result =
                connectionRequestBuilder.connect();
    }
}
