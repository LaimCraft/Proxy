package ru.laimcraft.proxy.rpc;

import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import ru.laimcraft.proxy.Proxy;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        try {
            String msg = byteBuf.toString(io.netty.util.CharsetUtil.UTF_8);
            Proxy.logger.warn("sendMessage " + msg);
            String[] args = msg.split(" ");
            switch (args[0].toLowerCase()) {
                case "binding":
                    String key = args[1];
                    if(!NettyServer.key.equals(key)) return;
                    RPC.ServerName serverName = RPC.ServerName.valueOf(args[2].toUpperCase());
                    RPC.addServer(serverName, new RPC.Server(ctx));
                    RPC.getServer(serverName).sendMessage("success");
                    Proxy.logger.info(String.format("Server %s connected", serverName.name().toLowerCase()));
                    return;
                case "login":
                    Proxy.server.getScheduler().buildTask(Proxy.instance, () -> {
                        Proxy.server.getPlayer(args[1]).ifPresent(player -> {
                            Proxy.authPlayers.put(player.getUsername(), player);
                        });
                    }).schedule();
                    return;
                case "transfer":
                    Proxy.server.getScheduler().buildTask(Proxy.instance, () -> {
                        Proxy.server.getPlayer(args[1]).ifPresent((player) -> {
                            Proxy.server.getServer(args[2]).ifPresent((server) -> {
                                ConnectionRequestBuilder connectionRequestBuilder = player.createConnectionRequest(server);
                                CompletableFuture<ConnectionRequestBuilder.Result> result = connectionRequestBuilder.connect();

                                result.thenAccept(connectResult -> {
                                    if(connectResult.getStatus() == ConnectionRequestBuilder.Status.CONNECTION_CANCELLED) {
                                        player.sendMessage(connectResult.getReasonComponent().get());
                                    }
                                });
                            });
                        });
                    }).schedule();
                    return;
                default:
                    return;
            }
        } catch (Exception ex) {
            Proxy.logger.info(ex.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Proxy.logger.warn(String.format("session to %s removed", RPC.removeServer(ctx)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {return;} catch (Exception e) {
            Proxy.logger.info(e.getMessage());
        }
    }
}
