package com.p8.common.router

/**
 *  @author : WX.Y
 *  date : 2021/3/22 19:03
 *  description :
 */
class RouterActivityPath {

    /**
     * 入口
     */
    object Main {
        private const val MAIN = "/main"

        const val PAGER_ENTRY = "${MAIN}/Entry"
    }

    /**
     * 首页
     */
    object Home {
        private const val HOME = "/home"

        const val PAGER_HOME = "$HOME/Home"
    }
}