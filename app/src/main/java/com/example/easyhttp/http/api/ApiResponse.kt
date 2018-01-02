package com.example.easyhttp.http.api

class ApiResponse<T> {
    private var error_code: Int? = null
    private var reason: String? = null
    private var result: T? = null

    constructor(status: Int?, msg: String?, data: T?) {
        this.error_code = status
        this.reason = msg
        this.result = data
    }

    fun getCode(): Int {
        return error_code!!
    }

    fun setCode(code: Int) {
        this.error_code = code
    }

    fun getMsg(): String {
        return reason!!
    }

    fun setMsg(msg: String) {
        this.reason = msg
    }

    fun getDatas(): T {
        return result!!
    }

    fun setDatas(datas: T) {
        this.result = datas
    }

    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("status=$error_code reason=$reason")
        if (null != result) {
            sb.append(" result:" + result.toString())
        }
        return sb.toString()
    }
}