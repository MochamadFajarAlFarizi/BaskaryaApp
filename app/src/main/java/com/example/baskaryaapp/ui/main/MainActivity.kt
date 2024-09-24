package com.example.baskaryaapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.baskaryaapp.R
import com.example.baskaryaapp.databinding.ActivityMainBinding
import com.example.baskaryaapp.ui.articles.ArticlesFragment
import com.example.baskaryaapp.ui.bookmark.BookmarkFragment
import com.example.baskaryaapp.ui.home.HomeFragment
import com.example.baskaryaapp.ui.scan.ScanFragment
import com.example.baskaryaapp.ui.setting.Prefference
import com.example.baskaryaapp.ui.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val pref by lazy { Prefference(this) }

    private lateinit var navview: BottomNavigationView
    val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (pref.getBoolean("dark_mode")){
            true ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
            false ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }

        }

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        binding.fab.setOnClickListener{
            val scanFragment = ScanFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, scanFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }


        navview =findViewById(R.id.bottomNavigationView)
        replace(HomeFragment())
        navview.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home->replace(HomeFragment())
                R.id.article->replace(ArticlesFragment())
                R.id.bookmark->replace(BookmarkFragment())
                R.id.Settings->replace(SettingFragment())
            }
            true
        }


    }
    private fun replace (fragment : Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction =fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navhost,fragment)
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            val imageBitmap=data?.extras?.get("data")as Bitmap
        }else{super.onActivityResult(requestCode, resultCode, data)

        }
    }
}