package com.example.shopkeeperapp

import android.app.Application
import com.example.shopkeeperapp.data.Repository
import com.example.shopkeeperapp.data.RoomDb

class  ShopKeeperApp : Application() {
    var uId = ""

    val database: RoomDb by lazy {
        RoomDb.getDatabase(this)
    }

    val repository: Repository by lazy {
        Repository(database.roomDao())
    }

}