package com.example.loginmvvm.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.R
import com.example.loginmvvm.Repositoy.GoogleRepository
import com.example.loginmvvm.Repositoy.SharedPreRepository
import com.example.loginmvvm.Repositoy.RoomRepository
import com.example.loginmvvm.databinding.ActivityMainBinding
import com.example.loginmvvm.db.UserDetailsDB
import com.example.loginmvvm.db.UserSharedPrefrences
import com.example.loginmvvm.utils.GoogleSignInClient
import com.example.loginmvvm.viewModels.GoogleSignInViewModel
import com.example.loginmvvm.viewModels.GoogleSignInViewModelFactory
import com.example.loginmvvm.viewModels.RoomMainViewModel
import com.example.loginmvvm.viewModels.RoomMainViewModelFactory
import com.example.loginmvvm.viewModels.SharedPrefViewModel
import com.example.loginmvvm.viewModels.SharedPrefViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var roomMainViewModel: RoomMainViewModel
    lateinit var googleSignInViewModel: GoogleSignInViewModel
    lateinit var username:String
    var isGoogleUser:Boolean = false


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val dao = UserDetailsDB.getDatabase(applicationContext).userDao()
        val repository =RoomRepository(dao)
        roomMainViewModel = ViewModelProvider(this,RoomMainViewModelFactory(repository)).get(RoomMainViewModel::class.java)

        val googleSignInClient = GoogleSignInClient(this)
        val googleRepository= GoogleRepository(googleSignInClient)
        googleSignInViewModel = ViewModelProvider(this, GoogleSignInViewModelFactory(googleRepository)).get(GoogleSignInViewModel::class.java)

        val userSharedPrefrences = UserSharedPrefrences(this)
        val sharedPreRepository = SharedPreRepository(userSharedPrefrences)
        val sharedPrefModel = ViewModelProvider(this,SharedPrefViewModelFactory(sharedPreRepository)).get(SharedPrefViewModel::class.java)


        roomMainViewModel.isDbEmpty { count->
            if(count==0){
                val i = Intent(this,RegisterPage::class.java)
                startActivity(i)
            }
        }

        sharedPrefModel.getUserName { result->
            username = result
        }
        sharedPrefModel.isGoogleUser { result->
            isGoogleUser = result
        }


        binding.txtHello.text = "Welcome to the app: " +username

        binding.logoutBtn.setOnClickListener {
            roomMainViewModel.deleteUser(username){
                sharedPrefModel.clearSharedPreferences()
                Toast.makeText(this, "Loggedout", Toast.LENGTH_SHORT).show()

                if(isGoogleUser){
                    googleSignInViewModel.signOut { }
                }
                val i = Intent(this,LoginPage::class.java)
                startActivity(i)
            }
        }


    }


}