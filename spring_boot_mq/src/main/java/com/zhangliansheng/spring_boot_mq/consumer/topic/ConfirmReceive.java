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
 * 类名称：ConfirmReceive
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/4 17:46
 */
@Component
public class ConfirmReceive {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "orderconfirmkey-queue",durable = "true"),
            exchange = @Exchange(value = "orderconfirmkey-exchange",durable = "true",type = "topic"),
            key = "orderconfirmkey.#"))
    @RabbitHandler
    public void receive(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws IOException {
        System.err.println(order);
        throw new RuntimeException("000");
//        long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
//        channel.basicAck(tag,false);
    }
}
