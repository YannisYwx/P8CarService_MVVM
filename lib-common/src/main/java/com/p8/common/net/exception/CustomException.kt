package com.p8.common.net.exception

import java.lang.Exception

/**
 *  @author : WX.Y
 *  date : 2021/3/23 14:05
 *  description :
 */
data class CustomException(var errorCode: Int, override var message: String? = null) : Exception(message)