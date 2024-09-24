package com.example.baskaryaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.baskaryaapp.data.repo.ArticlesRepository
import com.example.baskaryaapp.ui.articles.ArticlesViewModel

class ArticlesViewModelFactory private constructor(private val articlesRepository: ArticlesRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ArticlesViewModelFactory? = null

        @JvmStatic
        fun getInstance(articlesRepository: ArticlesRepository): ArticlesViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ArticlesViewModelFactory::class.java) {
                    INSTANCE = ArticlesViewModelFactory(articlesRepository)
                }
            }
            return INSTANCE as ArticlesViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            return ArticlesViewModel(articlesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}