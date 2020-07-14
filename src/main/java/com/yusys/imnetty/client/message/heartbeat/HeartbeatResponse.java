package com.yusys.imnetty.client.message.heartbeat;

import com.yusys.imnetty.common.dispatcher.Message;

public class HeartbeatResponse implements Message {
    /**
     * 类型 - 心跳响应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }
}
