package com.example.easyhttp.http.callback

/**
 * Created by shiwei on 2018/1/2.
 */
interface OnResultCallBack<in T> {

    fun onSuccess(t: T)

    fun onError(code: Int, errorMsg: String)

}