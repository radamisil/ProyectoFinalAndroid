package com.visight.adondevamos.data.remote

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


class AppServices {
    var sRestAdapter: Retrofit? = null
    var sAPI_URL = "http://40.84.143.87/Home/"
    var sApiClientInterface: ApiClientInterface? = null

    fun <S> createService(serviceClass: Class<S>): S {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
        client.connectTimeout(50, TimeUnit.SECONDS)
        client.readTimeout(50, TimeUnit.SECONDS)
        sRestAdapter = Retrofit.Builder()
            .baseUrl(sAPI_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return sRestAdapter!!.create(serviceClass)
    }

    fun getsRestAdapter(): Retrofit? {
        return sRestAdapter
    }

    fun getClient(): ApiClientInterface{
        if(sApiClientInterface == null){
            sApiClientInterface = createService(AppServices.ApiClientInterface::class.java)
        }
        return sApiClientInterface!!
    }

    interface ApiClientInterface {
        @GET("registracion")
        fun register()
    }

}