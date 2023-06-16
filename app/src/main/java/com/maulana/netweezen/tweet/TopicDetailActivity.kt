package com.maulana.netweezen.tweet


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maulana.netweezen.*
import com.maulana.netweezen.about.AboutActivity
import com.maulana.netweezen.api.ApiConfig
import com.maulana.netweezen.databinding.ActivityTopicDetailBinding
import com.maulana.netweezen.main.MainActivity
import com.maulana.netweezen.data.Data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION", "UNCHECKED_CAST")
class TopicDetailActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityTopicDetailBinding
    private lateinit var rvDetail: RecyclerView
    private val list = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalscore = intent.getStringExtra("totalScore")

        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.itemIconTintList = null
        binding.navView.menu.getItem(0).isChecked = false
        binding.btnScore.text = totalscore
        binding.moreTweetButton.setOnClickListener {startMoreTweet()}

        supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.custom_actionbar_topicdetail)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_grey)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        rvDetail = findViewById(R.id.rv_detail)
        rvDetail.setHasFixedSize(true)

        val data = intent.getParcelableExtra<Data>("netweezen") as Data
        Log.d("TopicDetailActivity Data", data.toString())
        val ivDetailPhoto: ImageView = findViewById(R.id.detail_image)
        ivDetailPhoto.setImageResource(data.photo)

        list.addAll(getListToko())
        findDataTweet()
    }

    private fun startMoreTweet() {
        val intent = Intent(this, MoreTweetActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("Recycle")
    private fun getListToko(): ArrayList<Data> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listData = ArrayList<Data>()
        for (i in dataName.indices) {
            val data = Data(dataName[i], dataPhoto.getResourceId(i, -1))
            listData.add(data)
        }
        return listData
    }

    private fun findDataTweet() {
        showLoading(true)
        val client = ApiConfig.getApiService().getTweet( "jalanrusak")
        client.enqueue(object : Callback<TweetResponse> {
            override fun onResponse(
                call: Call<TweetResponse>,
                response: Response<TweetResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setTweetScore(responseBody)
                        setTweetData(responseBody.tweets as List<TweetsItem>)
                    }
                } else {
                    Log.e("TopicDetailActivity.TAG", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TweetResponse>, t: Throwable) {
                showLoading(false)
                Log.e("TopicDetailActivity.TAG", "onFailure: ${t.message}")
            }
        })
    }
    private fun setTweetScore(restaurant: TweetResponse) {
        binding.btnScore.text = restaurant.count.toString()
    }

    private fun setTweetData(consumerTweets: List<TweetsItem>) {
        val listReview = ArrayList<String>()
        val listClean = ArrayList<String>()
        val listUsername = ArrayList<String>()
        val listScore = ArrayList<String>()
        val listLike = ArrayList<String>()
        val listComment = ArrayList<String>()
        for (review in consumerTweets) {

            listReview.add(
                """
                ${review.tweet}
                """.trimIndent()
            )
            listClean.add(
                """
                ${review.clean}
                """.trimIndent()
            )
            listUsername.add(
                """
                ${review.username}
                """.trimIndent()
            )
            listScore.add(
                """
                ${review.aspScore}
                """.trimIndent()
            )
            listLike.add(
                """
                ${review.like}
                """.trimIndent()
            )
            listComment.add(
                """
                ${review.reply}
                """.trimIndent()
            )

        }
        rvDetail.layoutManager = LinearLayoutManager(this)
        val adapter = TweetAdapter(listReview, listClean, listUsername, listScore, listLike, listComment)
        binding.rvDetail.adapter = adapter

        adapter.setOnItemClickCallback(object : TweetAdapter.OnItemClickCallback {
            override fun onItemClicked() {
                val intentToTopicDetailActivity = Intent(this@TopicDetailActivity, TweetDetailActivity::class.java)
                startActivity(intentToTopicDetailActivity)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                startActivity(Intent(this@TopicDetailActivity, MainActivity::class.java))
            }
            R.id.action_topic -> {
                Toast.makeText(applicationContext,"Coming Soon", Toast.LENGTH_SHORT).show()
            }
            R.id.action_about -> {
                startActivity(Intent(this@TopicDetailActivity, AboutActivity::class.java))
            }
        }
        return true
    }
}