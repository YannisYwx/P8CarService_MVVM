package com.p8.common.event

/**
 *  @author : WX.Y
 *  date : 2021/3/22 13:03
 *  description :
 */
class EventCode {
    interface App {
        companion object {
            /**
             * token失效
             */
            const val TOKEN_INVALID = 1_000

            /**
             * 重置密码
             */
            const val RESET_PASSWORD = 1_001
        }
    }

    interface Main {
        companion object {
            const val LOGIN_SUCCESS = 2_000
            const val LOGIN_FAIL = 2_001
        }
    }

    interface User {
        companion object {

        }
    }
}