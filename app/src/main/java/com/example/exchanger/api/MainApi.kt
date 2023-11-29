package com.example.exchanger.api

import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {
    @GET("v6/17e0fea5c13b86e478434590/latest/{base_code}")
    suspend fun getCurrencyRate(@Path("base_code") base_code: String): CurrencyData
}
