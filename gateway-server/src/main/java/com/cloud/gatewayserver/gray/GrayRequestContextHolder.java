package com.cloud.gatewayserver.gray;

import java.util.Optional;

/**
 * @Description
 * @Author HeLiSen
 * @Date 2022/12/9 9:24
 */
public class GrayRequestContextHolder {
    private static ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

    public static void setGrayTag(Boolean grayTag) {
        threadLocal.set(grayTag);
    }

    public static Optional getGrayTag() {
        return Optional.of(Optional.ofNullable(threadLocal.get()).orElse(false));
    }

    public static void clearGrayTag() {
        threadLocal.remove();
    }
}
