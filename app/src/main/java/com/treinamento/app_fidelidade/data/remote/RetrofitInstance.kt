package com.treinamento.app_fidelidade.data.remote



import com.google.gson.Gson
import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://127.0.0.1:3000"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: FidelidadeApi = retrofit.create(FidelidadeApi::class.java)
}