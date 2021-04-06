package com.p8.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : WX.Y
 * date : 2020/11/12 11:19
 * description : 订单信息
 *                 "id": "0be471a4740011eab328506b4b231e4c",
 *                 "openId": "eaf7c88d69964dff814811d8866409c6", //用户id
 *                 "serialNumber": "106516516",  //车位订单流水号
 *                 "payNumber": null,  //支付流水号
 *                 "merchantId": "1",  //商家id
 *                 "authorizerId": null, //授权人id
 *                 "parkingNumber": "0755195191", //车位号
 *                 "startTime": 1562053974000, //入场时间
 *                 "endTime": 1563609177000,  //出场世界
 *                 "duration": null,
 *                 "payMoney": 0.01, //支付金额
 *                 "prepayId": "",
 *                 "state": 1,  //订单状态 1.未支付  2.已支付
 *                 "profit": 1.20, //合作机构分成
 *                 "descr": "1", //描述
 *                 "type": null, //支付类型1.h5-微信  2.h5-支付宝 3.安卓-微信 4.安卓-支付宝 5.ios-微信 6.ios-支付宝
 *                 "createTime": 1588821284000,  //创建时间
 */
public class Order implements Parcelable {
    private String id;
    private String openId;
    private String prepayId;
    private String serialNumber;
    private String payNumber;
    private String merchantId;
    private String authorizerId;
    private String descr;
    private String parkingNumber;
    private long startTime;
    private long endTime;
    private long createTime;
    private int duration;
    private int state;
    private int type;
    private float payMoney;
    private float profit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAuthorizerId() {
        return authorizerId;
    }

    public void setAuthorizerId(String authorizerId) {
        this.authorizerId = authorizerId;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(float payMoney) {
        this.payMoney = payMoney;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    protected Order(Parcel in) {
        id = in.readString();
        openId = in.readString();
        prepayId = in.readString();
        serialNumber = in.readString();
        payNumber = in.readString();
        merchantId = in.readString();
        authorizerId = in.readString();
        descr = in.readString();
        parkingNumber = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        createTime = in.readLong();
        duration = in.readInt();
        state = in.readInt();
        type = in.readInt();
        payMoney = in.readFloat();
        profit = in.readFloat();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(openId);
        dest.writeString(prepayId);
        dest.writeString(serialNumber);
        dest.writeString(payNumber);
        dest.writeString(merchantId);
        dest.writeString(authorizerId);
        dest.writeString(descr);
        dest.writeString(parkingNumber);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(createTime);
        dest.writeInt(duration);
        dest.writeInt(state);
        dest.writeInt(type);
        dest.writeFloat(payMoney);
        dest.writeFloat(profit);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", openId='" + openId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", payNumber='" + payNumber + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", authorizerId='" + authorizerId + '\'' +
                ", descr='" + descr + '\'' +
                ", parkingNumber='" + parkingNumber + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", duration=" + duration +
                ", state=" + state +
                ", type=" + type +
                ", payMoney=" + payMoney +
                ", profit=" + profit +
                '}';
    }
}

