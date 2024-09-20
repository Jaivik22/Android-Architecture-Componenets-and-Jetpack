package com.example.loginmvvm.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.R
import com.example.loginmvvm.Repositoy.GoogleRepository
import com.example.loginmvvm.Repositoy.SharedPreRepository
import com.example.loginmvvm.Repositoy.RoomRepository
import com.example.loginmvvm.databinding.ActivityRegisterPageBinding
import com.example.loginmvvm.db.UserDetailsDB
import com.example.loginmvvm.db.UserSharedPrefrences
import com.example.loginmvvm.model.UserDetailsDbModel
import com.example.loginmvvm.utils.GoogleSignInClient
import com.example.loginmvvm.viewModels.GoogleSignInViewModel
import com.example.loginmvvm.viewModels.GoogleSignInViewModelFactory
import com.example.loginmvvm.viewModels.RoomMainViewModel
import com.example.loginmvvm.viewModels.RoomMainViewModelFactory
import com.example.loginmvvm.viewModels.SharedPrefViewModel
import com.example.loginmvvm.viewModels.SharedPrefViewModelFactory

class RegisterPage : AppCompatActivity() {
    lateinit var binding:ActivityRegisterPageBinding
    lateinit var roomMainViewModel: RoomMainViewModel
    lateinit var googleSignInViewModel: GoogleSignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_page)
        val dao = UserDetailsDB.getDatabase(applicationContext).userDao()
        val repository = RoomRepository(dao)
        roomMainViewModel = ViewModelProvider(this,RoomMainViewModelFactory(repository)).get(RoomMainViewModel::class.java)

        val googleSignInClient = GoogleSignInClient(this)
        val googleRepository= GoogleRepository(googleSignInClient)
        googleSignInViewModel = ViewModelProvider(this,GoogleSignInViewModelFactory(googleRepository)).get(GoogleSignInViewModel::class.java)

        val userSharedPrefrences = UserSharedPrefrences(this)
        val sharedPreRepository = SharedPreRepository(userSharedPrefrences)
        val sharedPrefModel = ViewModelProvider(this,
            SharedPrefViewModelFactory(sharedPreRepository)
        ).get(SharedPrefViewModel::class.java)


        binding.registerSubmitBtn.setOnClickListener {
            val name:String = binding.etNameRegister.text.toString().trim()
            val email:String = binding.etEmailRegister.text.toString().trim()
            val password:String = binding.etPasswordRegister.text.toString().trim()

            val userDetails = UserDetailsDbModel(0,name,email,password,false);

            roomMainViewModel.addUser(userDetails){
                val i = Intent(this,LoginPage::class.java)
                startActivity(i)
            }
        }
        binding.loginPageTxt.setOnClickListener {
            val i = Intent(this,LoginPage::class.java)
            startActivity(i)
        }

        binding.gSignInBtn.setOnClickListener {
            googleSignInViewModel.googleSignIn { result ->
                if (result.googlesignIn) {

                    sharedPrefModel.putUserName(result.Name)
                    sharedPrefModel.addGoogleUserStatus(result.googlesignIn)

                    val value = UserDetailsDbModel(0,result.Name,result.emailId,result.password,result.googlesignIn);
                    roomMainViewModel.addUser(value){
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                    }


                }
            }
        }

    }
}