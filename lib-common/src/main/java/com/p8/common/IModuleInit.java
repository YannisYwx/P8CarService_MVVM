package com.p8.common;

/**
 * @author : WX.Y
 * date : 2021/3/12 17:56
 * description : 动态配置组件Application,有需要初始化的组件实现该接口,统一在宿主app 的Application进行初始化
 */
public interface IModuleInit {
    /**
     * 需要优先初始化的
     */
    boolean onInitAhead(App application);

    /**
     * 可以后初始化的
     */
    boolean onInitLow(App application);

}

