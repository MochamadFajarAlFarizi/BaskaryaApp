package com.example.baskaryaapp.ui.articles

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
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.databinding.ItemListArticlesBinding
import com.example.baskaryaapp.ui.detailArticle.DetailArticleActivity

class ArticlesAdapter : ListAdapter<ArticlesItem, ArticlesAdapter.ListViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val articles = getItem(position)
        holder.bind(articles)
    }

    class ListViewHolder(private val binding: ItemListArticlesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(articles: ArticlesItem){
            Glide.with(binding.root.context)
                .load(articles.imageUrl)
                .into(binding.imgItemPhoto)
            binding.tvItemTitle.text = articles.title

            binding.root.setOnClickListener{
                val intentDetail = Intent(binding.root.context, DetailArticleActivity::class.java)
                intentDetail.putExtra(EXTRA_ID, articles.id)
                intentDetail.putExtra(EXTRA_ARTICLES, articles)
                intentDetail.putExtra("key_id", articles.id)
                intentDetail.putExtra("key_title", articles.title)
                intentDetail.putExtra("key_imageUrl", articles.imageUrl)

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

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>(){
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val EXTRA_ID = "key_id"
        const val EXTRA_ARTICLES = "key_articles"
    }
}