package com.p8.common.bean;


/**
 * @author : WX.Y
 * date : 2020/11/13 9:39
 * description : 代理商 大主
 *             "id": "1",   //id
 *             "loginName": "18599632256", //登录名
 *             "name": "王总", //姓名
 *             "facadeImg": null, //头像地址
 *             "address": "广东省深圳市南山区",  //地址
 *             "phone": "18599632256", //手机号
 *             "idCard": "360521199412202253", //身份证号
 *             "idCardImg": null, //身份证正面
 *             "idCardBackImg": null, //身份证反面
 *             "descr": "123", //描述
 *             "email": "22@qq.com", //邮箱
 *             "loginTime": 1604992400000, //最近登录时间
 *             "createTime": 1604971389000, //创建时间
 */
public class Agency {
    private String id;
    private String loginName;
    private String name;
    private String facadeImg;
    private String address;
    private String phone;
    private String idCard;
    private String idCardImg;
    private String idCardBackImg;
    private String descr;
    private String email;
    private long loginTime;
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacadeImg() {
        return facadeImg;
    }

    public void setFacadeImg(String facadeImg) {
        this.facadeImg = facadeImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardImg() {
        return idCardImg;
    }

    public void setIdCardImg(String idCardImg) {
        this.idCardImg = idCardImg;
    }

    public String getIdCardBackImg() {
        return idCardBackImg;
    }

    public void setIdCardBackImg(String idCardBackImg) {
        this.idCardBackImg = idCardBackImg;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "Agency{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", facadeImg='" + facadeImg + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", idCardImg='" + idCardImg + '\'' +
                ", idCardBackImg='" + idCardBackImg + '\'' +
                ", descr='" + descr + '\'' +
                ", email='" + email + '\'' +
                ", loginTime=" + loginTime +
                ", createTime=" + createTime +
                '}';
    }
}
