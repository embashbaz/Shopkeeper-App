package com.example.shopkeeperapp.ui.ordersList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopProduct

class OrderListViewModel : ViewModel() {

    val repository = Repository()

    private var _orderListProduct = MutableLiveData<List<Order>>()
    val orderListProduct : LiveData<List<Order>>
        get() = _orderListProduct

    fun getOrderShopProduct(uId: String){
        _orderListProduct = repository.getShopOrders(uId)
    }




}

