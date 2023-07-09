package com.aiemail.superemail.Models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {
    @GET("v1/people/{resourceName}")
    fun getPerson(
        @Path("resourceName") resourceName: String,
        @Query("personFields") personFields: String,
        @Query("key") apiKey: String
    ): Call<Person>
}
