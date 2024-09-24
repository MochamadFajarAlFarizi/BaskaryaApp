package com.example.baskaryaapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.repo.BatikRepository
import com.example.baskaryaapp.data.response.BatikItem
import com.example.baskaryaapp.data.response.BatikResponse
import com.example.baskaryaapp.databinding.FragmentSearchBatikBinding
import com.example.baskaryaapp.ui.BatikViewModelFactory
import com.example.baskaryaapp.ui.batikpedia.BatikRVAdapter
import com.example.baskaryaapp.ui.batikpedia.BatikpediaViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchBatikFragment : Fragment() {
    private lateinit var binding: FragmentSearchBatikBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBatikBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = BatikRepository.getInstance(ApiConfig.apiService)
        val factory = BatikViewModelFactory.getInstance(repository)
        val batikpediaViewModel = ViewModelProvider(this, factory)[BatikpediaViewModel::class.java]

        val layoutManager = GridLayoutManager(requireContext(), 3)

        binding.idRVBatik.layoutManager = layoutManager

        batikpediaViewModel.listBatik.observe(requireActivity()) { listBatik ->
            setBatikData(listBatik)
        }

        batikpediaViewModel.isLoading.observe(requireActivity()) { loading ->
            showLoading(loading)
        }
        val searchView =view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchBatik(query)
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

    private fun searchBatik(query: String) {
        val call = ApiConfig.apiService.searchbatik(query)
        showLoading(true)
        call.enqueue(object : Callback<BatikResponse> {
            override fun onResponse(call: Call<BatikResponse>, response: Response<BatikResponse>) {
                if (response.isSuccessful) {
                    val batikResponse = response.body()
                    batikResponse?.let {
                        if (it.data.isNullOrEmpty()) {
                            showLoading(false)
                            Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        } else {
                            setBatikData(it.data)
                            showLoading(false)
                        }
                    }
                } else {
                    showLoading(false)
                    val errorMessage = "Error: Data tidak ditemukan"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BatikResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(context, "Terjadi kesalahan: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setBatikData(items: List<BatikItem>) {
        val adapter = BatikRVAdapter()
        adapter.submitList(items)
        binding.idRVBatik.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}