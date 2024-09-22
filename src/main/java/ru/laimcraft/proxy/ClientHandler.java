package ru.laimcraft.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    Proxy proxy = Proxy.getInstance();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        ctx.close();
    }
}
