package com.example.easyhttp.http.subscriber;

import com.example.easyhttp.http.callback.OnResultCallBack;
import com.example.easyhttp.http.exception.ApiException;
import com.google.gson.stream.MalformedJsonException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.rx_cache2.RxCacheException;


public class HttpSubscriber<T> implements Observer<T> {
    private OnResultCallBack mOnResultListener;
    private Disposable mDisposable;

    public HttpSubscriber(OnResultCallBack listener) {
        this.mOnResultListener = listener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        if (mOnResultListener != null) {
            mOnResultListener.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof CompositeException) {
            CompositeException compositeE=(CompositeException)e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    mOnResultListener.onError(ApiException.Code_TimeOut,ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected,ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected,ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof RxCacheException) {
                    //缓存异常暂时不做处理
                }  else if (throwable instanceof MalformedJsonException) {
                    mOnResultListener.onError(ApiException.Code_MalformedJson,ApiException.MALFORMED_JSON_EXCEPTION);
                }
            }
        }else {
            String msg = e.getMessage();
            int code;
            if (msg.contains("#")) {
                code = Integer.parseInt(msg.split("#")[0]);
                mOnResultListener.onError(code, msg.split("#")[1]);
            } else {
                code = ApiException.Code_Default;
                mOnResultListener.onError(code, msg);
            }
        }
    }

    @Override
    public void onComplete() {

    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
