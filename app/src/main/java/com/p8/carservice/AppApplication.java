package com.p8.carservice;

import com.p8.common.App;
import com.p8.common.config.ModuleLifecycleConfig;

/**
 * @author : WX.Y
 * date : 2021/3/15 16:41
 * description :
 */
public class AppApplication extends App {

    @Override
    public void onCreate()
    {
        super.onCreate();
        setDebug(BuildConfig.DEBUG);
        // 初始化需要初始化的组件
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
    }
}

