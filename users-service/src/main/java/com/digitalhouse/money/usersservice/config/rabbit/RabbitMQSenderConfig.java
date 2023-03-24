package com.digitalhouse.money.usersservice.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQSenderConfig {

    //@Value("${queue.mail-service.name}")
    private String mailServiceQueue = "mail-service";

    @Bean
    public Queue mailQueue() {
        return new Queue(this.mailServiceQueue,false);
    }
}
