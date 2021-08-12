package com.example.shopkeeperapp.ui.Performance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopkeeperapp.data.MostProductSold
import com.example.shopkeeperapp.data.Repository

class PerformanceViewModel(repository: Repository): ViewModel() {

    val repository = repository

    fun getMostSoldItems(month: Int): LiveData<List<MostProductSold>>? {
        return repository.getMostSoldItems(month)
    }
}


class PerformanceViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerformanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerformanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}