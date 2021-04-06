package com.p8.common.bean;

import androidx.annotation.DrawableRes;

/**
 * @author : WX.Y
 * date : 2021/2/24 14:23
 * description : 财务管理
 */
public class Financing {
    @DrawableRes
    public int iconRes;

    public String title;


    public Financing(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }
}

