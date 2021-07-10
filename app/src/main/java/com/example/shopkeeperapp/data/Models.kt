package com.example.shopkeeperapp.data

import com.google.android.gms.maps.model.LatLng


data class ShopKeeper(
    var id: String,
    val email: String,
    val name: String,
    val adress: LatLng,
    val phoneNumber: Long,
    val buisinessArea: String,
    val county: String,
    val more: String

)

data class ShopProduct(
    val docId: String,
    val productName: String,
    val productQrCode: Long,
    val price: Double,
    val itemQuantity: Double,
    val imageUrl: String


)

