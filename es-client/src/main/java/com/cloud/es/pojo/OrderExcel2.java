package com.cloud.es.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客户excel表信息
 * @author Irving
 * @date 2021/5/14 16:12
 */
@Data
public class OrderExcel2 {

    @ExcelProperty(value = "订单编号",index = 1)
    private String orderCode;

    @ExcelProperty(value = "商品名称",index = 2)
    private String productName;

    @ExcelProperty(value = "商品数量",index = 32)
    private String productNum;

    @ExcelProperty(value = "商品编码",index = 4)
    private String productCode;

    @ExcelProperty(value = "商品金额",index = 5)
    private String price;

    @ExcelProperty(value = "商品优惠",index = 6)
    private String discountMoney;

    @ExcelProperty(value = "运费",index = 7)
    private String transExpenses;

@ExcelProperty(value = "订单实付金额",index = 8)
    private String orderPayMoney;

@ExcelProperty(value = "下单时间",index = 9)
    private String submitTime;

@ExcelProperty(value = "支付方式",index = 10)
    private String payType;

@ExcelProperty(value = "购买人姓名",index = 11)
    private String customerName;

@ExcelProperty(value = "购买人手机号",index = 12)
    private String customerPhone;

@ExcelProperty(value = "店铺名称",index = 13)
    private String shopName;

@ExcelProperty(value = "收件人姓名",index = 14)
    private String receiverName;

@ExcelProperty(value = "收件人手机号",index = 15)
    private String receiverPhone;

@ExcelProperty(value = "收件人地址",index = 16)
    private String receiverAddress;

    @ExcelProperty(value = "归属人账号",index = 17)
    private String loginName;

}
