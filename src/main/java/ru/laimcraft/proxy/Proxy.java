package ru.laimcraft.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ru.laimcraft.proxy.components.Servers;
import ru.laimcraft.proxy.mysql.MySQLProxy;
import ru.laimcraft.proxy.mysql.SQLManager;

import java.nio.file.Path;
import java.util.*;

@Plugin(id = "proxy", name = "Proxy", version = "1.0", url = "https://github.com/LaimCraft/Proxy", authors = {"LaimCraft"})
public class Proxy {

    public final List<String> AuthPlayers = new ArrayList<>();
    public final ProxyServer server;
    public final Logger logger;
    public static Proxy instance;
    public final Path dir;

    @Inject
    public Proxy(ProxyServer server, Logger logger, @DataDirectory Path dir) throws InterruptedException {
        this.server = server;
        this.logger = logger;
        this.dir = dir;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if(!dir.toFile().exists()) {dir.toFile().mkdir();}
        server.getEventManager().register(this, new EventHandler());
        SQLManager.reset();
        Servers.registerAll(server);
    }

    @Subscribe
    public void onReload(ProxyReloadEvent event) {

        //Сохранение авторизованных игроков и их сессий
    }

    public static Proxy getInstance() {
        return instance;
    }
}
