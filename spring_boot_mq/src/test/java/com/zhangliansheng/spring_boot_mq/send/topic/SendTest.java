package com.zhangliansheng.spring_boot_mq.send.topic;

import com.zhangliansheng.spring_boot_mq.MqApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MqApplication.class})
public class SendTest {

    @Autowired
    Send send;
    @Autowired
    ConfirmSend confirmSend;

    @Test
    public void  send() {
        send.Send();
    }

    @Test
    public void  confirmSend() {
        confirmSend.sendConfirm();
    }

}
