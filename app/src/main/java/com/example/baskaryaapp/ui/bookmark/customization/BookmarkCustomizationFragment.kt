package com.example.baskaryaapp.ui.bookmark.customization

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baskaryaapp.data.database.BookmarkCustom
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.response.Data
import com.example.baskaryaapp.databinding.FragmentBookmarkCustomizationBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BookmarkCustomizationFragment : Fragment(){

private lateinit var binding: FragmentBookmarkCustomizationBinding
    private val firebaseHelper = FirebaseHelper()
    private lateinit var customList: MutableList<Data>
    private lateinit var bookmarkedCustomizations: MutableList<BookmarkCustom>
    private var recyclerViewState: Parcelable? = null

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    binding = FragmentBookmarkCustomizationBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val layoutManager = LinearLayoutManager(requireActivity())
    binding.rvBookmarkCustom.layoutManager = layoutManager
    val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
    binding.rvBookmarkCustom.addItemDecoration(itemDecoration)

    customList = mutableListOf()

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid


    showLoading(true)

    lifecycleScope.launch {
        while (lifecycleScope.coroutineContext.isActive) {
            showLoading(true)
            delay(5000)
            if (isAdded() && userId != null) {
                if (isAdded()) {
                    userId?.let { uid ->
                        firebaseHelper.fetchCustomizations(uid) { customizations ->
                            bookmarkedCustomizations = customizations.toMutableList()
                            setCustomizationData(bookmarkedCustomizations)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
}

    private fun setCustomizationData(items: List<BookmarkCustom>) {
        val layoutManager = binding.rvBookmarkCustom.layoutManager
        recyclerViewState = layoutManager?.onSaveInstanceState()

        val adapter = CustomizationAdapter()
        adapter.submitList(items)
        binding.rvBookmarkCustom.adapter = adapter

        layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}