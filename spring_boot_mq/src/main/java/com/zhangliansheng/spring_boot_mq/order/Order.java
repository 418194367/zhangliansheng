package com.zhangliansheng.spring_boot_mq.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <pre>项目名称：zhangliansheng
 * 类名称：Order
 * 类描述：
 * 创建人：zhangliansheng
 * 创建时间：2019/12/4 15:42
 */
@Component
@Getter
@Setter
@ToString
public class Order implements Serializable {
    private String id;
    private String name;
    private String masId;
}
