package ru.laimcraft.proxy.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.laimcraft.proxy.Messages;
import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.api.message.Message;
import ru.laimcraft.proxy.api.message.server.LobbyServerMessage;

import java.io.*;
import java.net.Socket;

public class LaimCraftServer {

    public Plugin plugin;
    public Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;

    public LaimCraftServer(Plugin plugin) {
        connect();
    }

    private void connect() {
        new Thread(() -> {
            while (true) {
                try {
                    socket = new Socket("127.0.0.1", 25560);

                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectInputStream = new ObjectInputStream(socket.getInputStream());

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Object obj = objectInputStream.readObject();
                            if (obj instanceof LobbyServerMessage lobbyServerMessage) {
                                Bukkit.getScheduler().runTask(plugin, lobbyServerMessage::acceptServer);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            break; // Ошибка соединения — выходим из цикла
                        }
                    }
                } catch (IOException e) {
                    Bukkit.getLogger().warning("Ошибка подключения: " + e.getMessage());
                } finally {
                    closeConnection();
                }

                // Переподключение через 5 секунд
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    return; // Останавливаем поток, если сервер выключается
                }
            }
        }).start();
    }

    private void closeConnection() {
        try {
            if (objectOutputStream != null) objectOutputStream.close();
            if (objectInputStream != null) objectInputStream.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }

    public boolean sendMessage(Message message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            return true;
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Error sendMessage " + e.getMessage());
            return false;
        }
    }
}
