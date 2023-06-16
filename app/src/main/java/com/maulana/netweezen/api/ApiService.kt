package com.maulana.netweezen.api

import com.maulana.netweezen.TweetResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("tweets/{topic}")
    fun getTweet(
        @Path("topic") topic: String
    ): Call<TweetResponse>
}