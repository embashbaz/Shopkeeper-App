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

    private var _updatingProductOutput = MutableLiveData<HashMap<String, String>>()
    val updatingProductOutput: LiveData<HashMap<String, String>>
        get() = _updatingProductOutput

    private var _deletingProductOutput = MutableLiveData<HashMap<String, String>>()
    val deletingProductOutput: LiveData<HashMap<String, String>>
        get() = _deletingProductOutput

    fun uploadImage (mBitmap: Bitmap, uId: String, picName:String){
        _pictureUploadOutput = repository.uploadImage(mBitmap, uId, picName)

    }

    fun updateProduct(uId: String, docId: String, shopProduct: ShopProduct){
        _updatingProductOutput = repository.updateShopProduct(uId,docId, shopProduct)
    }

    fun deleteProduct(uId: String, docId: String){
        _deletingProductOutput = repository.deleteDocument(uId, docId)
    }

    fun saveNewProduct(shopProduct: ShopProduct, uId: String){
        _addingProductOutput = repository.saveNewProduct(shopProduct, uId)
    }


}