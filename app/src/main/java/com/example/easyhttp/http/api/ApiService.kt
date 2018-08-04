package com.example.easyhttp.http.api

import com.example.easyhttp.DOMAIN
import com.example.easyhttp.DOMAIN_TEST
import com.example.easyhttp.TestBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by shiwei on 2018/1/2.
 */
interface ApiService {

    @Headers("$DOMAIN:$DOMAIN_TEST")//需要动态修改url的情况才需要此项设置
    @FormUrlEncoded
    @POST("weixin/query?key=7c2d1da3b8634a2b9fe8848c3a9edcba")
    fun getDatas(@Field("pno") pno: Int, @Field("ps") ps: Int, @Field("dtype") dtype: String): Observable<ApiResponse<TestBean>>

}