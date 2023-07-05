package com.cloud.rocketmq.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 何立森
 * @Date: 2023/01/28/18:01
 * @Description:
 */
@Data
public class OrderStep {
    /**
     * 订单id
     */
    private String id;
    /**
     * 操作步骤
     */
    private String desc;

    public OrderStep() {
    }

    public OrderStep(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static List<OrderStep> buildOrderSteps(String id) {
        List<OrderStep> orderSteps = new ArrayList<>();
        OrderStep createStep = new OrderStep(id, "create order");
        orderSteps.add(createStep);

        OrderStep payStep = new OrderStep(id, "pay order");
        orderSteps.add(payStep);

        OrderStep callbackStep = new OrderStep(id, "call back order");
        orderSteps.add(callbackStep);

        return orderSteps;
    }
}
