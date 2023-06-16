package com.maulana.netweezen.about

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maulana.netweezen.main.MainActivity
import com.maulana.netweezen.R
import com.maulana.netweezen.databinding.ActivityAboutBinding

@Suppress("DEPRECATION")
class AboutActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.itemIconTintList = null
        binding.navView.menu.getItem(2).isChecked = true

        supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.custom_actionbar_user)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_grey)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                startActivity(Intent(this@AboutActivity, MainActivity::class.java))
            }
            R.id.action_topic -> {
                Toast.makeText(applicationContext,"Anda Memilih Topic", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}