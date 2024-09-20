package com.example.loginmvvm.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.R
import com.example.loginmvvm.Repositoy.SharedPreRepository
import com.example.loginmvvm.Repositoy.RoomRepository
import com.example.loginmvvm.databinding.ActivityLoginPageBinding
import com.example.loginmvvm.db.UserDetailsDB
import com.example.loginmvvm.db.UserSharedPrefrences
import com.example.loginmvvm.viewModels.RoomMainViewModel
import com.example.loginmvvm.viewModels.RoomMainViewModelFactory
import com.example.loginmvvm.viewModels.SharedPrefViewModel
import com.example.loginmvvm.viewModels.SharedPrefViewModelFactory

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    lateinit var roomMainViewModel: RoomMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_page)

        val dao = UserDetailsDB.getDatabase(applicationContext).userDao()
        val repository = RoomRepository(dao)
        roomMainViewModel = ViewModelProvider(this, RoomMainViewModelFactory(repository)).get(RoomMainViewModel::class.java)

        val userSharedPrefrences = UserSharedPrefrences(this)
        val sharedPreRepository = SharedPreRepository(userSharedPrefrences)
        val sharedPrefModel = ViewModelProvider(this,
            SharedPrefViewModelFactory(sharedPreRepository)
        ).get(SharedPrefViewModel::class.java)


        binding.registerPageTxt.setOnClickListener {
            val i  = Intent(this,RegisterPage::class.java)
            startActivity(i)
        }

        binding.loginSubmitBtn.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            roomMainViewModel.verifyUser(email,password){ userDetails ->
                Log.d("userDetails", userDetails!!.Name.toString())

                if(userDetails!=null){
                    sharedPrefModel.putUserName(email)
                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                }
            }
        }
    }
}