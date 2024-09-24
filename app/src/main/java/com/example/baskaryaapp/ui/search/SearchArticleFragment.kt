package com.example.baskaryaapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.repo.ArticlesRepository
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.data.response.ArticlesResponse
import com.example.baskaryaapp.databinding.FragmentSearchArticleBinding
import com.example.baskaryaapp.ui.ArticlesViewModelFactory
import com.example.baskaryaapp.ui.articles.ArticlesAdapter
import com.example.baskaryaapp.ui.articles.ArticlesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchArticleFragment : Fragment() {

    private lateinit var binding: FragmentSearchArticleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchArticleBinding.inflate(inflater, container, false)
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

        articlesViewModel.listArticles.observe(requireActivity()) { listArticles ->
            setArticlesData(listArticles)
        }

        articlesViewModel.isLoading.observe(requireActivity()) { loading ->
            showLoading(loading)
        }
        val searchView =view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchArticle(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchResultFragment = SearchResultFragment()

                if (!newText.isNullOrEmpty()) {
                    val bundle = Bundle()
                    bundle.putString("query", newText)
                    searchResultFragment.arguments = bundle
                }

                return true
            }
        })
    }

    private fun searchArticle(query:String)  {
        val call = ApiConfig.apiService.searcharticle(query)
        showLoading(true)
        call.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(call: Call<ArticlesResponse>, response: Response<ArticlesResponse>) {
                if (response.isSuccessful) {
                    val batikResponse = response.body()
                    batikResponse?.let {
                        if (it.data.isNullOrEmpty()) {
                            showLoading(false)
                            Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        } else {
                            setArticlesData(it.data)
                            showLoading(false)
                        }
                    }
                } else {
                    showLoading(false)
                    val errorMessage = "Error: Data tidak ditemukan"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(context, "Terjadi kesalahan: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setArticlesData(items: List<ArticlesItem>) {
        val adapter = ArticlesAdapter()
        adapter.submitList(items)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}