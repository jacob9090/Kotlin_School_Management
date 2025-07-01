package com.jacob.fruitoftek.kotlinschoolmanagement.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://fruitoftek.com/fedco/kotlin/"
    private const val CONNECTION_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    // Logging interceptor for debugging API requests/responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with headers and error handling
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            try {
                val response = chain.proceed(request)
                if (!response.isSuccessful) {
                    throw HttpException(response)
                }
                response
            } catch (e: Exception) {
                throw when (e) {
                    is HttpException -> e
                    else -> NetworkException("Network error: ${e.message ?: "Unknown error"}")
                }
            }
        }
        .build()

    // Directly expose an ApiService instance (no need to call .create!)
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// Custom exceptions for network and HTTP errors
class NetworkException(message: String) : IOException(message)
class HttpException(response: okhttp3.Response) : IOException("HTTP ${response.code} ${response.message}")
class EmptyResponseException : IOException("Empty response from server")


//object RetrofitClient {
//    private const val BASE_URL = "https://fruitoftek.com/fedco/kotlin/"
//    private const val CONNECTION_TIMEOUT = 30L
//    private const val READ_TIMEOUT = 30L
//    private const val WRITE_TIMEOUT = 30L
//
//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
//        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//        .addInterceptor(loggingInterceptor)
//        .addInterceptor { chain ->
//            val request = chain.request().newBuilder()
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
//                .build()
//
//            try {
//                val response = chain.proceed(request)
//                if (!response.isSuccessful) {
//                    throw HttpException(response)
//                }
//                response
//            } catch (e: Exception) {
//                throw when (e) {
//                    is HttpException -> e
//                    else -> NetworkException("Network error: ${e.message ?: "Unknown error"}")
//                }
//            }
//        }
//        .build()
//
//    val instance: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }
//}
//
//class NetworkException(message: String) : IOException(message)
//class HttpException(response: okhttp3.Response) : IOException("HTTP ${response.code} ${response.message}")
//class EmptyResponseException : IOException("Empty response from server")