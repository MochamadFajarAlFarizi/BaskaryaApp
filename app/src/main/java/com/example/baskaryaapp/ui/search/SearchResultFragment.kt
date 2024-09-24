package com.example.baskaryaapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.baskaryaapp.R
import com.example.baskaryaapp.databinding.FragmentSearchResultBinding
import com.example.baskaryaapp.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator


class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    lateinit var imageView2 : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       imageView2= view.findViewById(R.id.imageView2)

        imageView2.setOnClickListener{
            val homeFragment = HomeFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, homeFragment)
            fragmentTransaction.commit()
        }
        val sectionsPagerAdapter = SearchSectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }



    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.articles,
            R.string.batik
        )
    }
}