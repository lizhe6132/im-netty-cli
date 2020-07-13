package com.yusys.imnetty.client.handler;

import com.yusys.imnetty.client.NettyClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ClientHandler.class);
    @Autowired
    private NettyClient nettyClient;
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接不活跃时重连
        nettyClient.reconnect();
        //继续触发事件
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 空闲时向服务端发送心跳
        if (event instanceof IdleStateEvent) {
            LOG.info("[userEventTriggered][发起一次心跳]");
            
        } else {
            super.userEventTriggered(ctx, event);
        }

    }
}
