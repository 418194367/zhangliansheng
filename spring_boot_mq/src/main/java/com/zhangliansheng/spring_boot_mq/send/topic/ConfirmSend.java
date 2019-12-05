package com.zhangliansheng.spring_boot_mq.send.topic;

import com.zhangliansheng.spring_boot_mq.order.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：ConfirmSend
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/4 17:34
 */
@Component
public class ConfirmSend {
    @Autowired
    RabbitTemplate rabbitTemplate;

    RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){

        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            String id = correlationData.getId();
            if (b){
                System.err.println("----------->>>>"+id);
            }
        }
    };

    /**
     * @Author zhangliansheng
     * @Description 发送确认形式的消息
     * @Date 17:36 2019/12/4
     * @Param []
     * @return void
     **/
    public void sendConfirm(){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setName("ORDER");
        order.setMasId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("orderconfirmkey-exchange","orderconfirmkey.key.confirm", order,correlationData);
        System.out.println("======>>>"+correlationData.getId());
    }
}
