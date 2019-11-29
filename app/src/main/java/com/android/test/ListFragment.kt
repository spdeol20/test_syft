package com.android.test


import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.adapter.DataAdapter
import com.android.models.DataModelNew
import com.android.utils.Injection
import com.android.viewmodel.SearchRepositoriesViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), View.OnClickListener {

    private var seletedFilterPos: Int = 0
    private lateinit var viewModel: SearchRepositoriesViewModel
    lateinit var navController: NavController
    var data:  DataModelNew = DataModelNew(false, ArrayList(),0)
    var filterList:  DataModelNew = DataModelNew(false, ArrayList(),0)
    public lateinit var adapter: DataAdapter
    private lateinit var layoutManage: RecyclerView.LayoutManager;
    var isload = false;
    var page: String="";
      var map:LinkedHashMap<String,List<Int>> = LinkedHashMap()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }
    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }
    companion object {
        private const val LAST_SEARCH_QUERY: String = "all"
        private const val DEFAULT_QUERY = "all"
    }
    private fun showEmptyList(show: Boolean) {
        if (show) {
            rvList.visibility = View.GONE
            txtLoading.visibility = View.VISIBLE
            txtLoading.text="somthing is wrong"
        } else {
            rvList.visibility = View.VISIBLE
            txtLoading.visibility = View.GONE

        }
    }

    private fun setupScrollListener() {
        rvList.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManage.itemCount
                val visibleItemCount = layoutManage.childCount
                val lastVisibleItem =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }
    private fun initAdapter() {
        adapter =  DataAdapter(activity!!, data)
        rvList.adapter = adapter
        viewModel.repos.observe(this, Observer<DataModelNew> {
            if (it != null) {
                filtering(it)
                showEmptyList(it.items?.size == 0)
                data.items!!.addAll(it.items!!)
                txtCount.text = getString(R.string.total_count) +" "+it.total_count.toString()
                if (seletedFilterPos == 0) {
                    adapter.notifyDataSetChanged()
                } else {
                    setFilterData(map.keys.toTypedArray())
                }
            }
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(activity, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun filtering(it: DataModelNew) {
        var i:Int=-1
        for (item in it.items!!) {
            i++;
            var list: List<Int> = ArrayList<Int>()
            if (map.containsKey(item.language)) {
                list = map.get(item.language)!!
                (list!! as ArrayList).add(i)
                map!!.put(item.language,list)
            }
            else{
                (list!! as ArrayList).add(i)
                map!!.put(item.language,list)
            }
        }
    }
    private fun clearFilter(){
        map.clear()
    }

    private fun initSearch(query: String) {
        etSearch.setText(query)

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        etSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        etSearch.text.trim().let {
            if (it.isNotEmpty()) {
                rvList.scrollToPosition(0)
                data = DataModelNew(false,ArrayList(),0)
                viewModel.searchRepo(it.toString())
                adapter =  DataAdapter(activity!!, data)
                rvList.adapter = adapter
                clearFilter()
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(activity!!)).get(SearchRepositoriesViewModel::class.java)
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchRepo(query)
        navController = Navigation.findNavController(view)
        layoutManage = LinearLayoutManager(activity)
        rvList.layoutManager = layoutManage;
        initAdapter()
        setupScrollListener()
        initSearch(query)

    }

    fun OpenFiter() {
        Log.d("Activity", "OpenFiter:  ")
        val nameLang  = map.keys.toTypedArray()
        (nameLang)[0] = "All"
//        val nameLang = arrayOf("Red", "Orange", "Yellow", "Blue")

        val arrayAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_single_choice,
            nameLang)
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.filter))
        builder.setSingleChoiceItems(arrayAdapter, seletedFilterPos
        ) { dialog, pos ->
            seletedFilterPos = pos;
              filterList = DataModelNew(false, ArrayList(),0)
            setFilterData(nameLang)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setFilterData(nameLang: Array<String>) {
        if (seletedFilterPos>0) {
            var itemPresentInMain = map.get(nameLang.get(seletedFilterPos))
            for (j in 0..itemPresentInMain!!.size) {
            if (!(filterList.items)!!.contains(data.items!!.get(j))) {
                (filterList.items)!!.add(data.items!!.get(j))
            }
        }
            adapter = DataAdapter(activity!!, filterList)
            rvList.adapter = adapter
            txtCount.text = getString(R.string.total_count) +" "+filterList!!.items!!.size
        }
        else{

            adapter = DataAdapter(activity!!, data)
            rvList.adapter = adapter
            txtCount.text = getString(R.string.total_count) +" "+data!!.items!!.size
        }

    }


}