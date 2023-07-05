package com.cloud.es.utils;

import cn.hutool.core.lang.Snowflake;

/**
 * @Description: ID工具类
 * @Author: YinPeng
 * @Date: 2022/8/30 17:40
 */
public class IdUtils {

  private final static int MOD = 32;
  private final static Snowflake instance = new Snowflake(1,System.currentTimeMillis() % MOD);

  /**
   * 生成字符串类型ID
   * @return
   */
  public static String getNextIdStr() {
    return instance.nextIdStr();
  }

  /**
   * 生成数字型ID
   * @return
   */
  public static Long getNextId() {
    return instance.nextId();
  }

}
