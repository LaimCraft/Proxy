package ru.laimcraft.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        String[] msg = String.valueOf(object).split(" ");
        switch (msg[0]) {
            case "auth":
                Proxy.getInstance().AuthPlayers.add(msg[1]);
                ctx.close();
                break;
            default:
                ctx.close();
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
