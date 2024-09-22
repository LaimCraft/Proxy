package ru.laimcraft.proxy.events;

import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import ru.laimcraft.proxy.Proxy;

public class PlayerConnectToServer {
    private final Proxy proxy;
    private final ServerPostConnectEvent event;
    public PlayerConnectToServer(Proxy proxy, ServerPostConnectEvent event) {
        this.proxy = proxy;
        this.event = event;
        start();
    }

    private void start() {
        // PUSTO
    }
}
