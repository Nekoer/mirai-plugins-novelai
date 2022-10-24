package com.hcyacg.data

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


public class LocalCookieJar : CookieJar {
    private var cookies: List<Cookie>? = null
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies ?: ArrayList<Cookie>()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
    }
}