package com.p8.common.bean;

import java.io.Serializable;

/**
 * @author : WX.Y
 * date : 2020/9/9 15:41
 * description :
 */
public class UserInfo implements Serializable {
    private String id;
    private String phone;
    private String password;
    private String nickName;
    private int sex;
    private int age;
    private String headImg;
    private long loginTime;
    private long createTime;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", icon_password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", headImg='" + headImg + '\'' +
                ", loginTime=" + loginTime +
                ", createTime=" + createTime +
                ", token='" + token + '\'' +
                '}';
    }
}

