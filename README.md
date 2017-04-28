
## EasyHttp

## 概述
基于RxJava2+Retrofit2+RxCache的网络请求框架

## 更新日志
#### 2017.04.28
* onError(int code, String errorMsg)非网络问题不再统一返回1003，而是返回服务器的错误状态码。

#### 2017.04.26
* 使用RxCache缓存机制，可自定义缓存过期时间，及数据分页缓存等功能。
* 统一的请求错误处理；
* 统一的网络状态判断处理；
* 基于HttpLoggingInterceptor的请求日志打印。

## 使用说明

####1 引入插件

    //Rxjava2
    compile 'io.reactivex.rxjava2:rxjava:2.0.7'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    //Retrofit2
    compile 'com.squareup.retrofit2:retrofit:latest.release'
    compile 'com.squareup.retrofit2:converter-gson:latest.release'
    compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
    //RxCache
    compile "com.github.VictorAlbertos.RxCache:runtime:1.8.0-2.x"
    compile 'com.github.VictorAlbertos.Jolyglot:gson:0.0.3'
    //Okhttp-interceptor
    compile 'com.squareup.okhttp3:logging-interceptor:3.6.0'

#### 2 将http包下的所有内容拷贝到工程；

**ApiResponse**——封装的返回数据模板（里面的error_code，reason，result名称需要跟后台对应好，通常情况下error_code代码状态码，reason为成功或失败的提示信息，result中为具体的数据，由于数据格式未知所以使用泛型代表）

**ApiService**——Retrofit的数据请求接口。注意一下每个方法的返回值类型。（我们真正需要的是TestBean中的数据它必须被ApiResponse包装，最后返回Observable类型）

**CacheProvider**——RxCache的缓存接口，注意它的第一个参数类型必须和Retrofit数据请求接口的返回值类型一样。

**OnResultCallBack**——请求成功或失败的回调。

**ApiException**——公共的请求错误处理类。

**HttpSubscriber**——公共的请求订阅者。

**HttpManager**——发起请求的管理类。

#### 3 配置好BASE_URL及SUCCESS_CODE；
#### 4 使用方式详见Demo，如果对RxCache不了解，看[这里](https://github.com/VictorAlbertos/RxCache)

