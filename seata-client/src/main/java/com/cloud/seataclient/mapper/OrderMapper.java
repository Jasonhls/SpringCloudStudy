package com.cloud.seataclient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.seataclient.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 何立森
 * @Date: 2023/04/04/15:29
 * @Description:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
