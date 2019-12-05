package com.zhangliansheng.spring_boot_mq.send.topic;

import com.zhangliansheng.spring_boot_mq.order.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    /**
     * @Author zhangliansheng
     * @Description 发送路由模式消息
     * @Date 17:35 2019/12/4
     * @Param []
     * @return void
     **/
    public void Send(){
        //死循环发送消息
        while (true) {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(UUID.randomUUID().toString());
            Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setName("ORDER");
            order.setMasId(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("orderkey-exchange", "orderkey.key.001", order, correlationData);
        }
    }


}
