package ru.laimcraft.proxy.components;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import ru.laimcraft.proxy.Proxy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Servers {
    public static ServerInfo info = new ServerInfo("lobby", new InetSocketAddress("127.0.0.1", 60100));
    public final static void registerAll(ProxyServer server) {
        server.registerServer(new ServerInfo("castle", new InetSocketAddress("127.0.0.1", 60200)));
        server.registerServer(new ServerInfo("vanilla", new InetSocketAddress("127.0.0.1", 60120)));
        server.registerServer(new ServerInfo("anarchy", new InetSocketAddress("127.0.0.1", 60210)));
        server.registerServer(new ServerInfo("roleplay", new InetSocketAddress("127.0.0.1", 60250)));
        server.registerServer(new ServerInfo("rpb", new InetSocketAddress("127.0.0.1", 25670)));
        server.registerServer(new ServerInfo("pillarsoffortune", new InetSocketAddress("127.0.0.1", 60421)));
        server.registerServer(new ServerInfo("oceanworld", new InetSocketAddress("127.0.0.1", 6740)));
        server.registerServer(new ServerInfo("skyblock", new InetSocketAddress("127.0.0.1", 60400)));
        server.registerServer(new ServerInfo("grif", new InetSocketAddress("127.0.0.1", 60450)));
        //Порты с 60451 по 60459 заняты VDS для сервера grif и туннеля
        // 60451 SFTP
    }
}
