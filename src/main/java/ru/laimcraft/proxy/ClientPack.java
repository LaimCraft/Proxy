package ru.laimcraft.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;

public class ClientPack {
    public HashMap<String, String> request = new HashMap<>();
    private Bootstrap bootstrap;
    NioEventLoopGroup group;
    public ClientPack() throws InterruptedException {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());

                        channel.pipeline().addLast(new ClientHandler());
                    }
                });
        Channel channel = bootstrap.connect("localhost", 59999).sync().channel();
        String s = "";
        channel.writeAndFlush(s);
        channel.closeFuture().sync();
    }

    public void close(String login) throws InterruptedException {
        Channel channel = bootstrap.connect("localhost", 59999).sync().channel();
        channel.writeAndFlush("close " + login);
        channel.closeFuture().sync();
    }
}
