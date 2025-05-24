package ru.laimcraft.proxy;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import ru.laimcraft.proxy.events.PlayerConnectToServer;
import ru.laimcraft.proxy.mysql.MySQLAccounts;
import ru.laimcraft.proxy.rpc.RPC;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class EventHandler {
    private Proxy proxy = Proxy.instance;

    @Subscribe
    public void onPlayerConnect(LoginEvent event) {
        Proxy.logger.info("  " + event.getPlayer().getUsername() + " " + event.getPlayer().isOnlineMode());
    }

    @Subscribe
    public void onServerPostConnectEvent(ServerPostConnectEvent event) {
        new PlayerConnectToServer(proxy, event);
    }

    @Subscribe
    public void onServerConnectEvent(ServerConnectedEvent event) throws URISyntaxException {
    }

    /*@Subscribe
    public void onMessage(PluginMessageEvent event) {
        switch (event.getIdentifier().getId()) {
            case "laimcraft:proxy":
                ByteArrayDataInput input = event.dataAsDataStream();
                String request = input.readUTF();
                if (request.equalsIgnoreCase("transfer")) {
                    String serverName = input.readUTF();
                    String login = input.readUTF();
                    Player player = Proxy.server.getPlayer(login).get();
                    RegisteredServer server = Proxy.server.getServer(serverName).get();

                    ConnectionRequestBuilder connectionRequestBuilder =
                            player.createConnectionRequest(server);
                    CompletableFuture<ConnectionRequestBuilder.Result> result =
                            connectionRequestBuilder.connect();
                    return;
                } if (request.equalsIgnoreCase("login")) {
                String login = input.readUTF();
                Player player = Proxy.server.getPlayer(login).get();
                Proxy.authPlayers.put(login, player);
                return;
            } else return;
            default:
                return;
        }
    }*/

    @Subscribe
    public void onServer(ServerConnectedEvent event) {
        if (event.getServer().getServerInfo().getName().equals("lobby")) {
            if (!Proxy.authPlayers.containsKey(event.getPlayer().getUsername())) { // Игрок не авторизирован
                String login = MySQLAccounts.getLoginByLogin(event.getPlayer().getUsername());
                if (login == null) {
                    RPC.getServer(RPC.ServerName.LOBBY).sendMessage(String.format("message-to-player %s %s",
                            event.getPlayer().getUsername(), GsonComponentSerializer.gson().serialize(Messages.registerSendMessage)));
                    return;
                } else {
                    RPC.getServer(RPC.ServerName.LOBBY).sendMessage(String.format("message-to-player %s %s",
                            event.getPlayer().getUsername(), GsonComponentSerializer.gson().serialize(Messages.loginSendMessage)));
                    return;
                }
            } else {
                RPC.getServer(RPC.ServerName.LOBBY).sendMessage(String.format("login %s", event.getPlayer().getUsername()));
                return;
            }
        }
    }

    @Subscribe
    public void onSrv(ServerPreConnectEvent event) {
        /*proxy.logger.info("======ServerPreConnectEvent " + event.getPlayer().getUsername() + " original "
                + event.getOriginalServer() + ", previous " + event.getPreviousServer());*/
        // original - Сервер на который я захожу, previous - Сервер на котором я был до этого, при первом заходе previous = null
        if (!event.getOriginalServer().getServerInfo().getName().equals("lobby")) {
            if (Proxy.authPlayers.containsKey(event.getPlayer().getUsername())) return;
            String message = "Нельзя зайти на сервер пока вы не авторизовались.";
            Component component = Component.text(message)
                    .color(TextColor.color(0xFF0000));
            event.getPlayer().sendMessage(component);
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
            return;
        }
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        Proxy.authPlayers.remove(event.getPlayer().getUsername());
    }
}
