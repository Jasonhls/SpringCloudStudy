package com.cloud.clientserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 何立森
 * @Date: 2023/12/14/9:48
 * @Description:
 */
@RequestMapping("/labelKeyWord")
@RestController
public class LabelKeyWordController {

    @PostMapping(value = "/labelUseExample")
    public void labelUseExample() {
        retry:
        for (int i = 0; i < 10; i++) {
            System.out.println(" i = " + i);
            for (int j = 10; j < 20; j++) {
                if(j == 15) {
                    break retry; //直接跳到retry，相当于跳出了两层循环，如果只有break，只能跳出一层循环
                }
                System.out.println(" j = " + j);
            }
        }

        retry2:
        for (int i = 0; i < 10; i++) {
            System.out.println(" i = " + i);
            for (int j = 10; j < 20; j++) {
                if(j == 15) {
                    continue retry2;  //直接中断内层循环，从最外层循环继续，如果是continue，是从最内层循环继续
                }
                System.out.println(" j = " + j);
            }
        }

        int m = 20;
        while(m > 1) {
            retry3:{
                if(m % 3 == 1) {
                    System.out.println("哈1");
                    break retry3; //退出到retry3，即退出被大括号括起来的代码部分，继续往后执行
                }
                if(m % 3 == 2) {
                    System.out.println("哈2");
                    break retry3;
                }
                if(m % 3 == 0) {
                    System.out.println("哈3");
                }
            }
            m--;
        }
    }
}
