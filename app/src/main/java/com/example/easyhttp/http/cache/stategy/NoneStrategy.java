package com.example.easyhttp.http.cache.stategy;


import com.example.easyhttp.http.cache.RxCache;
import com.example.easyhttp.http.cache.data.CacheResult;
import com.example.easyhttp.http.cache.data.ResultFrom;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by Chu on 2017/6/24.
 * 仅加载网络，不缓存
 */

public final class NoneStrategy implements IStrategy  {

    private NoneStrategy() {
    }

    static final NoneStrategy INSTANCE = new NoneStrategy();

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, final String key, Observable<T> source, Type type) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                return new CacheResult<>(ResultFrom.Remote, key, t);
            }
        });
    }

    @Override
    public <T> Publisher<CacheResult<T>> flow(RxCache rxCache, final String key, Flowable<T> source, Type type) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                return new CacheResult<>(ResultFrom.Remote, key, t);
            }
        });
    }
}
