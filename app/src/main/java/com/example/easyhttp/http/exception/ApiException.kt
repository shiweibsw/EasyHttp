package com.example.easyhttp.http.exception

import java.lang.RuntimeException

/**
 * Created by shiwei on 2018/1/12.
 */
class ApiException(resultCode: Int, msg: String) : RuntimeException(getApiExceptionMessage(resultCode, msg)) {
    companion object {
        val Code_TimeOut = 1000
        val Code_UnConnected = 1001
        val Code_MalformedJson = 1020
        val Code_Default = 1003
        val CONNECT_EXCEPTION = "网络出现了些许小问题,请检查网络连接"
        val SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试"
        val MALFORMED_JSON_EXCEPTION = "数据解析错误"
    }

    init {
        var result = getApiExceptionMessage(resultCode, msg)
    }
}

fun getApiExceptionMessage(resultCode: Int, msg: String): String {
    val message: String
    when (resultCode) {
        else -> message = resultCode.toString() + "#" + msg
    }
    return message
}
