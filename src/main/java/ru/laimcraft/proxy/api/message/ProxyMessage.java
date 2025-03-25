package ru.laimcraft.proxy.api.message;

public class ProxyMessage extends Message {

    public ProxyMessage(byte[] bytes) {
        super(bytes);
    }

    public void acceptProxyServer() {}
}