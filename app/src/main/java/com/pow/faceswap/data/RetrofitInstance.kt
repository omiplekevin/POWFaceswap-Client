package com.pow.faceswap.data

import com.pow.faceswap.MainActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
	
	val restApi: RestAPI by lazy { 
		getInstance().create(RestAPI::class.java)
	}
	
	private val BASE_URL = "http://${MainActivity._SERVER_IP}/"
	
	private fun getInstance(): Retrofit {
		val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		val clientBuilder = OkHttpClient.Builder()
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.connectTimeout(10, TimeUnit.SECONDS)
			.addInterceptor(interceptor)
		
		return Retrofit.Builder().baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(clientBuilder.build())
			.build()
	}
}