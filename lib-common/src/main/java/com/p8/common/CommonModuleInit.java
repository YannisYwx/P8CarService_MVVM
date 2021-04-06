package com.p8.common;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.tencent.mmkv.MMKV;
import com.orhanobut.logger.Logger;

/**
 * @author : WX.Y
 * date : 2021/3/15 16:50
 * description :
 */
public class CommonModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(App application) {
        // 初始化日志
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return application.isDebug();
            }
        });
        if (application.isDebug()) {
            ARouter.openLog(); // 开启日志
            ARouter.openDebug(); // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
        }
        ARouter.init(application);
        MMKV.initialize(application);
        Logger.i("基础层初始化完毕 -- onInitAhead");

        return false;
    }

    @Override
    public boolean onInitLow(App application) {
        return false;
    }

}
