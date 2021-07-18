package com.example.shopkeeperapp.data

import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class Repository(var mRoomDao: RoomDao? = null) {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore
    var mFirebaseStore: FirebaseStorage

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()
        mFirebaseStore = FirebaseStorage.getInstance()

    }

    val roomDao: RoomDao? = mRoomDao

    fun login(email: String, password: String): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                val data : HashMap<String, String> = hashMapOf()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    //operationOutput.value?.set("Status", "success")
                    //operationOutput.value?.set("value", mFirebaseAuth.uid.toString())

                    data.put("status", "success")
                    data.put("value", mFirebaseAuth.uid.toString())

                } else {
                    data.put("Status", "Failed")
                    data.put("value",  "Operation Failed with error"+ task.exception)

                    Log.d(ContentValues.TAG, "Failed withe errt", task.exception)

                }

                operationOutput.postValue(data)

            }


        return operationOutput
    }


    fun register(shopKeeper: ShopKeeper, password: String): MutableLiveData<HashMap<String, String>>{

        val operationOutput = MutableLiveData<HashMap<String, String>>()
        var status = hashMapOf<String, String>()
        mFirebaseAuth.createUserWithEmailAndPassword( shopKeeper.email,  password)
            .addOnCompleteListener (){ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val userId = mFirebaseAuth.uid
                    if (userId != null) {
                        shopKeeper.id = userId.toString()
                    }

                    mFirebaseDb.collection("shops").document(userId.toString()).set(shopKeeper)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot written")
                            status.put("status", "success")
                            status.put("value", userId.toString())
                            mFirebaseAuth.signOut()
                            operationOutput.postValue(status)

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                            status.put("status", "Failed")
                            status.put("value",
                                "Operation Failed with error"+ e.toString()
                            )
                            operationOutput.postValue(status)
                        }

                } else {
                    status.put("status", "Failed")
                    status.put("value",
                        "Operation Failed with error"+ task.exception
                    )
                }
                operationOutput.postValue(status)

            }

        return operationOutput

    }

    fun uploadImage (mBitmap: Bitmap, uId: String, picName:String): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()

        val imageRef = mFirebaseStore.reference.child(uId+"/"+picName+".jpg")

        val bitmap = mBitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)

        var status = hashMapOf<String, String>()
        uploadTask.addOnFailureListener {
            status.put("status", "Failed")
            status.put("value",
                "Operation Failed with error"+ it.toString()
            )
            operationOutput.postValue(status)

        }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            if(progress < 100.0){

                status.put("status", "Operation in progress")
                status.put("value","Upload is $progress% done" )


            }else if (progress == 100.0){

                imageRef.downloadUrl.addOnSuccessListener {
                    status.put("status", "Operation finished")
                    status.put("value", it.toString())
                    operationOutput.postValue(status)
                }

            }
            operationOutput.postValue(status)
        }



    return operationOutput
    }

    fun saveNewProduct(shopProduct: ShopProduct, uId: String): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()

        var status = hashMapOf<String, String>()
        mFirebaseDb.collection("shops").document(uId).collection("products").add(shopProduct)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot written")
                status.put("status", "Success")
                status.put("value","Record added" )

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                status.put("status", "Failed")
                status.put("value",e.toString() )
            }

        operationOutput.postValue(status)


        return operationOutput
    }

    fun getProducts(uId:String): MutableLiveData<List<ShopProduct>>{

        val data = MutableLiveData<List<ShopProduct>>()

        val productRef = mFirebaseDb.collection("shops").document(uId).collection("products")

        productRef
            .get()
            .addOnSuccessListener {

                val dataList = ArrayList<ShopProduct>()
                for (snapshot in it){

                    dataList.add(snapshot.toObject(ShopProduct::class.java))
                }
                data.value = dataList

            }.addOnFailureListener {
                data.value = null
            }

        return data
    }



    @WorkerThread
    suspend fun insertCart(cart: Cart){
        roomDao?.insertCart(cart)
    }

    @WorkerThread
    suspend fun insertItemProduct(itemProduct: ItemProduct){
        roomDao?.insertItemProduct(itemProduct)
    }

    @WorkerThread
    suspend fun insertExpenduture(expenduture: Expenduture){
        roomDao?.insertExpenduture(expenduture)
    }

    @WorkerThread
    suspend fun insertIncome(income: ShopIncome){
        roomDao?.insertIncome(income)
    }

    @WorkerThread
    suspend fun updateCart(cart: Cart){
        roomDao?.updateCart(cart)
    }

    @WorkerThread
    suspend fun updateItemProduct(itemProduct: ItemProduct){
        roomDao?.updateItemProduct(itemProduct)
    }

    @WorkerThread
    suspend fun updateExpenduture(expenduture: Expenduture){
        roomDao?.updateExpenduture(expenduture)
    }

    @WorkerThread
    suspend fun updateIncme(income: ShopIncome){
        roomDao?.updateIncme(income)
    }

    @WorkerThread
    suspend fun deleteCart(cart: Cart){
        roomDao?.deleteCart(cart)
    }

    @WorkerThread
    suspend fun deleteItemProduct(item: ItemProduct){
        roomDao?.deleteItemProduct(item)
    }


    fun getAllCart(): LiveData<List<Cart>>{

    }


    fun getAllItemForCart(id: Int): LiveData<List<ItemProduct>>? {
        return roomDao?.getAllItemForCart(id)
    }


    suspend fun deleteAllItemForCart(id: Int){
        roomDao?.deleteAllItemForCart(id)
    }
}