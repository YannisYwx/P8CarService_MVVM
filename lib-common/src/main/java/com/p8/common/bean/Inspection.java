package com.p8.common.bean;

/**
 * @author : WX.Y
 * date : 2020/12/21 11:14
 * description : 地主信息
 *             "id": "2",    //地主id
 *             "merchantId": "1",  //商家id
 *             "loginName": "wzh", //登录名
 *             "realName": "wzh",  //真实姓名
 *             "phone": "18565616772", //手机号
 *             "loginTime": 1603272804000, //登陆时间
 *             "createTime": 1561428847000, //创建时间
 */
public class Inspection {
    private String id;
    private String merchantId;
    private String loginName;
    private String realName;
    private String phone;
    private long loginTime;
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "id='" + id + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", loginTime=" + loginTime +
                ", createTime=" + createTime +
                '}';
    }
}

