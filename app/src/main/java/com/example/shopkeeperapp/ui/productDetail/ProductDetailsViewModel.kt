package com.example.shopkeeperapp.ui.productDetail

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopkeeperapp.data.Repository

class ProductDetailsViewModel: ViewModel() {

    val repository = Repository()


    private var _pictureUploadOutput = MutableLiveData<HashMap<String, String>>()
    val pictureUploadOutput: LiveData<HashMap<String, String>>
        get() = _pictureUploadOutput

    fun uploadImage (mBitmap: Bitmap, uId: String, picName:String){
        _pictureUploadOutput = repository.uploadImage(mBitmap, uId, picName)

    }


}