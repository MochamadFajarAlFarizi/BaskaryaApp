package com.example.baskaryaapp.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.repo.ArticlesRepository
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.databinding.FragmentArticlesBinding
import com.example.baskaryaapp.ui.ArticlesViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private val firebaseHelper = FirebaseHelper()
    private lateinit var articleList: MutableList<ArticlesItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ArticlesRepository.getInstance(ApiConfig.apiService)
        val factory = ArticlesViewModelFactory.getInstance(repository)
        val articlesViewModel = ViewModelProvider(this, factory)[ArticlesViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvArticles.addItemDecoration(itemDecoration)

        articleList = mutableListOf()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid


        lifecycleScope.launch {
            while (lifecycleScope.coroutineContext.isActive) {
                showLoading(true)
                delay(5000)
                if (isAdded() && userId != null) {
                    // Dapatkan data bookmark
                    firebaseHelper.getBookmarkedArticles(userId) { bookmarkedIds ->
                        if (isAdded()) {
                            // Set data article
                            articlesViewModel.listArticles.observe(requireActivity()) { listArticles ->
                                setArticlesData(listArticles, bookmarkedIds)
                            }

                            showLoading(false)
                        }
                    }
                }
            }
        }


    }


    private fun setArticlesData(items: List<ArticlesItem>, bookmarkedIds: List<String?>) {
        val layoutManager = binding.rvArticles.layoutManager as? LinearLayoutManager
        val lastFirstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()
        val topOffset = if (lastFirstVisiblePosition != RecyclerView.NO_POSITION) {
            val firstView = binding.rvArticles.getChildAt(0)
            firstView?.top ?: 0
        } else {
            0
        }

        val adapter = ArticlesAdapter()
        val itemsWithBookmarkStatus = items.map { article ->
            article.copy(isBookmarked = bookmarkedIds.contains(article.id))
        }
        adapter.submitList(itemsWithBookmarkStatus)

        val recyclerViewState = layoutManager?.onSaveInstanceState()

        binding.rvArticles.adapter = adapter

        layoutManager?.onRestoreInstanceState(recyclerViewState)
        layoutManager?.scrollToPositionWithOffset(lastFirstVisiblePosition ?: 0, topOffset)

        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}