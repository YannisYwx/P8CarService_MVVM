package com.p8.common.config;

import androidx.annotation.Nullable;

import com.p8.common.App;
import com.p8.common.IModuleInit;

/**
 * @author : WX.Y
 * date : 2021/3/12 17:55
 * description : 作为组件生命周期初始化的配置类,通过反射机制,动态调用每个组件初始化逻辑
 */
public class ModuleLifecycleConfig {

    private ModuleLifecycleConfig() {

    }

    private static class SingleHolder {
        private static ModuleLifecycleConfig instance =
                new ModuleLifecycleConfig();
    }

    public static ModuleLifecycleConfig getInstance() {
        return SingleHolder.instance;
    }

    /**
     * 优先初始化
     */
    public void initModuleAhead(@Nullable App application) {
        for (String moduleName : ModuleLifecycleReflect.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleName);
                IModuleInit init = (IModuleInit) clazz.newInstance();
                init.onInitAhead(application);
            } catch (ClassNotFoundException | InstantiationException
                    | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 后初始化
     */
    public void initModuleLow(@Nullable App application) {
        for (String moduleName : ModuleLifecycleReflect.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleName);
                IModuleInit init = (IModuleInit) clazz.newInstance();
                // 调用初始化方法
                init.onInitLow(application);
            } catch (ClassNotFoundException | InstantiationException
                    | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
