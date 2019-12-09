package com.zhangliansheng.spring_boot_mq;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：Simple
 * 类描述：单例类
 * 创建人：zhangliansheng
 * 创建时间：2019/12/6 9:00
 */
public class Simple {
    public static Simple simple = null;
    private Simple(){
        simple = null;
    }

    public static Simple getInstance(){
        System.out.println(simple == simple);
        if (simple ==null){
            synchronized (Simple.class){
                if (simple ==null){
                    simple = new Simple();
                    System.out.println("00000000000000");
                }
                System.out.println("1111111111111111111");
            }
            System.out.println("2222222222222222");
        }
//        simple = null;
        return simple;
    }
}
