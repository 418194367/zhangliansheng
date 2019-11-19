package com.example.thread_pool_executor.thread;

import com.example.thread_pool_executor.ThreadPoolExecutorApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 文档接口自动生成工具类
 *
 * @author Males
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ThreadPoolExecutorApplication.class})
@AutoConfigureMockMvc
@Slf4j
public class TestThreadPoolExecutor {

    @Test
    public void TestThreadPoolExecutor (){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedTransferQueue(),new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1),new ThreadPoolExecutor.CallerRunsPolicy());
        /*threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("丢弃的任务"+r.toString());
                r.run();
                Future<?> submit = threadPoolExecutor2.submit(r);
            }
        });*/
        for (int i = 0;i <= 50;i++){
            threadPoolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName()+"====>");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.err.println("队列中的任务数"+threadPoolExecutor.getQueue().size());
        for (int i = 0; i<= 50;i++){
            Future<?> submit = threadPoolExecutor2.submit(() -> {
                System.out.println(Thread.currentThread().getName());
//                System.out.println(2/0);
            });
            Object o = null;
            try {
                o = submit.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println(o);
        }
        System.out.println("00000");

    }

    @Test
    public void ABA(){
         AtomicInteger atomicInt = new AtomicInteger(100);
         AtomicStampedReference atomicStampedRef = new AtomicStampedReference(atomicInt,0);
        int stamp = atomicStampedRef.getStamp();
        new Thread(){
            public void run(){
                boolean b1 = atomicStampedRef.compareAndSet(atomicInt,111, stamp, stamp+1);
            }
        }.start();
        new Thread(){
            public void run(){
                boolean b2 = atomicStampedRef.compareAndSet(atomicInt,100, stamp, stamp+1);
            }
        }.start();
        new Thread(){
            public void run(){
                boolean b3 = atomicStampedRef.compareAndSet(atomicInt,111, stamp, stamp+1);
            }
        }.start();

    }

    @Test
    public void bigDecimal(){
        BigDecimal bigDecimal = new BigDecimal(124.66);
        BigDecimal bigDecima2 = new BigDecimal(125.66);
        int i = bigDecimal.compareTo(bigDecima2);
        System.out.println(i);
    }
}
