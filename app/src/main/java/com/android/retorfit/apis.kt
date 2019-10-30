package com.android.retorfit

import com.android.models.DataModel
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface Apis{
    @GET("/orgs/square/repos")
    fun callApi() : Call<List<DataModel>>
}