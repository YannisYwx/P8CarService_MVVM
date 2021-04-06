package com.p8.common.config;

/**
 * @author : WX.Y
 * date : 2021/3/12 17:54
 * description :组件生命周期初始化类的类目管理者,在这里注册需要初始化的组件,通过反射动态调用各个组件的初始化方法
 */
public class ModuleLifecycleReflect {
    /**
     * 基础库
     */
    private static final String BaseInit = "com.p8.common.CommonModuleInit";

    /**
     * main组件库
     */
    private static final String MainInit =
            "com.p8.main.MainModuleInit";

    /**
     * 用户组件初始化
     */
    private static final String UserInit = "com.drz.user.UserModuleInit";

    public static String[] initModuleNames = {BaseInit, MainInit};
}
