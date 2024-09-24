package com.example.baskaryaapp.ui.bookmark.customization

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baskaryaapp.data.database.BookmarkCustom
import com.example.baskaryaapp.databinding.ItemListCustomizationBinding
import com.example.baskaryaapp.ui.detailCustomization.DetailCustomizationActivity

//class CustomizationAdapter : ListAdapter<Data, CustomizationAdapter.ListViewHolder>(DIFF_CALLBACK){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = ItemListCustomizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val custom = getItem(position)
//        holder.bind(custom)
//    }
//
//    class ListViewHolder(private val binding: ItemListCustomizationBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(custom: Data){
//            Glide.with(binding.root.context)
//                .load(custom.imageUrl)
//                .into(binding.imgItemPhoto)
//
////            binding.root.setOnClickListener{
////                val intentDetail = Intent(binding.root.context, DetailArticleActivity::class.java)
////                intentDetail.putExtra(EXTRA_ID, custom.id)
////                intentDetail.putExtra(EXTRA_CUSTOM, custom)
////                intentDetail.putExtra("key_id", custom.id)
////                intentDetail.putExtra("NAMA_BATIK", custom.name)
////                intentDetail.putExtra("IMAGE_URL", custom.imageUrl)
////
////                val optionsCompat: ActivityOptionsCompat =
////                    ActivityOptionsCompat.makeSceneTransitionAnimation(
////                        itemView.context as Activity,
////                        Pair(binding.imgItemPhoto, "image"),
////                    )
////
////                itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
////            }
//        }
//    }
//
//    companion object {
//
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Data>(){
//            override fun areItemsTheSame(
//                oldItem: Data,
//                newItem: Data
//            ): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(
//                oldItem: Data,
//                newItem: Data
//            ): Boolean {
//                return oldItem == newItem
//            }
//        }
//
//        const val EXTRA_ID = "key_id"
//        const val EXTRA_CUSTOM = "key_custom"
//    }
//}

class CustomizationAdapter : ListAdapter<BookmarkCustom, CustomizationAdapter.BookmarkCustomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkCustomViewHolder {
        val binding = ItemListCustomizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkCustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkCustomViewHolder, position: Int) {
        val bookmarkCustom = getItem(position)
        holder.bind(bookmarkCustom)
    }

    class BookmarkCustomViewHolder(private val binding: ItemListCustomizationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmarkCustom: BookmarkCustom) {
            Glide.with(binding.root.context)
                .load(bookmarkCustom.imageUrl)
                .into(binding.imgItemPhoto)

            binding.root.setOnClickListener{
                val intentDetail = Intent(binding.root.context, DetailCustomizationActivity::class.java)
                intentDetail.putExtra("key_id", bookmarkCustom.userId)
                intentDetail.putExtra("NAMA_BATIK", bookmarkCustom.name)
                intentDetail.putExtra("IMAGE_URL", bookmarkCustom.imageUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgItemPhoto, "image"),
                    )

                itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarkCustom>(){
            override fun areItemsTheSame(
                oldItem: BookmarkCustom,
                newItem: BookmarkCustom
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: BookmarkCustom,
                newItem: BookmarkCustom
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
