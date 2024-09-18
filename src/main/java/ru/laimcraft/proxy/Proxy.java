package ru.laimcraft.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(id = "proxy", name = "Proxy", version = "1.0", url = "https://github.com/LaimCraft/Proxy", authors = {"LaimCraft"})
public class Proxy {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        
    }
}
