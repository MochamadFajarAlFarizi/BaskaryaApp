package com.example.baskaryaapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BatikResponse(

	@field:SerializedName("data")
	val data: List<BatikItem> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class BatikItem(

	@field:SerializedName("origin")
	val origin: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	var isBookmarked: Boolean = false
):Parcelable
