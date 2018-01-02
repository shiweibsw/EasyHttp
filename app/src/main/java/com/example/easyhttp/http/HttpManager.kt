package com.knightdavion.kotlin.ibiliplayer.data.remote

import android.util.Log
import com.example.easyhttp.*
import com.example.easyhttp.http.api.ApiResponse
import com.example.easyhttp.http.api.ApiService
import com.example.easyhttp.http.convert.CustomGsonConverterFactory
import com.example.easyhttp.http.parser.DefaultUrlParser
import com.example.easyhttp.http.parser.UrlParser
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by shiwei on 2018/1/2.
 */
object HttpManager {

    private lateinit var mRetrofit: Retrofit
    private lateinit var mApiService: ApiService
    private lateinit var okHttpClient: OkHttpClient
    val DEFAULT_TIMEOUT: Long = 10L
    private val mDomainNameHub = HashMap<String, HttpUrl>()
    private var mUrlParser: UrlParser = DefaultUrlParser()

    init {
        cacheCustomDomain()
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(initRedirectInterceptor())
                .addInterceptor(initHttpLoggingInterceptor())
                .addInterceptor { chain ->
                    val original = chain!!.request()
                    val requestBuilder = original.newBuilder().header("Accept", "application/json")
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }

        okHttpClient = builder.build()

        mRetrofit = Retrofit.Builder()
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
        mApiService = mRetrofit.create(ApiService::class.java)
    }


    private fun <T> toSubscribe(o: Observable<ApiResponse<T>>, s: Observer<T>) {
        o.subscribeOn(Schedulers.io())
                .map(Function<ApiResponse<T>, T>() { response ->
                    response.getDatas()
                }).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s)
    }

    /**
     * 缓存url
     */
    private fun cacheCustomDomain() {
        //在这里加入你要动态修改的URL
        mDomainNameHub.put(DOMAIN_TEST, checkUrl(SECOND_URL))
    }

    /**
     * 重定向拦截器
     */
    private fun initRedirectInterceptor(): Interceptor {
        return Interceptor { it.proceed(processRequest(it.request())) }
    }

    /**
     * 打印调试信息
     */
    private fun initHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("HttpManager", message) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    /**
     *解析Request的Header
     */
    private fun processRequest(request: Request): Request {
        var newBuilder = request.newBuilder()
        var domainName = obtainDomainNameFromHeaders(request)
        var httpUrl: HttpUrl? = null
        if (domainName.isNotEmpty()) {
            httpUrl = fetchDomain(domainName)
            newBuilder.removeHeader(DOMAIN)
        }
        if (null != httpUrl) {
            val newUrl = mUrlParser.parseUrl(httpUrl, request.url())
            return newBuilder.url(newUrl).build()
        }
        return newBuilder.build()
    }

    /**
     * 解析请求的Header
     */
    private fun obtainDomainNameFromHeaders(request: Request): String {
        val headers = request.headers(DOMAIN)
        if (headers == null || headers.size == 0)
            return ""
        if (headers.size > 1)
            throw IllegalArgumentException("Only one Domain-Name in the headers")
        return request.header(DOMAIN)
    }

    /**
     *获得Header对应的HttpUrl
     */
    fun fetchDomain(domainName: String): HttpUrl? {
        return mDomainNameHub[domainName]
    }

    /**
     * 包装url类型String->HttpUrl
     */
    fun checkUrl(url: String): HttpUrl {
        val parseUrl = HttpUrl.parse(url)
        return parseUrl!!
    }


    fun getDatas(subscriber: Observer<TestBean>, pno: Int, ps: Int, dtype: String) {
        toSubscribe(mApiService.getDatas(pno, ps, dtype), subscriber)
    }


}
