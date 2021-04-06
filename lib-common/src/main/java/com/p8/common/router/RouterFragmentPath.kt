package com.p8.common.router

/**
 *  @author : WX.Y
 *  date : 2021/3/22 18:57
 *  description :
 */
class RouterFragmentPath {

    /**
     * 入口组件
     */
    object Main {
        private const val MAIN = "/main"

        /**
         * 九宫格入口
         */
        const val PAGER_ENTRY = "$MAIN/ENTRY"

        /**
         * 登录页
         */
        const val PAGER_LOGIN = "$MAIN/Login"
    }

    /** 首页组件  */
    object Home {
        private const val HOME = "/home"

        /** 首页  */
        const val PAGER_HOME = "$HOME/Home"
    }

    /** 社区组件  */
    object Community {
        private const val COMMUNITY = "/community"

        /** 社区页  */
        const val PAGER_COMMUNITY = "$COMMUNITY/Community"
    }

}