package com.yusys.imnetty.client.message.heartbeat;

import com.yusys.imnetty.common.dispatcher.Message;

public class HeartBeatRequest implements Message {
    /**
     * 类型 - 心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }
}
