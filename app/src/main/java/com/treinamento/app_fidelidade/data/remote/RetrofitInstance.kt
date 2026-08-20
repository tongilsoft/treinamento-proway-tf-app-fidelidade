package com.treinamento.app_fidelidade.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
            LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_DATE_TIME)
        })
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: FidelidadeApi = retrofit.create(FidelidadeApi::class.java)
}