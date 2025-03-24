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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class EventHandler {
    private Proxy proxy = Proxy.getInstance();

    @Subscribe
    public void onPlayerConnect(LoginEvent event) {
        Proxy.getInstance().logger.info("  " + event.getPlayer().getUsername() + " " + event.getPlayer().isOnlineMode());
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
            case "server:transfer":
                ByteArrayDataInput input = event.dataAsDataStream();
                String key = input.readUTF();
                if (!key.equals(Proxy.getInstance().secret)) return;
                String request = input.readUTF();
                if (request.equalsIgnoreCase("transfer")) {
                    String serverName = input.readUTF();
                    String login = input.readUTF();
                    Player player = Proxy.getInstance().server.getPlayer(login).get();
                    RegisteredServer server = Proxy.getInstance().server.getServer(serverName).get();

                    ConnectionRequestBuilder connectionRequestBuilder =
                            player.createConnectionRequest(server);
                    CompletableFuture<ConnectionRequestBuilder.Result> result =
                            connectionRequestBuilder.connect();
                }

                String login = Utils.byteArrayToString(event.getData());
                if (login == null) return;
                if (Proxy.getInstance().server.getAllPlayers().contains(login)) {
                    Proxy.getInstance().server.getPlayer(login).get().transferToHost
                            (Proxy.getInstance().server.getServer("castle").get().getServerInfo().getAddress());
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Subscribe
    public void onSrv(ServerPreConnectEvent event) throws InterruptedException {
        if (event.getOriginalServer().getServerInfo().getName().equals("lobby")) return;
        if (Proxy.authPlayers.containsKey(event.getPlayer().getUsername())) return;
        String message = "Нельзя зайти на сервер пока вы не авторизовались.";
        Component component = Component.text(message)
                .color(TextColor.color(0xFF0000));
        event.getPlayer().sendMessage(component);
        event.setResult(ServerPreConnectEvent.ServerResult.denied());
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        Proxy.authPlayers.remove(event.getPlayer().getUsername());
    }
}
