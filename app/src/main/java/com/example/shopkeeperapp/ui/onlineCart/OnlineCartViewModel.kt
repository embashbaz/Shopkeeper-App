package com.example.shopkeeperapp.ui.onlineCart

import androidx.lifecycle.*
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopIncome
import com.example.shopkeeperapp.data.ShopProduct
import com.example.shopkeeperapp.ui.Performance.PerformanceViewModel
import kotlinx.coroutines.launch

class OnlineCartViewModel(mRepository: Repository): ViewModel() {
    val repository = mRepository

    private var _updatingOrderOutput = MutableLiveData<HashMap<String, String>>()
    val updatingOrderOutput: LiveData<HashMap<String, String>>
        get() = _updatingOrderOutput

    fun updateOrder(order: Order){
        _updatingOrderOutput = repository.updateOrder(order)
    }

    fun addItemsToCart(items: ArrayList<ShopIncome>) = viewModelScope.launch{
        repository.insertIncome(items)
    }

}

class OnlineViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnlineCartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnlineCartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}