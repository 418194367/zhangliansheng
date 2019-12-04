package com.zhangliansheng.spring_boot_mq.consumer.topic;

import com.rabbitmq.client.Channel;
import com.zhangliansheng.spring_boot_mq.order.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：Receive
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/4 15:40
 */
@Component
public class Receive {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "orderkey-queue",durable = "true"),
            exchange = @Exchange(value = "orderkey-exchange",durable = "true",type = "topic"),
            key = "orderkey.key.#"))
    @RabbitHandler
    public void receive(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws IOException {
        System.err.println(order);
        long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(tag,false);
    }
}
