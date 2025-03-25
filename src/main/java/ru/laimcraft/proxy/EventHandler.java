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
import ru.laimcraft.proxy.events.PlayerConnectToServer;
import ru.laimcraft.proxy.mysql.MySQLAccounts;

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

    private boolean validateToken(String accessToken) {
        String urlString = "https://sessionserver.mojang.com/session/minecraft/validate";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setDoOutput(true);

            // Отправляем пустое тело запроса
            try (OutputStream os = connection.getOutputStream()) {
                os.write(new byte[0]);
            }

            int responseCode = connection.getResponseCode();
            return responseCode == 204; // 204 No Content означает, что токен действителен

        } catch (IOException e) {
            e.printStackTrace();
            return false; // В случае ошибки считаем токен недействительным
        }
    }

    @Subscribe
    public void onServerPostConnectEvent(ServerPostConnectEvent event) {
        new PlayerConnectToServer(proxy, event);
    }

    @Subscribe
    public void onServerConnectEvent(ServerConnectedEvent event) throws URISyntaxException {
    }

    @Subscribe
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
    }

    @Subscribe
    public void onServer(ServerConnectedEvent event) throws InterruptedException {
        Proxy.server.getServer("lobby").get().sendMessage(Component.text("hi"));
        if (event.getServer().getServerInfo().getName().equals("lobby")) {
            if (!Proxy.authPlayers.containsKey(event.getPlayer().getUsername())) { // Игрок не авторизирован
                String login = MySQLAccounts.getLoginByLogin(event.getPlayer().getUsername());
                if(login == null) {
                    event.getPlayer().sendMessage(Messages.registerSendMessage);
                    return;}
                event.getPlayer().sendMessage(Messages.loginSendMessage);
            }
        }
    }

    @Subscribe
    public void onSrv(ServerPreConnectEvent event) throws InterruptedException {
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
