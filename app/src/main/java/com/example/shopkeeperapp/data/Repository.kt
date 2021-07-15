package com.example.shopkeeperapp.data

import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class Repository {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore
    var mFirebaseStore: FirebaseStorage

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()
        mFirebaseStore = FirebaseStorage.getInstance()

    }

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
                            status.put("Status", "success")
                            status.put("value", userId.toString())
                            mFirebaseAuth.signOut()

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                            status.put("Status", "Failed")
                            status.put("value",
                                "Operation Failed with error"+ e.toString()
                            )
                        }

                } else {
                    status.put("Status", "Failed")
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
            status.put("Status", "Failed")
            status.put("value",
                "Operation Failed with error"+ it.toString()
            )
            operationOutput.postValue(status)

        }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            if(progress == 100.0){

                status.put("Status", "Operation finished")
                status.put("value", imageRef.downloadUrl.toString()



                )

            }else{
                status.put("Status", "Operation in progress")
                status.put("value","Upload is $progress% done" )

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
                status.put("Status", "Success")
                status.put("value","Record added" )

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                status.put("Status", "Failed")
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
}