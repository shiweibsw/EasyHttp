
## EasyHttp

## 概述
基于RxJava2+Retrofit2+RxCache的网络请求框架，用最简洁的方式完成网络请求
    
     HttpManager.getDatasCached(HttpSubscriber(object : OnResultCallBack<TestBean> {
                    override fun onSuccess(t: TestBean) {
                     
                    }
                    override fun onError(code: Int, errorMsg: String) {
                       
                    }
                }, lifecycle), 1, 10, "json")


## 更新日志

#### 2018.8.04
* 增加RxCache缓存功能（默认优先网络）
* 更新重要框架版本

#### 2018.1.02
* 使用Kotlin重构；
* 增加生命周期管理；
* 支持动态修改BaseUrl。

#### 2017.05.04
* 增加自定义的GsonTSpeaker，解决ClassCastException问题。

#### 2017.04.28
* onError(int code, String errorMsg)非网络问题不再统一返回1003，而是返回服务器的错误状态码(万一有用呢\(^o^)/~)。

#### 2017.04.26
* 使用RxCache缓存机制，可自定义缓存过期时间，及数据分页缓存等功能。
* 统一的请求错误处理；
* 统一的网络状态判断处理；
* 基于HttpLoggingInterceptor的请求日志打印。

## 使用说明

####1 引入插件


        compile 'io.reactivex.rxjava2:rxjava:2.1.8'
        compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
        compile 'com.squareup.retrofit2:retrofit:latest.release'
        compile 'com.squareup.retrofit2:converter-gson:latest.release'
        compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
        compile 'com.squareup.okhttp3:logging-interceptor:3.6.0'
        compile "android.arch.lifecycle:runtime:1.0.3"
        compile 'com.google.code.gson:gson:2.8.5'
        compile 'com.jakewharton:disklrucache:2.0.2'

#### 2 将http包下的所有内容拷贝到工程；

**ApiResponse**——封装的返回数据模板（里面的error_code，reason，result名称需要跟后台对应好，通常情况下error_code代码状态码，reason为成功或失败的提示信息，result中为具体的数据，由于数据格式未知所以使用泛型代表）

**ApiService**——Retrofit的数据请求接口。注意一下每个方法的返回值类型。（我们真正需要的是TestBean中的数据它必须被ApiResponse包装，最后返回Observable类型）

**OnResultCallBack**——请求成功或失败的回调。

**ApiException**——公共的请求错误处理类。

**HttpSubscriber**——公共的请求订阅者。

**HttpManager**——发起请求的管理类。

#### 3 配置好BASE_URL及SUCCESS_CODE；

#### 4 使用方式详见Demo

#### 5 缓存类型
缓存模块来自 此处 https://github.com/z-chu/RxCache

在`CacheStrategy` 类中提供如下缓存策略：

 策略选择                   | 摘要      
 ------------------------- | ------- 
 firstRemote()             | 优先网络
 firstCache() |优先缓存
 firstCacheTimeout(milliSecond) |优先缓存,并设置超时时间
 onlyRemote() | 仅加载网络，但数据依然会被缓存
 onlyCache()           | 仅加载缓存 
 cacheAndRemote()              | 先加载缓存，后加载网络   
 none()              | 仅加载网络，不缓存

