package ru.laimcraft.proxy.api.message.proxy;

import net.kyori.adventure.text.Component;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.api.message.ProxyMessage;

public class TestProxyMessage extends ProxyMessage {
    public TestProxyMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void acceptProxyServer() {
        Proxy.server.getPlayer("limeworld").ifPresent((player) -> {
            player.sendMessage(Component.text("TestProxyMessage"));
        });
    }
}
