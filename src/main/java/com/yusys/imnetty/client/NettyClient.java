package com.yusys.imnetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class NettyClient {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);
    @Value("${netty.server.host:127.0.0.1}")
    private String remoteHost;
    @Value("${netty.server.port:8080}")
    private Integer remotePort;
    private final EventLoopGroup worker = new NioEventLoopGroup(1);
    @Autowired
    private ClientHandlerInitializer clientHandlerInitializer;
    private volatile Channel channel;

    @PostConstruct
    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(worker) //设置一个 EventLoopGroup 对象
                .channel(NioSocketChannel.class) //  指定 Channel 为客户端 NioSocketChannel
                .remoteAddress(remoteHost, remotePort) // 指定客户端连接地址
                .option(ChannelOption.SO_KEEPALIVE, true) // TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .option(ChannelOption.TCP_NODELAY, true) // 允许较小的数据包的发送，降低延迟
                .handler(clientHandlerInitializer);
        // 连接服务端
        bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    LOG.error("[start][Netty Client 连接服务器({}:{}) 失败]", remoteHost, remotePort);
                    reconnect();
                    return;
                }
                channel = future.channel();
                LOG.info("[start][Netty Client 连接服务器({}:{}) 成功]", remoteHost, remotePort);
            }
        });
    }

    public void reconnect() {
    }
    /**
     * 关闭 Netty Client
     */
    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Client
        if (channel != null) {
            channel.close();
        }
        // <3.2> 优雅关闭一个 EventLoopGroup 对象
        worker.shutdownGracefully();
    }
    /**
     * 发送消息
     *
     * @param invocation 消息体
     */
    /*public void send(Invocation invocation) {
        if (channel == null) {
            LOG.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            LOG.error("[send][连接({})未激活]", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }*/
}
