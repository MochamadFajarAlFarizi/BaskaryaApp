package com.example.baskaryaapp.ui.batikpedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baskaryaapp.R
import com.example.baskaryaapp.data.api.ApiConfig.apiService
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.repo.BatikRepository
import com.example.baskaryaapp.data.response.BatikItem
import com.example.baskaryaapp.databinding.FragmentBatikpediaBinding
import com.example.baskaryaapp.ui.BatikViewModelFactory
import com.example.baskaryaapp.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BatikpediaFragment : Fragment() {
    private lateinit var binding: FragmentBatikpediaBinding
    private val firebaseHelper = FirebaseHelper()
    private lateinit var batikList: MutableList<BatikItem>
    lateinit var imageView2 : ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBatikpediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = BatikRepository.getInstance(apiService)
        val factory = BatikViewModelFactory.getInstance(repository)
        val batikpediaViewModel = ViewModelProvider(this, factory)[BatikpediaViewModel::class.java]

        val layoutManager = GridLayoutManager(requireContext(), 3)

        binding.idRVBatik.layoutManager = layoutManager
        imageView2= view.findViewById(R.id.imageView2)

        imageView2.setOnClickListener{
            val homeFragment = HomeFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.navhost, homeFragment)
            fragmentTransaction.commit()
        }

        batikList = mutableListOf()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            firebaseHelper.getBookmarkedBatiks(userId) { bookmarkedIds ->
                batikpediaViewModel.listBatik.observe(requireActivity()) { listBatik ->
                    setBatikData(listBatik, bookmarkedIds)
                }
            }
        }

        lifecycleScope.launch {
            while (lifecycleScope.coroutineContext.isActive) {
                delay(10000)
                if (isAdded() && userId != null) {
                    firebaseHelper.getBookmarkedBatiks(userId) { bookmarkedIds ->
                        if (isAdded()) {
                            batikpediaViewModel.listBatik.observe(requireActivity()) { listBatik ->
                                setBatikData(listBatik, bookmarkedIds)
                            }
                        }
                    }
                }
            }
        }

        batikpediaViewModel.isLoading.observe(requireActivity()) { loading ->
            showLoading(loading)
        }
    }


    private fun setBatikData(items: List<BatikItem>, bookmarkedIds: List<String?>) {
        val layoutManager = binding.idRVBatik.layoutManager as? LinearLayoutManager
        val lastFirstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()
        val topOffset = if (lastFirstVisiblePosition != RecyclerView.NO_POSITION) {
            val firstView = binding.idRVBatik.getChildAt(0)
            firstView?.top ?: 0
        } else {
            0
        }

        val adapter = BatikRVAdapter()
        val itemsWithBookmarkStatus = items.map { batik ->
            batik.copy(isBookmarked = bookmarkedIds.contains(batik.id))
        }
        adapter.submitList(itemsWithBookmarkStatus)

        val recyclerViewState = layoutManager?.onSaveInstanceState()

        binding.idRVBatik.adapter = adapter

        layoutManager?.onRestoreInstanceState(recyclerViewState)
        layoutManager?.scrollToPositionWithOffset(lastFirstVisiblePosition ?: 0, topOffset)

        showLoading(false)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}