package com.example.baskaryaapp.ui.customization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.response.Data
import com.example.baskaryaapp.databinding.ActivityCustomizationBinding
import com.example.baskaryaapp.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class CustomizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomizationBinding
    private val firebaseHelper = FirebaseHelper()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var customList: MutableList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.setOnClickListener{back()}
        val id = intent.getStringExtra("key_id").toString()
        val namaBatik = intent.getStringExtra("NAMA_BATIK").toString()
        val imageUrl = intent.getStringExtra("IMAGE_URL").toString()
        val custom = intent.getParcelableExtra<Data>("key_custom")

        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivCustom)
        binding.tvPattern.text = "$namaBatik Pattern"

        customList = mutableListOf()

        var bookmark = false

        if (custom?.isBookmarked == true) {
            bookmark = true
        } else {
            bookmark = false
        }

        binding.btnSave.setOnClickListener {
            bookmark = !bookmark
            if (bookmark) {
                firebaseHelper.addBookmarkCustom(id, namaBatik, imageUrl, customList)
                showToastAndNavigate(getString(R.string.bookmark_added))
            } else {
                firebaseHelper.removeBookmarkCustom(id, namaBatik, imageUrl, customList)
                showToastAndNavigate(getString(R.string.bookmark_removed))
            }
        }

        Log.d("CustomizationActivity", "ID: $id")
        Log.d("CustomizationActivity", "title: $namaBatik")
        Log.d("CustomizationActivity", "imageurl: $imageUrl")
    }

    private fun showToastAndNavigate(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun back(){
        super.onBackPressed()
    }
}