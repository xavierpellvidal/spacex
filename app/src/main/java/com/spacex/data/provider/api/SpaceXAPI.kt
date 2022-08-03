package com.spacex.data.provider.api

import com.spacex.data.model.LaunchesQueryResponse
import com.spacex.data.model.Query
import com.spacex.data.model.Rocket
import com.spacex.util.Constants
import com.spacex.util.Constants.RETROFIT_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

class SpaceXAPI {

    lateinit var retrofit: Retrofit

    fun initApiService() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.readTimeout(RETROFIT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.writeTimeout(RETROFIT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClient.addInterceptor(interceptor)

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_V4_SERVICE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): SpacexServiceApi {
        return retrofit.create(SpacexServiceApi::class.java)
    }

    // Services
    interface SpacexServiceApi {
        /**
         * GET ALL ROCKETS
         */
        @GET("rockets")
        suspend fun getRocketsList(): Response<MutableList<Rocket>>

        /**
         * GET ROCKET BY ID
         */
        @GET("rockets/{id}")
        suspend fun getRocket(@Path("id") id: String): Response<Rocket>

        /**
         * GET LAUNCHES BY ROCKET ID
         */
        @POST("launches/query")
        suspend fun getLaunches(@Body query: Query): Response<LaunchesQueryResponse>
    }

    companion object {
        private var instance: SpaceXAPI? = null
        fun getInstance(): SpaceXAPI {
            if (instance == null) {
                instance = SpaceXAPI()
            }
            return instance!!
        }
    }
}