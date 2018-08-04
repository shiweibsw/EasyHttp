package com.example.easyhttp.http.cache.stategy;

import com.example.easyhttp.http.cache.CacheTarget;
import com.example.easyhttp.http.cache.RxCache;
import com.example.easyhttp.http.cache.RxCacheHelper;
import com.example.easyhttp.http.cache.data.CacheResult;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/**
 * 优先缓存并可设置超时时间
 * 作者: 赵成柱 on 2016/9/12 0012.
 */
public final class FirstCacheTimeoutStrategy implements IStrategy {
    private boolean isSync;
    private long milliSecond;

    /**
     * @param milliSecond 毫秒数
     */
    public FirstCacheTimeoutStrategy(long milliSecond) {
        this(milliSecond, false);
    }

    /**
     * @param milliSecond 毫秒数
     * @param isSync 是否用同步的方式保存缓存
     */
    public FirstCacheTimeoutStrategy(long milliSecond, boolean isSync) {
        this.isSync = isSync;
        this.milliSecond = milliSecond;
    }

    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = RxCacheHelper.loadCache(rxCache, key, type, true);
        cache = cache.filter(new Predicate<CacheResult<T>>() {
            @Override
            public boolean test(CacheResult<T> tCacheResult) throws Exception {
                return System.currentTimeMillis() - tCacheResult.getTimestamp() <= milliSecond;
            }
        });
        Observable<CacheResult<T>> remote;
        if (isSync) {
            remote = RxCacheHelper.loadRemoteSync(rxCache, key, source, CacheTarget.MemoryAndDisk, false);
        } else {
            remote = RxCacheHelper.loadRemote(rxCache, key, source, CacheTarget.MemoryAndDisk, false);
        }
        return cache.switchIfEmpty(remote);
    }

    @Override
    public <T> Publisher<CacheResult<T>> flow(RxCache rxCache, String key, Flowable<T> source, Type type) {
        Flowable<CacheResult<T>> cache = RxCacheHelper.loadCacheFlowable(rxCache, key, type, true);
        cache = cache.filter(new Predicate<CacheResult<T>>() {
            @Override
            public boolean test(CacheResult<T> tCacheResult) throws Exception {
                return System.currentTimeMillis() - tCacheResult.getTimestamp() <= milliSecond;
            }
        });
        Flowable<CacheResult<T>> remote;
        if (isSync) {
            remote = RxCacheHelper.loadRemoteSyncFlowable(rxCache, key, source, CacheTarget.MemoryAndDisk, false);
        } else {
            remote = RxCacheHelper.loadRemoteFlowable(rxCache, key, source, CacheTarget.MemoryAndDisk, false);
        }
        return cache.switchIfEmpty(remote);
    }
}
