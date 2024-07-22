package com.cloud.testclient.extend;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

/**
 * @Author: 何立森
 * @Date: 2024/07/12/14:11
 * @Description: 容器启动之后，会执行CommandLineRunner的run方法
 */
@Slf4j
public class CustomCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String[] parameters = args;
        log.info("CustomCommandLineRunner run方法入参为：{}", JSONUtil.toJsonStr(parameters));
    }

}
