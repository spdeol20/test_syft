package com.android.retorfit

import com.android.models.DataModelNew
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Apis{
    @GET("/orgs/square/repos")
    fun callApi() : Call<DataModelNew>

    @GET("/search/repositories")
    fun callPagingApi(@QueryMap  map:HashMap<String,String>) : Call<DataModelNew>

}