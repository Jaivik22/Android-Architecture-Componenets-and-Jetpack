package com.example.livedata

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel:MainViewModel
    private val txtLive:TextView
        get() = findViewById(R.id.txtLive)
    private  val btnSubmit:Button
        get() = findViewById(R.id.updateBtn)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.mutablefact.observe(this,{
            txtLive.text = it
        })

        btnSubmit.setOnClickListener { mainViewModel.updateData() }
    }
}