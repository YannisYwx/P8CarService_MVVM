package com.p8.common.bean;

/**
 * @author : WX.Y
 * date : 2021/2/24 17:07
 * description : 结算：分为施主 中主  小主 地主
 */
public class SettleAccounts {
    private String name;
    private String totalCost;//总费用
    private int introduce;//介绍数量
    private String proportion;//分成比例
    //已支付
    private String havePaid;
    /**
     * 截止支付日期
     */
    private String expirationDate;
    /**
     * 发起人
     */
    private String initiator;
    /**
     * 负责人
     */
    private String principal;

    private String phone;

    private String date;

    public SettleAccounts() {
    }

    public SettleAccounts(String name, String totalCost, int introduce, String proportion, String havePaid, String expirationDate, String initiator, String principal, String phone, String date) {
        this.name = name;
        this.totalCost = totalCost;
        this.introduce = introduce;
        this.proportion = proportion;
        this.havePaid = havePaid;
        this.expirationDate = expirationDate;
        this.initiator = initiator;
        this.principal = principal;
        this.phone = phone;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public int getIntroduce() {
        return introduce;
    }

    public void setIntroduce(int introduce) {
        this.introduce = introduce;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getHavePaid() {
        return havePaid;
    }

    public void setHavePaid(String havePaid) {
        this.havePaid = havePaid;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

