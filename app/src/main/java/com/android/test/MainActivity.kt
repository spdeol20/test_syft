package com.android.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

class MainActivity : AppCompatActivity() {
    lateinit var host: NavHostFragment;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Syft"

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.getItemId()) {
            R.id.menu_filter -> {

                var fragment  = supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.primaryNavigationFragment as ListFragment
//                var fragment = host.childFragmentManager.fragments as ListFragment
                fragment.OpenFiter()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
}
}


