package com.example.easyhttp.http.convert;

import com.example.easyhttp.http.api.ApiResponse;
import com.example.easyhttp.http.exception.ApiException;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import static com.example.easyhttp.AppConfigKt.SUCCESS_CODE;


final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            ApiResponse result = gson.fromJson(response, ApiResponse.class);
            int code = result.getCode();
            if (code == SUCCESS_CODE) {
                return gson.fromJson(response, type);
            } else {
                throw new ApiException(Integer.valueOf(code), result.getMsg());
            }
        } finally {
            value.close();
        }
    }
}