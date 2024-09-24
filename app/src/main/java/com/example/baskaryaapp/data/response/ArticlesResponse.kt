package com.example.baskaryaapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticlesResponse(

	@field:SerializedName("data")
	val data: List<ArticlesItem> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class ArticlesItem(

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("id")
	val id: String? = null,


	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	var isBookmarked: Boolean = false
):Parcelable


