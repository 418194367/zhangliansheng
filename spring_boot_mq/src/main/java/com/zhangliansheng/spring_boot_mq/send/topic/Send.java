package com.zhangliansheng.spring_boot_mq.send.topic;

import com.zhangliansheng.spring_boot_mq.order.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：Send
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/4 14:49
 */
@Component
public class Send {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void Send(){
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("111111111");
        Order order = new Order();
        order.setId("111111");
        order.setName("555555");
        order.setMasId("888888");
        rabbitTemplate.convertAndSend("orderkey-exchange","orderkey.key.001",order,correlationData);
    }


}
