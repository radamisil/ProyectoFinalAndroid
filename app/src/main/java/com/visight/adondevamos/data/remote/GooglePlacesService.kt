package com.visight.adondevamos.data.remote

import com.visight.adondevamos.data.remote.requests.GetPublicPlacesRequest
import com.visight.adondevamos.data.remote.responses.GetPublicPlacesResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class GooglePlacesService {
    lateinit var sRestAdapter: Retrofit
    var sApiClientInterface: ApiClientInterface? = null
    var sAPI_URL: String = "https://maps.googleapis.com/maps/api/place/"

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
        return sRestAdapter.create(serviceClass)
    }

    fun getClient(): ApiClientInterface {
        if (sApiClientInterface == null) {
            sApiClientInterface = createService(GooglePlacesService.ApiClientInterface::class.java)
        }
        return sApiClientInterface as ApiClientInterface
    }

    interface ApiClientInterface {
        @GET("search/json")
        fun getPublicPlaces(@Query("location") location: String,
                            @Query("radius") radius: Int,
                            @Query("types") types: String,
                            @Query("key") key: String): Single<GetPublicPlacesResponse>
    }
}