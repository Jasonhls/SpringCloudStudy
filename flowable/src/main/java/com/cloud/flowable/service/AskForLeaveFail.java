package com.cloud.flowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @Author: 何立森
 * @Date: 2023/10/25/11:57
 * @Description:
 */
public class AskForLeaveFail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("请假失败");
    }
}
