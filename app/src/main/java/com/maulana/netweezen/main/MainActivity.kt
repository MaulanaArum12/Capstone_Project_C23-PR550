package com.maulana.netweezen.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maulana.netweezen.data.Data
import com.maulana.netweezen.R
import com.maulana.netweezen.tweet.TopicDetailActivity
import com.maulana.netweezen.about.AboutActivity
import com.maulana.netweezen.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvToko: RecyclerView
    private val list = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.custom_actionbar_home)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_grey)))
        rvToko = findViewById(R.id.rv_toko)
        rvToko.setHasFixedSize(true)

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.itemIconTintList = null
        binding.navView.menu.getItem(0).isChecked = true

        list.addAll(getListData())
        showRecyclerList()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_topic -> {
                Toast.makeText(applicationContext,"Coming Soon", Toast.LENGTH_SHORT).show()
            }
            R.id.action_about -> {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
            }
        }
        return true
    }

    @SuppressLint("Recycle")
    private fun getListData(): ArrayList<Data> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listData = ArrayList<Data>()
        for (i in dataName.indices) {
            val data = Data(dataName[i], dataPhoto.getResourceId(i, -1))
            listData.add(data)
        }
        return listData
    }

    private fun showRecyclerList() {
        rvToko.layoutManager = LinearLayoutManager(this)
        val mainAdapter = MainAdapter(list)
        rvToko.adapter = mainAdapter
        mainAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Data) {
                val intentToTopicDetailActivity = Intent(this@MainActivity, TopicDetailActivity::class.java)
                startActivity(intentToTopicDetailActivity)
            }
        })
    }
}