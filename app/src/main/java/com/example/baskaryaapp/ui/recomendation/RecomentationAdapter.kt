package com.example.baskaryaapp.ui.recomendation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.response.GenerateResponse
import com.example.baskaryaapp.data.response.SimilarImagesItem
import com.example.baskaryaapp.databinding.ItemListBatikNotextBinding
import com.example.baskaryaapp.ui.customization.CustomizationActivity
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecomentationAdapter : ListAdapter<SimilarImagesItem, RecomentationAdapter.UploadResponseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadResponseViewHolder {
        val binding = ItemListBatikNotextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UploadResponseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UploadResponseViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class UploadResponseViewHolder(private val binding: ItemListBatikNotextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val progressBar = binding.progressBar
        fun bind(similarImage: SimilarImagesItem) {
            binding.apply {
                similarImage.url?.let { url ->
                    Glide.with(itemView)
                        .load(url)
                        .placeholder(R.drawable.baskarya_logo)
                        .into(idIVBatik)
                }
            }

            itemView.setOnClickListener {
                val context = itemView.context
                progressBar.visibility = View.VISIBLE
                postCustomBatik(context, similarImage)
            }
        }

        private fun postCustomBatik(context: Context, similarImage: SimilarImagesItem) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val user = firebaseAuth.currentUser
            val email = user?.email
            val uid = email ?: "default_email_if_not_available"
            val imageUrl = similarImage.url ?: ""
            val batikName = similarImage.namaBatik ?: ""

            val call = ApiConfig.apiService.customBatik(uid, imageUrl, batikName)
            call.enqueue(object : Callback<GenerateResponse> {
                override fun onResponse(call: Call<GenerateResponse>, response: Response<GenerateResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val generateResponse = response.body()
                        generateResponse?.let {
                            val intent = Intent(context, CustomizationActivity::class.java)

                            intent.putExtra("key_custom", it.data)
                            intent.putExtra("key_id", it.data?.id)
                            intent.putExtra("IMAGE_URL", it.data?.imageUrl)
                            intent.putExtra("NAMA_BATIK", it.data?.name)

                            context.startActivity(intent)
                            Log.d("RecomendationAdapter", "Response successful: $it")
                        }
                    } else {
                        Log.e("RecomendationAdapter", "Response unsuccessful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GenerateResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }
            })
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<SimilarImagesItem>() {
        override fun areItemsTheSame(oldItem: SimilarImagesItem, newItem: SimilarImagesItem): Boolean {
            return oldItem.namaBatik == newItem.namaBatik
        }

        override fun areContentsTheSame(oldItem: SimilarImagesItem, newItem: SimilarImagesItem): Boolean {
            return oldItem == newItem
        }
    }
}
