package com.zhangliansheng.spring_boot_mq.send.topic;

import com.zhangliansheng.spring_boot_mq.MqApplication;
import com.zhangliansheng.spring_boot_mq.Simple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.*;

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

    @Test
    public void  Simple() {

        /**
         * @Author zhangliansheng
         * @Description 测试线程池自己处理的方法
         * @Date 15:50 2019/12/6
         * @Param []
         * @return void
         **/
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                r.run();
            }
        });
        /**
         * @Author zhangliansheng
         * @Description 测试线程池默认策略
         * @Date 15:50 2019/12/6
         * @Param []
         * @return void
         **/
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1));
        /**
         * @Author zhangliansheng
         * @Description 测试线程池不抛出异常的策略
         * @Date 15:50 2019/12/6
         * @Param []
         * @return void
         **/
        ThreadPoolExecutor threadPoolExecutor3 = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());


        /*while (true) {
            threadPoolExecutor.execute(() ->{
                System.out.println("execute===>"+Thread.currentThread().getName());
            });

        }*/
        /*while (true) {
            threadPoolExecutor3.execute(() ->{
                System.out.println("execute===>"+Thread.currentThread().getName());
            });
        }*/

        while (true) {
//            threadPoolExecutor2.shutdown();
            threadPoolExecutor2.execute(() ->{
                System.out.println("execute===>"+Thread.currentThread().getName());
            });

        }

    }

}
