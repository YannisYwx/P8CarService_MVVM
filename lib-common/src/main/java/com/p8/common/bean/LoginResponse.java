package com.p8.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author : WX.Y
 * date : 2020/9/9 15:53
 * description :
 */
public class LoginResponse {
    @SerializedName("appUser")
    private UserInfo userInfo;
    private String token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        if (userInfo != null) {
            userInfo.setToken(token);
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userInfo=" + userInfo +
                ", token='" + token + '\'' +
                '}';
    }
}

