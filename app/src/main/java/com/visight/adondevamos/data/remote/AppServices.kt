package com.visight.adondevamos.data.remote

import com.visight.adondevamos.data.entity.PlaceAverageAvailability
import com.visight.adondevamos.data.entity.User
import com.visight.adondevamos.data.remote.requests.RegisterRequest
import com.visight.adondevamos.data.remote.requests.SendPlacePhotoRequest
import com.visight.adondevamos.data.remote.responses.AnalizedPhotoResponse
import com.visight.adondevamos.data.remote.responses.PollAverageResponse
import com.visight.adondevamos.data.remote.responses.UserResponse
import io.reactivex.Observable
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
                                        @Query("google_place_id") googlePlaceId: String? = null):
                Observable<PollAverageResponse>

        @GET("GlobalAveragee")
        fun getPlaceGlobalAverageAvailability(@Query("shop_id") shopId: String,
                                        @Query("google_place_id") googlePlaceId: String):
                Observable<List<PlaceAverageAvailability>>
    }

}