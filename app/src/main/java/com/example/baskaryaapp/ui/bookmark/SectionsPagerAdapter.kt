package com.example.baskaryaapp.ui.bookmark

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baskaryaapp.ui.bookmark.articles.BookmarkArticlesFragment
import com.example.baskaryaapp.ui.bookmark.batikpedia.BookmarkBatikFragment
import com.example.baskaryaapp.ui.bookmark.customization.BookmarkCustomizationFragment

class SectionsPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = BookmarkArticlesFragment()
            1 -> fragment = BookmarkBatikFragment()
            2 -> fragment = BookmarkCustomizationFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}