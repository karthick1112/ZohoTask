package com.example.test.Retrofit

import com.example.test.ModelClass.GetNewsListResponseBody
import com.example.test.ModelClass.GetWeatherListResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


interface APIInterface {

    @GET("data/2.5/weather")
    fun getWeatherLists(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appId: String): Observable<GetWeatherListResponseBody>

    @GET("v4/articles/?format=json&limit=20")
    fun getNewsLists(@Query("offset") offset: String): Observable<GetNewsListResponseBody>

}