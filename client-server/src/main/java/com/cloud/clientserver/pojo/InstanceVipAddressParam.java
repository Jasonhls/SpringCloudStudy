package com.cloud.clientserver.pojo;

import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/07/12/16:29
 * @Description:
 */
@Data
public class InstanceVipAddressParam {
    private String vipAddress;
    private Boolean secure;
    private String region;
}
