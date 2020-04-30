package com.mvgreen.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("register")
    fun register(): Single<Response>

    @GET("update")
    fun update(@Query("id") id: Int): Single<Response>

    @GET("submit")
    fun submit(@Query("id") id: Int, @Query("line") line: String): Single<Response>

}