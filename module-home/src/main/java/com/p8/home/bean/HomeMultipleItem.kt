package com.p8.home.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.p8.common.bean.UserMenu

/**
 *  @author : WX.Y
 *  date : 2021/3/23 17:47
 *  description :
 */
class HomeMultipleItem(private var itemType: Int, vararg menus: UserMenu?) : MultiItemEntity {

    companion object {
        /**
         * 物流 财务 地主 工单 模块
         */
        const val MODULE = 0

        /**
         * 菜单
         */
        const val MENU = 1
    }


    var userMenu: UserMenu? = null

    init {
        if (menus.isNotEmpty()) {
            userMenu = menus[0]
        }
    }

    override fun getItemType() = itemType

}