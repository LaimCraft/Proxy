package ru.laimcraft.proxy.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.AclEntryType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RPC {

    public static final HashMap<ServerName, Server> servers = new HashMap<>();
    private static final Object modifyServersHashMap = new Object();

    public static void addServer(ServerName serverName, Server server) {
        synchronized (modifyServersHashMap) {
            servers.put(serverName, server);
        }
    }

    public static String removeServer(ChannelHandlerContext ctx) {
        synchronized (modifyServersHashMap) {
            Iterator<Map.Entry<ServerName, Server>> iterator = servers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ServerName, Server> entry = iterator.next();
                if (entry.getValue().ctx == ctx) {
                    iterator.remove(); // Безопасное удаление
                    return entry.getKey().name().toLowerCase();
                }
            }
        }
        return "[error]";
    }

    public static Server getServer(ServerName serverName) {
        return servers.get(serverName);
    }

    public static class Server {

        public ChannelHandlerContext ctx;

        public Server(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void sendMessage(String message) {
            if (ctx == null || ctx.channel() == null) return;

            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

            ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
            byteBuf.writeBytes(bytes);

            ctx.writeAndFlush(byteBuf);
        }

        public void sendMessage(ByteBuf byteBuf) {
            if (ctx == null || ctx.channel() == null) return;

            ctx.writeAndFlush(byteBuf);
        }
    }

    public enum ServerName {
        LOBBY,
        PROXY
    }
}
