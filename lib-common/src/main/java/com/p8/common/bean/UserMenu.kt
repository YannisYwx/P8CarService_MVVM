package com.p8.common.bean

import com.p8.common.Constants

/**
 *  @author : WX.Y
 *  date : 2021/3/23 17:49
 *  description :
 */
class UserMenu(
    @Constants.UserType var userType: Int,
    var menuLabel: String,
    var iconRes: Int,
    @MenuType var menuTye: Int
) {

    var hasTopLine = false
    var hasBottomLine = false
    constructor(
        @Constants.UserType userType: Int,
        menuLabel: String,
        iconRes: Int,
        @MenuType menuTye: Int,
        hasTopLine: Boolean
    ) : this(userType, menuLabel, iconRes, menuTye) {
        this.hasTopLine = hasTopLine
    }

    override fun toString(): String {
        return "UserMenu{" +
                "userType=" + userType +
                ", menuLabel='" + menuLabel + '\'' +
                ", iconRes='" + iconRes + '\'' +
                ", menuTye=" + menuTye +
                '}'
    }

    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class MenuType {
        companion object {
            /**
             * 用户中心
             */
            var USER_CENTER = 0

            /**
             * 停车监控
             */
            var PARKING_MONITOR = 1

            /**
             * 设备绑定
             */
            var DEVICE_BINDING = 2

            /**
             * 设备调试
             */
            var DEVICE_DEBUG = 3

            /**
             * 工单处理
             */
            var WORK_ORDER_PROCESSING = 4

            /**
             * J架管理
             */
            var J_MANAGE = 5

            /**
             * 配件管理
             */
            var MOUNTINGS_MANAGE = 6

            /**
             * 财务管理
             */
            var FINANCE_MANAGE = 7

            /**
             * 地主管理
             */
            var LAND_MANAGE = 8

            /**
             * J架管理
             */
            var CAR_OWNER_MANAGE = 81

            /**
             * 公告管理
             */
            var NOTICE_MANAGE = 9

            /**
             * 订单管理
             */
            var ORDER_MANAGE = 10

            /**
             * 修改密码
             */
            var MODIFY_PASSWORD = 11

            /**
             * App更新
             */
            var APP_UPDATE = 12

            /**
             * 设置
             */
            var SETTINGS = 13

            /**
             * 消息中心
             */
            var MESSAGE_CENTER = 14

            /**
             * 清理缓存
             */
            var CLEAR_CACHE = 15

            /**
             * 签到签出
             */
            var CLOCK = 16
        }
    }

}