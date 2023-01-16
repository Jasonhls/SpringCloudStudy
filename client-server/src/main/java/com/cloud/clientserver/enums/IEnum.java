package com.cloud.clientserver.enums;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/16:05
 * @Description:
 */
public interface IEnum {

    static <E extends IEnum> E codeOf(Class<E> enumClass, Integer code) {
        E[] enumConstants = enumClass.getEnumConstants();
        E[] var3 = enumConstants;
        int var4 = enumConstants.length;
        for(int var5 = 0; var5 < var4; ++var5) {
            E e = (E) var3[var5];
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    Integer getCode();

    String getDesc();
}
