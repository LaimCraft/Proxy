package ru.laimcraft.proxy.api;

import ru.laimcraft.proxy.Proxy;
import ru.laimcraft.proxy.api.message.ProxyMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class LaimCraftProxyServer {

    public static HashMap<String, ClientServer> sessions = new HashMap<>();

    public LaimCraftProxyServer() {
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(25560); // Main Server System Port
                while (!server.isClosed()) {
                    ClientServer clientServer = new ClientServer(server.accept());
                    new Thread(clientServer).start();
                }
            } catch (IOException e) {
                return;
            }
        }).start();
    }

    public static class ClientServer implements Runnable {

        public Socket socket;
        public InputStream inputStream;
        public OutputStream outputStream;

        //ByteArrayDataOutput - Запись
        //ByteArrayDataInput - Чтение

        public ClientServer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                while (true)
                    if(objectInputStream.readObject() instanceof final ProxyMessage proxyMessage)
                        Proxy.server.getScheduler().buildTask(Proxy.server, proxyMessage::acceptProxyServer);
                /*while (true) {
                    if(objectInputStream.readObject() instanceof final ProxyMessage proxyMessage) {
                        Proxy.server.getScheduler().buildTask(Proxy.server, proxyMessage::acceptProxyServer);
                    }
                }*/
            } catch (Exception ex) {
                Proxy.logger.info("Exception LaimCraftProxyServer " + ex.getMessage() + " " + ex.toString());
            }
        }
    }
}
