package com.cloud.clientserver.pojo;

import com.cloud.clientserver.enums.SystemEnum;
import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/01/16/16:28
 * @Description:
 */
@Data
public class DeserializerDto {
    private String name;
    private SystemEnum.SexType sexType;
    private SystemEnum.ShopType shopType;

}
