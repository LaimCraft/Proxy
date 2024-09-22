package ru.laimcraft.proxy;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import ru.laimcraft.proxy.components.Servers;
import ru.laimcraft.proxy.events.PlayerConnectToServer;
import ru.laimcraft.proxy.mysql.MySQLProxy;
import ru.laimcraft.proxy.mysql.SQLManager;

public class EventHandler {
    private Proxy proxy = Proxy.getInstance();
    @Subscribe
    public void onServerPostConnectEvent(ServerPostConnectEvent event) {
        new PlayerConnectToServer(proxy, event);
    }

    @Subscribe
    public void onServerConnectEvent(ServerConnectedEvent event) {
    }

    @Subscribe
    public void onSrv(ServerPreConnectEvent event) throws InterruptedException {
        if(event.getOriginalServer().getServerInfo().getName().equals("lobby")) return;
        if(proxy.AuthPlayers.contains(event.getPlayer().getUsername())) return;
        String message = "Нельзя зайти на сервер пока вы не авторизовались.";
        Component component = Component.text(message)
                .color(TextColor.color(0xFF0000));
        event.getPlayer().sendMessage(component);
        event.setResult(ServerPreConnectEvent.ServerResult.denied());
    }

    @Subscribe
    public void onEventE(PlayerAvailableCommandsEvent event) {
        String serverName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();
        switch (serverName) {
            default:
                event.getRootNode();
                return;
        }
    }

    @Subscribe
    public void onLeave(DisconnectEvent event) {
        if(!proxy.AuthPlayers.contains(event.getPlayer().getUsername())) return;
        proxy.AuthPlayers.remove(event.getPlayer().getUsername());

    }
}
