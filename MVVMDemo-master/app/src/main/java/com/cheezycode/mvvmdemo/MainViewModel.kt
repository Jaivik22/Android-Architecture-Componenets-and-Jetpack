package com.cheezycode.mvvmdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {

    fun getQuotes() : LiveData<List<Quote>>{
        return quoteRepository.getQuotes()
    }
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    fun insertQuote(quote: Quote){
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler){
            quoteRepository.insertQuote(quote)
        }

    }
}