package com.example.shopkeeperapp.ui.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopProduct

class ProductListViewModel: ViewModel() {

    val repository = Repository()

    private var _shopListProduct = MutableLiveData<List<ShopProduct>>()
    val shopListProduct : LiveData<List<ShopProduct>>
    get() = _shopListProduct

    fun getListShopProduct(uId: String){
        _shopListProduct = repository.getProducts(uId)
    }


}