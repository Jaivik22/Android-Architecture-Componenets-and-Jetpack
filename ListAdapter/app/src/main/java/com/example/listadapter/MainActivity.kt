package com.example.listadapter

import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.programmingList)
        val adapter = ProgrammingAdapter();

        val p1 = ProgrammingItem(1,"J","Jaivik")
        val p2 = ProgrammingItem(2,"V","Vatsal")
        val p3 = ProgrammingItem(3,"D","Deep")

        adapter.submitList(listOf(p1,p2,p3))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val p3 = ProgrammingItem(3,"D","Deep    ")
            val p4 = ProgrammingItem(4,"J","Jeel")
            val p5 = ProgrammingItem(5,"A","Anuj")

            adapter.submitList(listOf(p3,p4,p5))

        }, 4000)
    }
}