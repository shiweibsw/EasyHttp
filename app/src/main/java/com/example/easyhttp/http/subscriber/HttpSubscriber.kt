package com.example.easyhttp.http.subscriber

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.example.easyhttp.http.callback.OnResultCallBack
import com.example.easyhttp.http.exception.ApiException
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Created by shiwei on 2018/1/02.
 */
class HttpSubscriber<T>(listener: OnResultCallBack<T>, lifecycle: Lifecycle) : Observer<T>, LifecycleObserver {

    var mOnResultListener: OnResultCallBack<T>? = null

    lateinit var mDisposable: Disposable

    init {
        mOnResultListener = listener
        lifecycle.addObserver(this)
    }

    override fun onNext(t: T) {
        mOnResultListener?.onSuccess(t)
        mDisposable.dispose()
    }

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
    }

    override fun onError(e: Throwable) {
        if (e is CompositeException) {
            e.exceptions.forEach {
                if (it is SocketTimeoutException) {
                    mOnResultListener?.onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (it is ConnectException) {
                    mOnResultListener?.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (it is UnknownHostException) {
                    mOnResultListener?.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (it is MalformedJsonException) {
                    mOnResultListener?.onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
                }
            }
        } else if (e is SocketTimeoutException) {
            mOnResultListener?.onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
        } else if (e is ConnectException) {
            mOnResultListener?.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
        } else if (e is UnknownHostException) {
            mOnResultListener?.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
        } else {
            var msg = e.message
            var code: Int = 0
            if (msg!!.contains("#")) {
                code = msg.split("#")[0].toInt()
                mOnResultListener?.onError(code, msg.split("#")[1]);
            } else {
                code = ApiException.Code_Default
                mOnResultListener?.onError(code, msg)
            }
        }
    }

    override fun onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unSubscribe() {
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }
}