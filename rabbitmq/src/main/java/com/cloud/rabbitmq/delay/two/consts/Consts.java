package com.cloud.rabbitmq.delay.two.consts;

/**
 * 常量池
 */
public class Consts {
    private Consts() {
    }

    /**
     * 链路id，发消息时传递
     */
    public static final String TRACE_ID = "traceId";
    /**
     * 消息版本号key，某些场景使用
     */
    public static final String VERSION = "version";
    /**
     * 重试计数key
     */
    public static final String RETRY_COUNT = "retryCount";
    /**
     * 序列化用到的日期格式
     */
    public static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String INFO = "DelayMsg [{}] has been {}";

    public static final String CONFIG_LOG_PRE = "配置================> ";
}
