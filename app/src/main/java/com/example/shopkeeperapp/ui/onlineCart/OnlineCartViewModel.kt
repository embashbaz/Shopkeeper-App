package com.example.shopkeeperapp.ui.onlineCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopProduct

class OnlineCartViewModel: ViewModel() {
    val repository = Repository()

    private var _updatingOrderOutput = MutableLiveData<HashMap<String, String>>()
    val updatingOrderOutput: LiveData<HashMap<String, String>>
        get() = _updatingOrderOutput

    fun updateOrder(order: Order){
        _updatingOrderOutput = repository.updateOrder(order)
    }

}