package com.gonzalez.blanchard.perroscomposeapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getPerros(@Url url:String): Response<DogResponse>

}