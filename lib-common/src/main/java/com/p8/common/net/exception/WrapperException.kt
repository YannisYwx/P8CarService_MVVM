package com.p8.common.net.exception

/**
 *  @author : WX.Y
 *  date : 2021/3/23 14:42
 *  description :
 */
class WrapperException(var msg: String, @Transient var body: Any? = null) : RuntimeException(msg) {

    init {
        body?.let {
            if (it is Throwable) {
                initCause(it)
            }
        }
        if (msg.isEmpty()) msg = "null"
    }

    /**
     * [okhttp3.Request.tag]
     * 转换body为想要的类型，如果类型不匹配，则返回null
     *
     * @param clazz 类型对象class
     * @param <T>   泛型用于指定类型
    </T> */
    fun <T> castBody(clazz: Class<out T?>) =
        if (clazz.isInstance(body)) {
            clazz.cast(body)
        } else null


    override fun toString() = "WrapperException{ msg = $msg, body = $body}"
}