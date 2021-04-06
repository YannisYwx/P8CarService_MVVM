package com.p8.common.mvvm.view.status;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.p8.common.R;

/**
 * @author : WX.Y
 * date : 2021/3/11 16:37
 * description :
 */
public class LoadingStatus extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.common_layout_loading;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        //不响应reload事件
        return true;
    }

    @Override
    public boolean getSuccessVisible() {
        //背景可自定义
        return true;
    }

}

