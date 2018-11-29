package com.alexandre.skiresort.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.alexandre.skiresort.BuildConfig
import com.alexandre.skiresort.service.model.SkiResort
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val TAG = "SkiResortListService"

/**
 * Get all the ski resort
 */
fun requestSkiResort(
        service: SkiResortListService,
        onSuccess: (skiResorts: List<SkiResort>) -> Unit,
        onError: (error: String) -> Unit) : MutableLiveData<List<SkiResort>> {

    val result =  MutableLiveData<List<SkiResort>>()
    service.getSkiResorts().enqueue(
            object : Callback<List<SkiResort>> {
                override fun onFailure(call: Call<List<SkiResort>>?, t: Throwable) {
                    Log.e(TAG, "fail to get data", t)
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<List<SkiResort>>?,
                        response: Response<List<SkiResort>>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val skiResorts = response.body() ?: emptyList()
                        onSuccess(skiResorts)
                        result.value = skiResorts
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
    return result
}

interface SkiResortListService{

    /**
     * Get ski resort list.
     */
    @GET("v0/b/ski-resort-be7dc.appspot.com/o/resort-weather.json?alt=media&token=f40092bf-2e06-4077-a84e-0906b834d487")
    fun getSkiResorts(): Call<List<SkiResort>>

    companion object {
        private const val BASE_URL = "https://firebasestorage.googleapis.com/"

        fun create(): SkiResortListService {
            val logger = HttpLoggingInterceptor()
            logger.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SkiResortListService::class.java)
        }
    }
}