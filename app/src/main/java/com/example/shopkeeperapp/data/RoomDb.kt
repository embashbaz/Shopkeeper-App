package com.example.shopkeeperapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Cart::class, ItemProduct::class, Expenduture::class, ShopIncome::class), version = 1, exportSchema = false)
abstract class RoomDb: RoomDatabase() {

    abstract fun roomDao(): RoomDao

    companion object{

        @Volatile
        private var INSTANCE: RoomDb? = null
        fun getDatabase(context: Context): RoomDb {

            var instance = INSTANCE

            if(instance == null) {

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "room_db"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
            }
            return instance

        }


    }



}