package com.yusys.imnetty.client;

import com.yusys.imnetty.client.handler.ClientHandler;
import com.yusys.imnetty.common.codec.InvocationDecoder;
import com.yusys.imnetty.common.codec.InvocationEncoder;
import com.yusys.imnetty.common.dispatcher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientHandlerInitializer extends ChannelInitializer<Channel> {
    private static final Integer READ_TIME_OUT_SECONDS = 60;
    @Autowired
    private ClientHandler clientHandler;
    @Autowired
    private MessageDispatcher messageDispatcher;
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline
                // 空闲检测
                .addLast(new IdleStateHandler(READ_TIME_OUT_SECONDS, 0, 0))
                .addLast(new ReadTimeoutHandler(3 * READ_TIME_OUT_SECONDS))
                // 解码器
                .addLast(new InvocationDecoder())
                // 编码器
                .addLast(new InvocationEncoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 客户端处理器
                .addLast(clientHandler);

    }
}
