package com.example.shopkeeperapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.GeoPoint

import kotlinx.android.parcel.Parcelize


data class ShopKeeper(
    var id: String,
    val email: String,
    val name: String,
    val address: GeoPoint,
    val phoneNumber: Long,
    val buisinessArea: String,
    val county: String,
    val more: String,
    var msgToken : String = ""

)

@Parcelize
data class Order(
    var id: String = "",
    var shopId: String = "",
    var userId: String = "",
    var shopName: String = "",
    var userName: String = "",
    var userToken: String = "",
    var shopToken: String = "",
    var cart: Cart? = null,
    var itemList: List<ItemProduct>? = null,

    ): Parcelable


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
    var id: Int = 0,
    var type: String = "",
    var status: Int = 0,
    var dateCreated: String = "",
    var totalPrice: Double = 0.0,
    var shopKey: String = ""

    ): Parcelable

@Parcelize
@Entity
data class ItemProduct(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cartId: Int = 0,
    var name: String = "",
    var date: String = "",
    var price: Double = 0.0,
    var quantity: Double = 0.0,
    var totalPriceNum: Double = 0.0,
    var description: String = ""


): Parcelable