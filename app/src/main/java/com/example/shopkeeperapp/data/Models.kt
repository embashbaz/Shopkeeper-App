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
data class ShopIncome(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val productId: String,
    val incomeName: String,
    val totalPrice: Double,
    val day: Int,
    val month: Int,
    val year: Int

)

@Entity
data class Expenditure(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ExpendureName: String,
    val cost: Double,
    val day: Int,
    val month: Int,
    val year: Int

)

@Parcelize
@Entity
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val status: Int,
    val dateCreated: String,
    var totalPrice: Double,

    ): Parcelable

@Parcelize
@Entity
data class ItemProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cartId: Int,
    var name: String,
    val date: String,
    var price: Double,
    var quantity: Double,
    var totalPriceNum: Double,
    var description: String


): Parcelable