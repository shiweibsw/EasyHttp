package com.example.easyhttp.http.parser

import okhttp3.HttpUrl

/**
 * Created by shiwei on 2018/1/2.
 */
class DefaultUrlParser : UrlParser {
    override fun parseUrl(domainUrl: HttpUrl, url: HttpUrl): HttpUrl {
        if (null == domainUrl) return url
        return url.newBuilder()
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build()
    }
}