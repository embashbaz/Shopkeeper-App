package com.example.shopkeeperapp.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RoomDao {

    @Insert
    suspend fun insertCart(cart: Cart)

    @Insert
    suspend fun insertItemProduct(itemProduct: ItemProduct)

    @Insert
    suspend fun insertExpenduture(expenditure: Expenditure)

    @Insert
    suspend fun insertIncome(income: ShopIncome)

    @Update
    suspend fun updateCart(cart: Cart)

    @Update
    suspend fun updateItemProduct(itemProduct: ItemProduct)

    @Update
    suspend fun updateExpenduture(expenditure: Expenditure)

    @Update
    suspend fun updateIncme(income: ShopIncome)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Delete
    suspend fun deleteItemProduct(item: ItemProduct)

    @Query("SELECT * FROM Cart ORDER BY id")
    fun getAllCart(): LiveData<List<Cart>>

    @Query("SELECT * FROM Itemproduct WHERE cartId =:id")
    fun getAllItemForCart(id: Int): LiveData<List<ItemProduct>>

    @Query("DELETE FROM Itemproduct WHERE cartId =:id")
    suspend fun deleteAllItemForCart(id: Int)



}