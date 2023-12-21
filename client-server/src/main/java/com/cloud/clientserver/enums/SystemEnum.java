package com.cloud.clientserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/16:05
 * @Description:
 */
public class SystemEnum {
    @AllArgsConstructor
    @Getter
    public enum SexType implements IEnum {
        MAN(1, "男人"),
        WOMEN(2, "女人");

        private Integer code;

        private String desc;

        public static SexType getEnumByCode(Integer code) {
            for (SexType value : values()) {
                if (Objects.equals(value.getCode(),code)) {
                    return value;
                }
            }
            return null;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum ShopType implements IEnum {
        JD(3, "京东"),
        PDD(5, "拼多多");

        private Integer code;

        private String desc;

        public static ShopType getEnumByCode(Integer code) {
            for (ShopType value : values()) {
                if (Objects.equals(value.getCode(),code)) {
                    return value;
                }
            }
            return null;
        }
    }
}
