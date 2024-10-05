package ru.laimcraft.proxy.components;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import ru.laimcraft.proxy.Proxy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Servers {
    public final static void registerAll(ProxyServer server) {
        server.registerServer(new ServerInfo("castle", new InetSocketAddress("127.0.0.1", 60200)));
        server.registerServer(new ServerInfo("vanilla", new InetSocketAddress("127.0.0.1", 60120)));
    }
}
