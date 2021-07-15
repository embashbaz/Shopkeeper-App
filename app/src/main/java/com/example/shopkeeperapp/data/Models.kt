package com.example.shopkeeperapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    var docId: String="",
    var productName: String="",
    var productQrCode: Long=0L,
    var price: Double=0.0,
    var itemQuantity: Double=0.0,
    var imageUrl: String="",
    var description: String=""


): Parcelable

@Entity
data class ProductSold(
    @PrimaryKey
    val id:Int,
    val productId: String,
    val namePRoduct: String,
    val totalPrice: Double,
    val day: Int,
    val month: Int,
    val year: Int

)

