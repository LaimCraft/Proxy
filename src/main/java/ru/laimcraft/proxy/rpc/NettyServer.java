package ru.laimcraft.proxy.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.laimcraft.proxy.Proxy;

public class NettyServer {

    public static final String key = "Fw129efp2w8pdsj903jkopmgrs";

    ServerBootstrap serverBootstrap;
    EventLoopGroup eventLoopGroup1 = new NioEventLoopGroup();
    EventLoopGroup eventLoopGroup2 = new NioEventLoopGroup();

    public NettyServer() {
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup1, eventLoopGroup2);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelHandler());

            ChannelFuture channelFuture = serverBootstrap.bind("127.0.0.1", 25555).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Proxy.logger.info(e.getMessage());
            return;
        } finally {
            eventLoopGroup1.shutdownGracefully();
            eventLoopGroup2.shutdownGracefully();
        }
    }
}
