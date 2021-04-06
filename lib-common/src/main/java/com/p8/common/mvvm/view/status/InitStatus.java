package com.p8.common.mvvm.view.status;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.p8.common.R;

/**
 * @author : WX.Y
 * date : 2021/3/11 17:41
 * description :
 */
public class InitStatus extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.common_layout_init;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        super.onViewCreate(context, view);

    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        //不响应reload事件
        return true;
    }
}
