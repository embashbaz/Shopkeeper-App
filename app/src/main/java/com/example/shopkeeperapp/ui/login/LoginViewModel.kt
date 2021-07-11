package com.example.shopkeeperapp.ui.login

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopKeeper

class LoginViewModel: ViewModel(){

    val repository = Repository()

    private var _loginOutput = MutableLiveData<HashMap<String, String>>()
    val loginOutput: LiveData<HashMap<String, String>>
        get() = _loginOutput

    fun signUp (email: String, password: String){
        _loginOutput = repository.login(email, password)
    }


}