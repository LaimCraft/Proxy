package ru.laimcraft.proxy.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.api.message.server.LobbyServerMessage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LaimCraftServer {

    public Plugin plugin;
    public Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;

    public LaimCraftServer(Plugin plugin) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 25560);

                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                while (true) {
                    if (objectInputStream.readObject() instanceof final LobbyServerMessage lobbyServerMessage) {
                        Bukkit.getScheduler().runTask(plugin, lobbyServerMessage::acceptServer);
                    }
                }

            } catch (Exception ex) {
                return;
            }
        }).start();
    }
}
