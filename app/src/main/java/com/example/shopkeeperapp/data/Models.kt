package com.example.shopkeeperapp.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

import kotlinx.android.parcel.Parcelize


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


@Parcelize
data class ShopProduct(
    val docId: String,
    val productName: String,
    val productQrCode: Long,
    val price: Double,
    val itemQuantity: Double,
    val imageUrl: String,
    val description: String


): Parcelable

