package com.p8.home

import com.blankj.utilcode.util.CacheDoubleUtils
import com.orhanobut.logger.Logger
import com.p8.common.Constants
import com.p8.common.bean.LoginInfo
import com.p8.common.bean.UserMenu
import com.p8.home.bean.HomeMultipleItem
import java.util.*

/**
 *  @author : WX.Y
 *  date : 2021/3/23 18:11
 *  description :
 */
class AppDataUtil {
    companion object {

        @Constants.UserType
        fun getUserType() =
            CacheDoubleUtils.getInstance()
                .getParcelable<LoginInfo>(Constants.Key.LOGIN_INFO, LoginInfo.CREATOR).userType

        /**
         * 根据登入类型获取菜单选项
         *
         * @return 菜单集合
         */
        fun getUserMenus(): List<HomeMultipleItem>? {
            val userType: Int = getUserType()
            val userMenus: MutableList<HomeMultipleItem> =
                ArrayList<HomeMultipleItem>()
            userMenus.add(HomeMultipleItem(HomeMultipleItem.MODULE))
            Logger.e("getUserMenus ====== $userType")
            when (userType) {
                Constants.UserType.LARGE -> {

                    //大主菜单
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "我的",
                                R.mipmap.icon_me,
                                UserMenu.MenuType.USER_CENTER,
                                true
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "用户",
                                R.mipmap.icon_user,
                                UserMenu.MenuType.CAR_OWNER_MANAGE
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "配件",
                                R.mipmap.icon_parts,
                                UserMenu.MenuType.MOUNTINGS_MANAGE,
                                true
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "订单",
                                R.mipmap.icon_order_manage,
                                UserMenu.MenuType.ORDER_MANAGE
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "J 架",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.J_MANAGE
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LARGE,
                                "公告",
                                R.mipmap.icon_notice,
                                UserMenu.MenuType.NOTICE_MANAGE
                            )
                        )
                    )
                }
                Constants.UserType.MEDIUM -> {
                }
                Constants.UserType.SMALL -> {
                }
                Constants.UserType.LAND -> {
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "个人中心",
                                R.mipmap.icon_user,
                                UserMenu.MenuType.USER_CENTER
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "停车监控",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.PARKING_MONITOR
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "设备绑定",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.DEVICE_BINDING
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "设备调试",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.DEVICE_DEBUG
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "工单处理",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.WORK_ORDER_PROCESSING
                            )
                        )
                    )
                    userMenus.add(
                        HomeMultipleItem(
                            HomeMultipleItem.MENU,
                            UserMenu(
                                Constants.UserType.LAND,
                                "签到签出",
                                R.mipmap.icon_j,
                                UserMenu.MenuType.CLOCK
                            )
                        )
                    )
                }
                Constants.UserType.BUILD -> {
                }
                Constants.UserType.ONESELF -> {
                }
                Constants.UserType.OTHER -> {
                }
                Constants.UserType.PLACE -> {
                }
                Constants.UserType.PLATFORM -> {
                }
                else -> {
                }
            }
            return userMenus
        }

        var mLoginInfo: LoginInfo? = null

        fun getLoginInfo(): LoginInfo? {
            if (mLoginInfo == null) {
                mLoginInfo = CacheDoubleUtils.getInstance()
                    .getParcelable(Constants.Key.LOGIN_INFO, LoginInfo)
            }
            return mLoginInfo
        }
    }
}