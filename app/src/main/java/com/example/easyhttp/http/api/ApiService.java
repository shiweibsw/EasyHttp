package com.example.easyhttp.http.api;


import com.example.easyhttp.TestBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shiwei on 2016/4/28.
 * Retrofit 各种请求接口
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("query?key=7c2d1da3b8634a2b9fe8848c3a9edcba")
    Observable<ApiResponse<TestBean>> getDatas(@Field("pno") int pno, @Field("ps") int ps, @Field("dtype") String dtype);

}
