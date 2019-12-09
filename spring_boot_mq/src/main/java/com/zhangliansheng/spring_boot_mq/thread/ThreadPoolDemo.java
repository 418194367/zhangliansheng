package com.zhangliansheng.spring_boot_mq.thread;

import java.util.concurrent.*;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：ThreadPoolDemo
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/9 14:47
 */
public class ThreadPoolDemo {

    /**
     * @Author zhangliansheng
     * @Description 测试线程池自己处理的方法
     * @Date 15:50 2019/12/6
     * @Param []
     * @return void
     **/
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1), new RejectedExecutionHandler() {
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
    public static ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1));
    /**
     * @Author zhangliansheng
     * @Description 测试线程池不抛出异常的策略
     * @Date 15:50 2019/12/6
     * @Param []
     * @return void
     **/
    public static ThreadPoolExecutor threadPoolExecutor3 = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args) {
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
