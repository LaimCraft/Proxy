package ru.laimcraft.proxy.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class LaimCraftServer {

    public Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;

    public LaimCraftServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("0.0.0.0", 25560);
            } catch (IOException e) {
                return;
            }
        }).start();
    }
}
