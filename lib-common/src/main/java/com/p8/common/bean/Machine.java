package com.p8.common.bean;

/**
 * @author : WX.Y
 * date : 2020/9/25 14:28
 * description : 泊位信息
 */
public class Machine {
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int pageSize;
    /**
     * 车位号
     */
    private String parkingNumber = "";
    /**
     * 入场时间
     */
    private long entryTime = 0;
    /**
     * 泊位状态(0 无车/1 有车/2 等待激活/3 初始化中/4 异常)
     */
    private int parkingStatus;
    /**
     * 设备密钥
     */
    private String productKey = "";
    /**
     * 设备名称
     */
    private String deviceName = "";
    /**
     * 设备id
     */
    private String deviceId = "";
    /**
     * 设备地址
     */
    private String address = "";
    /**
     * 泊位id
     */
    private String id = "";
    /**
     * 时间戳
     */
    private String timestamp = "";
    /**
     * 安装人员姓名
     */
    private String inspectName = "";
    /**
     * 安装人员id
     */
    private String inspectId = "";
    /**
     * 安装时间
     */
    private long createTime = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectName() {
        return inspectName;
    }

    public void setInspectName(String inspectName) {
        this.inspectName = inspectName;
    }

    public String getInspectId() {
        return inspectId;
    }

    public void setInspectId(String inspectId) {
        this.inspectId = inspectId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(int parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", parkingNumber='" + parkingNumber + '\'' +
                ", entryTime=" + entryTime +
                ", productKey='" + productKey + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
