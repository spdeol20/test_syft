package com.android.test


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.adapter.DataAdapter
import com.android.models.DataModelNew
import com.android.retorfit.Retro
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Response
import java.lang.reflect.Array.get

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    var data: ArrayList<DataModelNew>? = ArrayList()
    public lateinit var adapter: DataAdapter
    private lateinit var layoutManage: RecyclerView.LayoutManager;
    var isload = false;
    var page: String="";
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        layoutManage = LinearLayoutManager(activity)
        rvList.layoutManager = layoutManage;
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val visibleItemCount = layoutManage.childCount
                val firstVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                Log.e("call paging","number visibleItemCount - "+visibleItemCount+" firstVisibleItem- "+firstVisibleItem+" total = "+totalItemCount)
                if (isload ) {
                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0) {
                        isload = false
                        callPaginApi(page)
                    }//                    && totalItemCount >= ClothesFragment.itemsCount
                }
            }
        })

        val call = Retro.calApi.callApi()
        call.enqueue(object : retrofit2.Callback<ArrayList<DataModelNew>> {
            override fun onFailure(call: Call<ArrayList<DataModelNew>>, t: Throwable) {
                t.printStackTrace()
            }
//            <https://api.github.com/organizations/82592/repos?page=2>; rel="next", <https://api.github.com/organizations/82592/repos?page=8>; rel="last"
            override fun onResponse(
                call: Call<ArrayList<DataModelNew>>,
                response: Response<ArrayList<DataModelNew>>
            ) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    isload = true
                    var next:String ?= ""
                    val headers = response.headers()
                    val listofUrl = response.headers().get("link")!!.split(",")
                    for (item in listofUrl){
                        if (item.contains("next")){
                            page = item.substring(item.indexOf("<") + 1, item.indexOf(">")).split("=").get(1);
                        }
                    }
                    data = response.body()
                    txtLoading.visibility = View.GONE

                    adapter  = DataAdapter(activity!!, data)
                    rvList.adapter = adapter
                } else {
                    txtLoading.text = getString(R.string.failure)
                }
            }

        })
    }

    fun callPaginApi(num:String){
        Log.e("call paging","number "+num)
        val call = Retro.calApi.callPagingApi(num)
        call.enqueue(object : retrofit2.Callback<ArrayList<DataModelNew>> {
            override fun onFailure(call: Call<ArrayList<DataModelNew>>, t: Throwable) {
                t.printStackTrace()
            }
            //            <https://api.github.com/organizations/82592/repos?page=2>; rel="next", <https://api.github.com/organizations/82592/repos?page=8>; rel="last"
            override fun onResponse(
                call: Call<ArrayList<DataModelNew>>,
                response: Response<ArrayList<DataModelNew>>
            ) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    isload = true
                    val headers = response.headers()
                    val listofUrl = response.headers().get("link")!!.split(",")
                    for (item in listofUrl){
                        if (item.contains("next")){
                            page = item.substring(item.indexOf("<") + 1, item.indexOf(">")).split("=").get(1);
                        }
                    }

                    data!!.addAll(response.body()!!.toMutableList())
                    adapter!!.notifyDataSetChanged()
                } else {
                    txtLoading.text = getString(R.string.failure)
                }
            }

        })
    }

}