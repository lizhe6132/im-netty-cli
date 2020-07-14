package com.yusys.imnetty.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 编码器
 * MessageToByteEncoder 是 Netty 定义的编码 ChannelHandler 抽象类，将泛型 <I> 消息转换成字节数组
 * MessageToByteEncoder 会最终将 ByteBuf out 写到 TCP Socket 中
 */
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {
    private static final Logger LOG = LoggerFactory.getLogger(InvocationEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation invocation, ByteBuf out) throws Exception {
        // 将 Invocation 转换成 byte[] 数组
        byte[] content = JSON.toJSONBytes(invocation);
        //动态长度解决粘包拆包问题,消息分为消息头(指明长度)和消息体两部分
        // 写入 length
        out.writeInt(content.length);
        // 写入内容
        out.writeBytes(content);
        LOG.info("[encode][连接({}) 编码了一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
