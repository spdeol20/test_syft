package com.android.retorfit

import com.android.models.DataModelNew
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis{
    @GET("/orgs/square/repos")
    fun callApi() : Call<ArrayList<DataModelNew>>

    @GET("/orgs/square/repos")
    fun callPagingApi(@Query("page") page: String): Call<ArrayList<DataModelNew>>
}