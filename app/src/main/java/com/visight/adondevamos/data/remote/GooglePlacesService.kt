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
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class GooglePlacesService {
    lateinit var sRestAdapter: Retrofit
    lateinit var sApiClientInterface: ApiClientInterface
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
        return sApiClientInterface
    }

    interface ApiClientInterface {
        @POST("findplacefromtext")
        fun getPublicPlaces(@Body getPublicPlacesRequest: GetPublicPlacesRequest): Single<GetPublicPlacesResponse>
    }
}