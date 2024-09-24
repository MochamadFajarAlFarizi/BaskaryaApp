package com.example.baskaryaapp.ui.batikpedia

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baskaryaapp.data.repo.BatikRepository
import com.example.baskaryaapp.data.response.BatikItem
import kotlinx.coroutines.launch

class BatikpediaViewModel(private val repository: BatikRepository) : ViewModel() {
    private val _listBatik = MutableLiveData<List<BatikItem>>()
    val listBatik: LiveData<List<BatikItem>> = _listBatik

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getBatik()
    }

    private fun getBatik() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.batik()
                if (!response.error!!) {
                    _listBatik.value = response.data
                } else {
                    Log.e("BatikViewModel", "Error load stories: ${response.status}")
                }
            } catch (e: Exception) {
                Log.e("BatikViewModel", "Error load stories: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}