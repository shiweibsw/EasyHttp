package com.example.easyhttp.http.callback;

/**
 * Created by shiwei on 16/3/10.
 */
public interface OnResultCallBack<T> {

    void onSuccess(T t);

    void onError(int code, String errorMsg);
}
