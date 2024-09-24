package com.example.baskaryaapp.ui.detailArticle

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.databinding.ActivityDetailArticleBinding
import com.google.firebase.auth.FirebaseAuth

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    private val firebaseHelper = FirebaseHelper()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var articleList: MutableList<ArticlesItem>
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener{back()}
        val articles = intent.getParcelableExtra<ArticlesItem>(EXTRA_ARTICLES)
        articles?.let {
            Glide.with(this)
                .load(it.imageUrl)
                .into(binding.imgItemPhoto)
            binding.tvItemTitle.text = it.title
            binding.tvItemSubTitle.text = it.author
            binding.tvItemDescription.text = it.content
        }

        val id = intent.getStringExtra("key_id").toString()
        val title = intent.getStringExtra("key_title").toString()
        val imageUrl = intent.getStringExtra("key_imageUrl").toString()
        articleList = mutableListOf()

        var bookmark = false
        if (articles?.isBookmarked == true) {
            binding.icBookmark.setImageResource(R.drawable.ic_bookmarked)
            bookmark = true
        } else {
            binding.icBookmark.setImageResource(R.drawable.ic_unbookmarked)
            bookmark = false
        }

        binding.icBookmark.setOnClickListener {
            bookmark = !bookmark
            if (bookmark) {
                binding.icBookmark.setImageResource(R.drawable.ic_bookmarked)
                firebaseHelper.addBookmarkArticle(id, title, imageUrl, articleList)
            } else {
                binding.icBookmark.setImageResource(R.drawable.ic_unbookmarked)
                firebaseHelper.removeBookmarkArticle(id, title, imageUrl, articleList)
            }
        }

        Log.d("DetailArticleActivity", "ID: $id")
        Log.d("DetailArticleActivity", "title: $title")
        Log.d("DetailArticleActivity", "imageurl: $imageUrl")
    }
    private fun back(){
        super.onBackPressed()
    }
    companion object {
        const val EXTRA_ARTICLES = "key_articles"
    }
}