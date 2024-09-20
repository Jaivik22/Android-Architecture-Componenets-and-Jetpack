package com.example.lifecycle_aware_compo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(Observer())
        Log.d("Main","Activity OnCreate")



    }

    override fun onStart() {
        super.onStart()
        Log.d("Main","Activity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Main","Activity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Main","Activity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Main","Activity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Main","Activity onDestory")
    }
}