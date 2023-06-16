package com.maulana.netweezen.tweet

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maulana.netweezen.R
import com.maulana.netweezen.TweetResponse
import com.maulana.netweezen.TweetsItem
import com.maulana.netweezen.about.AboutActivity
import com.maulana.netweezen.api.ApiConfig
import com.maulana.netweezen.databinding.ActivityTweetDetailBinding
import com.maulana.netweezen.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST", "DEPRECATION")
class TweetDetailActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityTweetDetailBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTweetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val review = intent.getStringExtra("review")
        val clean = intent.getStringExtra("clean")
        val username = intent.getStringExtra("username")
        val score = intent.getStringExtra("score")
        val like = intent.getStringExtra("like")
        val comment = intent.getStringExtra("comment")

        binding.contentTweetUser.text = review
        binding.contentTweetAspiration.text = clean
        binding.usernameTitle.text = username
        binding.btnScore.text = score +" %"
        binding.btnScoreLike.text = like
        binding.btnScoreReply.text = comment
        binding.navView.setOnNavigationItemSelectedListener(this)
        binding.navView.itemIconTintList = null
        binding.navView.menu.getItem(0).isChecked = false
        binding.replyTweetButton.setOnClickListener {Toast.makeText(applicationContext,"Coming Soon", Toast.LENGTH_SHORT).show()}

        supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.custom_actionbar_tweetdetail)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue_grey)))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Log.d("NETWEEZEN", "onCreate: " + review)

        findDataTweet()
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
                        setTweetData(responseBody.tweets as List<TweetsItem>)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TweetResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
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
                startActivity(Intent(this@TweetDetailActivity, MainActivity::class.java))
            }
            R.id.action_topic -> {
                Toast.makeText(applicationContext,"Coming Soon", Toast.LENGTH_SHORT).show()
            }
            R.id.action_about -> {
                startActivity(Intent(this@TweetDetailActivity, AboutActivity::class.java))
            }
        }
        return true
    }
}