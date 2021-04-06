package com.p8.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : WX.Y
 * date : 2020/11/11 11:50
 * description : 地主信息
 * "id": "fc195016b49c407b9c76f1a8049ee142",
 * "merchantId": null, //商家id
 * "agencyId": "1",  //大主id
 * "loginName": "18577563321", //登录名
 * "realName": "小刘", //真实姓名
 * "phone": "18577563321", //手机号
 * "loginTime": null, //最近登录时间
 * "createTime": 1604992923000, //创建时间
 */
public class Landlord implements Parcelable {

    private String id;
    private String merchantId;
    private String agencyId;
    private String loginName;
    private String realName;
    private String facadeImg;
    private String phone;
    private long loginTime;
    private long createTime;
    private String identifyPros;
    private String identifyCons;
    private String identify;
    private String email;
    /**
     * item编辑是否打开
     */
    public boolean isOpen;

    public Landlord(String realName) {
        this.realName = realName;
    }

    public Landlord() {

    }

    protected Landlord(Parcel in) {
        id = in.readString();
        merchantId = in.readString();
        agencyId = in.readString();
        loginName = in.readString();
        realName = in.readString();
        facadeImg = in.readString();
        phone = in.readString();
        loginTime = in.readLong();
        createTime = in.readLong();
        identifyPros = in.readString();
        identifyCons = in.readString();
        identify = in.readString();
        email = in.readString();
    }

    public static final Creator<Landlord> CREATOR = new Creator<Landlord>() {
        @Override
        public Landlord createFromParcel(Parcel in) {
            return new Landlord(in);
        }

        @Override
        public Landlord[] newArray(int size) {
            return new Landlord[size];
        }
    };

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

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
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

    public String getFacadeImg() {
        return facadeImg;
    }

    public void setFacadeImg(String facadeImg) {
        this.facadeImg = facadeImg;
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

    public String getIdentifyPros() {
        return identifyPros;
    }

    public void setIdentifyPros(String identifyPros) {
        this.identifyPros = identifyPros;
    }

    public String getIdentifyCons() {
        return identifyCons;
    }

    public void setIdentifyCons(String identifyCons) {
        this.identifyCons = identifyCons;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(merchantId);
        dest.writeString(agencyId);
        dest.writeString(loginName);
        dest.writeString(realName);
        dest.writeString(facadeImg);
        dest.writeString(phone);
        dest.writeLong(loginTime);
        dest.writeLong(createTime);
        dest.writeString(identifyPros);
        dest.writeString(identifyCons);
        dest.writeString(identify);
        dest.writeString(email);
    }

    @Override
    public String toString() {
        return "Landlord{" +
                "id='" + id + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", realName='" + realName + '\'' +
                ", facadeImg='" + facadeImg + '\'' +
                ", phone='" + phone + '\'' +
                ", loginTime=" + loginTime +
                ", createTime=" + createTime +
                ", identifyPros='" + identifyPros + '\'' +
                ", identifyCons='" + identifyCons + '\'' +
                ", identify='" + identify + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

