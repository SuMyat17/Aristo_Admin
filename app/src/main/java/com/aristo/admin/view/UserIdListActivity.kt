package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityUserIdListBinding
import com.aristo.admin.view.adapters.UserIdListRecyclerViewAdapter

class UserIdListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserIdListBinding
    private val userIdListAdapter by lazy { UserIdListRecyclerViewAdapter(this, ArrayList()) }
    private val userIdListLayoutManager by lazy { LinearLayoutManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserIdListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        // Hide the application name (title) from the toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupRecyclerView()

        binding.backButton.setOnClickListener { finish() }

        binding.progressLoading.visibility = View.VISIBLE

        CategoryFirebase.getCustomerIdList { isSuccess, data ->

            binding.progressLoading.visibility = View.GONE

            if (isSuccess) {
                if (data != null) {

                    userIdListAdapter.updateData(data)
                }
            } else {
                Toast.makeText(this, "Can't retrieve datas.", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun setupRecyclerView() {
        // Set up the RecyclerView
        binding.rvUserIdList.layoutManager = userIdListLayoutManager
        binding.rvUserIdList.adapter = userIdListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        // Customize the searchView as needed (optional)
        searchView.queryHint = "Search..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                userIdListAdapter.filter.filter(newText)
                return true
            }
        })

        return true
    }



}