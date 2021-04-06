package com.p8.common.mvvm.view.status;

import com.kingja.loadsir.callback.Callback;
import com.p8.common.R;

/**
 * @author : WX.Y
 * date : 2021/3/11 16:36
 * description :
 */
public class EmptyStatus extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.common_layout_empty;
    }
}

