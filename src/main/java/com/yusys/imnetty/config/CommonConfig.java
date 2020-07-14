package com.yusys.imnetty.config;

import com.yusys.imnetty.common.dispatcher.MessageDispatcher;
import com.yusys.imnetty.common.dispatcher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }
    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }
}
