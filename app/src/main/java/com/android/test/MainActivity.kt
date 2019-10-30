package com.android.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.adapter.DataAdapter
import com.android.models.DataModel
import com.android.retorfit.Retro
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var layoutManage: RecyclerView.LayoutManager;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManage  = LinearLayoutManager(this@MainActivity);
        rvList.layoutManager = layoutManage;

        val call=  Retro.calApi.callApi()
        call.enqueue( object :   retrofit2.Callback<List<DataModel>> {
            override fun onFailure(call: Call<List<DataModel>>, t: Throwable) {
                txtLoading.text = getString(R.string.failure)
            }

            override fun onResponse(call: Call<List<DataModel>>, response: Response<List<DataModel>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty() ){
                    txtLoading.visibility = View.GONE
                    rvList.adapter = DataAdapter(this@MainActivity,response.body())
                }
                else{
                    txtLoading.text = getString(R.string.failure)
                }
            }

        })
    }
}


