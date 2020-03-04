package com.zhangliansheng.spring_boot_mq.utils;

/**
 * Created by 张连霞 on 2020/3/4.
 */
public class StringUtil {

    /**
     * 将传入的字符串()内的逗号替换成竖杠方法
     *
     * @param str
     * @return
     */
    public static String replace(String str) {
        StringBuffer sb = new StringBuffer();
        int n = 0;
        while ((n = str.indexOf("(")) != -1) {
            sb.append(str.substring(0, n + 1));
            str = str.substring(n + 1);
            int m = -1;
            n = -1;
            while ((m = str.indexOf("(", m + 1)) < (n = str.indexOf(")", n + 1))) {
                if (-1 == str.indexOf("(")) {
                    break;
                }
            }
            sb.append(str.substring(0, n + 1).replace(",", "|"));
            str = str.substring(n + 1);
        }
        System.out.println(sb.toString());

        return sb.toString();
    }
}
