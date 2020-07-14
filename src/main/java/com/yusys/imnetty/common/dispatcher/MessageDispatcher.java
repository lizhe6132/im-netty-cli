package com.yusys.imnetty.common.dispatcher;

import com.alibaba.fastjson.JSON;
import com.yusys.imnetty.common.codec.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 将 Invocation 分发到其对应的 MessageHandler 中，进行业务逻辑的执行
 */
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {
    private static final Logger LOG = LoggerFactory.getLogger(MessageDispatcher.class);
    @Autowired
    private MessageHandlerContainer messageHandlerContainer;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        //获取消息处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        Message message = JSON.parseObject(invocation.getMessage(), messageClass);
        messageHandler.execute(ctx.channel(), message);

    }
}
