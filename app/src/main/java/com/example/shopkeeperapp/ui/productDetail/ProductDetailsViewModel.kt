package com.example.shopkeeperapp.ui.productDetail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.ShopProduct

class ProductDetailsViewModel: ViewModel() {

    val repository = Repository()


    private var _pictureUploadOutput = MutableLiveData<HashMap<String, String>>()
    val pictureUploadOutput: LiveData<HashMap<String, String>>
        get() = _pictureUploadOutput

    private var _addingProductOutput = MutableLiveData<HashMap<String, String>>()
    val addingProductOutput: LiveData<HashMap<String, String>>
        get() = _addingProductOutput

    fun uploadImage (mBitmap: Bitmap, uId: String, picName:String){
        _pictureUploadOutput = repository.uploadImage(mBitmap, uId, picName)

    }

    fun saveNewProduct(shopProduct: ShopProduct){
        _addingProductOutput = repository.saveNewProduct(shopProduct)
    }


}