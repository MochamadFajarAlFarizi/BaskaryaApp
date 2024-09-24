package com.example.baskaryaapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.api.ApiConfig.apiService
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.repo.ArticlesRepository
import com.example.baskaryaapp.data.repo.BatikRepository
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.data.response.BatikItem
import com.example.baskaryaapp.databinding.FragmentHomeBinding
import com.example.baskaryaapp.ui.ArticlesViewModelFactory
import com.example.baskaryaapp.ui.BatikViewModelFactory
import com.example.baskaryaapp.ui.articles.ArticlesFragment
import com.example.baskaryaapp.ui.articles.ArticlesViewModel
import com.example.baskaryaapp.ui.batikpedia.BatikpediaFragment
import com.example.baskaryaapp.ui.batikpedia.BatikpediaViewModel
import com.example.baskaryaapp.ui.search.SearchResultFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
      lateinit var vpSlider: ViewPager
    lateinit var morebp:TextView
    lateinit var morear: TextView
    lateinit var search_btn :ImageView
    private val firebaseHelper = FirebaseHelper()
    private lateinit var batikList: MutableList<BatikItem>
    private lateinit var articleList: MutableList<ArticlesItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        val delay = 3000L
        vpSlider=view.findViewById(R.id.view_pager)
        val arrSlider= ArrayList<Int>()
        arrSlider.add(R.drawable.baskarya_logo)
        arrSlider.add(R.drawable.banner_baskarya2)
        arrSlider.add(R.drawable.banner_baskarya3)

        var adapterSlider=AdapterSlider(arrSlider,activity)
        vpSlider.adapter=adapterSlider

        val runnable = object : Runnable {
            override fun run() {
                val currentItem = vpSlider.currentItem
                val totalCount = adapterSlider.count

                if (currentItem < totalCount - 1) {
                    vpSlider.currentItem = currentItem + 1
                } else {
                    vpSlider.currentItem = 0
                }

                handler.postDelayed(this, delay)
            }
        }

        handler.postDelayed(runnable, delay)


        val repository = BatikRepository.getInstance(apiService)
        val factory = BatikViewModelFactory.getInstance(repository)
        val batikpediaViewModel = ViewModelProvider(this, factory)[BatikpediaViewModel::class.java]
        val articlerepository = ArticlesRepository.getInstance(ApiConfig.apiService)
        val articlefactory = ArticlesViewModelFactory.getInstance(articlerepository)
        val articlesViewModel = ViewModelProvider(this, articlefactory)[ArticlesViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val arlayoutManager = LinearLayoutManager(requireActivity())
        binding.RVArticle.layoutManager = arlayoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), arlayoutManager.orientation)
        binding.RVArticle.addItemDecoration(itemDecoration)


        binding.idRVCourses.layoutManager = layoutManager

        batikList = mutableListOf()
        articleList = mutableListOf()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        lifecycleScope.launch {
            showLoading(true)
            while (lifecycleScope.coroutineContext.isActive) {
                delay(5000)
                if (isAdded() && userId != null) {
                    firebaseHelper.getBookmarkedBatiks(userId) { bookmarkedIds ->
                        if (isAdded()) {
                            batikpediaViewModel.listBatik.observe(requireActivity()) { listBatik ->
                                setBatikData(listBatik, bookmarkedIds)
                            }
                            showLoading(false)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            while (lifecycleScope.coroutineContext.isActive) {
                showLoading(true)
                delay(5000)
                if (isAdded() && userId != null) {
                    firebaseHelper.getBookmarkedArticles(userId) { bookmarkedIds ->
                        if (isAdded()) {
                            articlesViewModel.listArticles.observe(requireActivity()) { listArticles ->
                                setArticlesData(listArticles, bookmarkedIds)
                            }
                            showLoading(false)
                        }
                    }
                }
            }
        }


        morear =view.findViewById(R.id.tv_moreTa)
        morebp =view.findViewById(R.id.tv_morebp)
        search_btn=view.findViewById(R.id.iv_search)


        search_btn.setOnClickListener{
            val searchfragment = SearchResultFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, searchfragment)
            fragmentTransaction.commit()
        }
        morebp.setOnClickListener {
            val batikpediaFragment = BatikpediaFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, batikpediaFragment)
            fragmentTransaction.commit()
        }
        morear.setOnClickListener {
            val articleFragment = ArticlesFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, articleFragment)
            fragmentTransaction.commit()
        }
    }

    private fun setBatikData(items: List<BatikItem>, bookmarkedIds: List<String?>) {
        val adapter = HomeAdapter(3)
        val itemsWithBookmarkStatus = items.map { batik ->
            batik.copy(isBookmarked = bookmarkedIds.contains(batik.id))
        }
        adapter.submitList(itemsWithBookmarkStatus)
        binding.idRVCourses.adapter = adapter
    }

    private fun setArticlesData(items: List<ArticlesItem>, bookmarkedIds: List<String?>) {
        val adapter = AdapterArticle(1)
        val itemsWithBookmarkStatus = items.map { batik ->
            batik.copy(isBookmarked = bookmarkedIds.contains(batik.id))
        }
        adapter.submitList(itemsWithBookmarkStatus)
        binding.RVArticle.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}