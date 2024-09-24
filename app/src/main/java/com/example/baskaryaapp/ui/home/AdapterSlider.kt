package com.example.baskaryaapp.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.example.baskaryaapp.R

class AdapterSlider(var dataSlider:ArrayList<Int>,var context : Activity?): PagerAdapter(){
    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return dataSlider.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    @NonNull
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slide,container,false)

        val imgViewSlider:ImageView
        imgViewSlider = view.findViewById(R.id.iv_slider)

        imgViewSlider.setImageResource(dataSlider[position])

        container.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}