package com.example.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var txtCounter:TextView
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel = ViewModelProvider(this,MainViewModelFactory(10)).get(MainViewModel::class.java)
        txtCounter = findViewById(R.id.txtCounter)
        setText()

    }


    private fun setText() {
        txtCounter.text = mainViewModel.count.toString()
    }
    fun increment(v:View){
        mainViewModel.increment();
        setText();
    }
}