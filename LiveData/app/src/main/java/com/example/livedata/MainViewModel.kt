package com.example.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val factLiveDataObjet = MutableLiveData<String>("It's fact")

    val mutablefact : LiveData<String>
        get() = factLiveDataObjet

    fun updateData(){
        factLiveDataObjet.value = "New fact"
    }
}