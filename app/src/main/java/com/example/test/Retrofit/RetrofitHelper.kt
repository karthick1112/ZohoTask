package com.example.test.Retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {

    val newsUrl = "https://api.spaceflightnewsapi.net/"

    val okClient = OkHttpClient.Builder()
        .connectTimeout(10000, TimeUnit.SECONDS)
        .readTimeout(10000, TimeUnit.SECONDS).build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(newsUrl)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}