/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.livedata.RepoSearchResult
import com.android.models.DataModelNew
import com.android.retorfit.Retro
import retrofit2.Call
import retrofit2.Response

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository() {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()
    private val data = MutableLiveData<DataModelNew>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RepoSearchResult {
        Log.e("adapter listScrolled","New query: ")
        lastRequestedPage = 1
        requestAndSaveData(query)


        return RepoSearchResult(data,networkErrors)
    }

    fun requestMore(query: String) {
        Log.e("adapter listScrolled","requestMore : ")
        requestAndSaveData(query)
    }

      fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return
        isRequestInProgress = true
          var map: HashMap<String,String> = HashMap();
          map.put("q",query)
          map.put("sort","stars")
          map.put("order","desc")
          map.put("page",""+lastRequestedPage)
          val call = Retro.calApi.callPagingApi(map)
          call.enqueue(object : retrofit2.Callback<DataModelNew> {
              override fun onFailure(call: Call<DataModelNew>, t: Throwable) {
                  networkErrors.postValue(t.message)
                  isRequestInProgress = false
              }

              override fun onResponse(
                  call: Call<DataModelNew>,
                  response: Response<DataModelNew>
              ) {
                  data.postValue(response.body())
                  lastRequestedPage++
                  isRequestInProgress = false
              }

          })

    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}
