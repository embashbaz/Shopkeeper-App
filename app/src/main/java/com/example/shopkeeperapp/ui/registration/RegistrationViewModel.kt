package com.example.shopkeeperapp.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopKeeper
import com.google.android.gms.maps.model.LatLng

class RegistrationViewModel : ViewModel() {

    val repository = Repository()


    private var _registrationOutput = MutableLiveData<HashMap<String, String>>()
    val registrationOutput: LiveData<HashMap<String, String>>
        get() = _registrationOutput

    fun getUserData(uId: String): MutableLiveData<ShopKeeper?>{
       return repository.getShop(uId)
    }



    fun signUp (mShopKeeper: ShopKeeper, password: String){
        _registrationOutput = repository.register(mShopKeeper, password)
    }


}