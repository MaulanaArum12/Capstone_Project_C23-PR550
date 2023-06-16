package com.maulana.netweezen

import com.google.gson.annotations.SerializedName

data class TweetResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("tweets")
	val tweets: List<TweetsItem?>? = null
)

data class TweetsItem(

	@field:SerializedName("like")
	val like: Int? = null,

	@field:SerializedName("topic")
	val topic: String? = null,

	@field:SerializedName("_id")
	val id: Int? = null,

	@field:SerializedName("clean")
	val clean: String? = null,

	@field:SerializedName("tweet")
	val tweet: String? = null,

	@field:SerializedName("reply")
	val reply: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("asp_score")
	val aspScore: Int? = null
)
