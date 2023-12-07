package com.example.test.Retrofit

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@SuppressLint("StaticFieldLeak")
object ApiClient {
    var context: Context? = null

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }


    private val retrofitnews: Retrofit by lazy {
        val url: String? =Constants.newsAPIUrl
        Retrofit.Builder()
            .baseUrl(url!!)
            .client(httpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: APIInterface by lazy {
        retrofitnews.create(APIInterface::class.java)
    }
    private val retrofitweather: Retrofit by lazy {
        val url: String? =Constants.weatherAPIUrl
        Retrofit.Builder()
            .baseUrl(url!!)
            .client(httpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiServiceweather: APIInterface by lazy {
        retrofitweather.create(APIInterface::class.java)
    }
    fun changeApiBaseUrl(newApiBaseUrl: String?): APIInterface? {
        val interceptor = HttpLoggingInterceptor()

        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(getUnsafeOkHttpClient()!!)
            .baseUrl(newApiBaseUrl!!)
            .build()

        return retrofit.create(APIInterface::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            // Install the all-trusting trust manager
            val sslContext =
                SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            // final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            val builder = OkHttpClient.Builder()
            //builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder //.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(
                    600,
                    TimeUnit.SECONDS
                ) //.writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


}