package com.visight.adondevamos.data.remote

import com.visight.adondevamos.data.remote.responses.PlaceGlobalAvailabilityResponseData
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.data.remote.requests.RegisterRequest
import com.visight.adondevamos.data.remote.requests.SendPlacePhotoRequest
import com.visight.adondevamos.data.remote.responses.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


class AppServices {
    var sRestAdapter: Retrofit? = null
    //var sAPI_URL = "http://40.84.190.225/api/"
    var sAPI_URL = "http://adondevamos.ddns.net/api/"
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

    fun getClient(): ApiClientInterface {
        if (sApiClientInterface == null) {
            sApiClientInterface = createService(AppServices.ApiClientInterface::class.java)
        }
        return sApiClientInterface!!
    }

    interface ApiClientInterface {
        @POST("register")
        fun register(@Body registerRequest: RegisterRequest) : Observable<User>

        @GET("user")
        fun login(@Query("Email") email: String): Observable<UserResponse>

        @POST("calculateIA")
        fun sendPhoto(@Body sendPlacePhotoRequest: SendPlacePhotoRequest) : Observable<AnalizedPhotoResponse>

        @GET("IntelligenceAndPollAverage")
        fun getPlaceAverageAvailability(@Query("shop_id") shopId: String? = null,
                                        @Query("filterGooglePlaceid") googlePlaceId: String? = null):
                Observable<PollAverageResponseData>

        @GET("GlobalAverage")
        fun getPlaceGlobalAverageAvailability(@Query("shop_id") shopId: String? = null,
                                        @Query("google_place_id") googlePlaceId: String? = null):
                Observable<PlaceGlobalAvailabilityResponseData>

        @GET("promotions")
        fun getPromotions(@Query("filterPlace") filterPlace: String? = null,
                          @Query("filterUser") filterUser: String? = null): Observable<PromotionsResponse>

        @GET("tradeType")
        fun getCustomPublicPlaces(@Query("filter") filter: String? = null): Observable<CustomPublicPlacesResponseData>
    }

}