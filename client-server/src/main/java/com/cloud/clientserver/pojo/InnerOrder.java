package com.cloud.clientserver.pojo;

import java.util.HashMap;

public class InnerOrder extends HashMap<String, Object> {
    private Long accountId;
    private String systemCode;
    private String orderCode;
    private String buyerName;
    private String tel;

    public void setAccountId(Long accountId) {
        this.put("accountId", (this.accountId = accountId));
    }

    public void setSystemCode(String systemCode) {
        this.put("systemCode", (this.systemCode = systemCode));
    }

    public void setOrderCode(String orderCode) {
        this.put("orderCode", (this.orderCode = orderCode));
    }

    public void setBuyerName(String buyerName) {
        this.put("buyerName", (this.buyerName = buyerName));
    }

    public void setTel(String tel) {
        this.put("tel", (this.tel = tel));
    }

    public Long getAccountId() {
        return this.get("accountId") == null ? null : (Long) this.get("accountId");
    }

    public String getSystemCode() {
        return this.get("systemCode") == null ? null : (String) this.get("systemCode");
    }

    public String getOrderCode() {
        return this.get("orderCode") == null ? null : (String) this.get("orderCode");
    }


    public String getBuyerName() {
        return this.get("buyerName") == null ? null : (String) this.get("buyerName");
    }

    public String getTel() {
        return this.get("tel") == null ? null : (String) this.get("tel");
    }
}
