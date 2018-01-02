package com.example.easyhttp.http.parser

import okhttp3.HttpUrl

/**
 * Created by shiwei on 2018/1/2.
 */
interface UrlParser {
    abstract fun parseUrl(domainUrl: HttpUrl, url: HttpUrl): HttpUrl
}