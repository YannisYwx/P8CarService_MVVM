package com.p8.common.bean;

/**
 * @author : WX.Y
 * date : 2021/2/24 15:52
 * description : 开票情况
 */
public class Invoice {
    private String date;
    private String serialNumber;
    private String title;
    private String sum;
    private String status;
    private String code;

//    public Invoice() {
//    }

    public Invoice(String date, String serialNumber, String title, String sum, String status, String code) {
        this.date = date;
        this.serialNumber = serialNumber;
        this.title = title;
        this.sum = sum;
        this.status = status;
        this.code = code;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

