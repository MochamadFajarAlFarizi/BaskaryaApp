package com.example.baskaryaapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(

	@field:SerializedName("similar_images")
	val similarImages: List<SimilarImagesItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class SimilarImagesItem(

	@field:SerializedName("Nama Batik")
	val namaBatik: String? = null,

	@field:SerializedName("Url")
	val url: String? = null
) : Parcelable
