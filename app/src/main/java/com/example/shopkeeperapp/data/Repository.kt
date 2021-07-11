package com.example.shopkeeperapp.data

import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
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
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    operationOutput.value?.set("Status", "success")
                    operationOutput.value?.set("value", mFirebaseAuth.uid.toString())


                } else {
                    operationOutput.value?.set("Status", "Failed")
                    operationOutput.value?.set("value",
                        "Operation Failed with error"+ task.exception
                    )

                }

            }


        return operationOutput
    }


    fun register(shopKeeper: ShopKeeper, password: String): MutableLiveData<HashMap<String, String>>{

        val operationOutput = MutableLiveData<HashMap<String, String>>()

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
                            operationOutput.value?.set("Status", "success")
                            operationOutput.value?.set("value", userId.toString())
                            mFirebaseAuth.signOut()

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                            operationOutput.value?.set("Status", "Failed")
                            operationOutput.value?.set("value",
                                "Operation Failed with error"+ e.toString()
                            )
                        }

                } else {
                    operationOutput.value?.set("Status", "Failed")
                    operationOutput.value?.set("value",
                        "Operation Failed with error"+ task.exception
                    )
                }

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
        uploadTask.addOnFailureListener {
            operationOutput.value?.set("Status", "Failed")
            operationOutput.value?.set("value",
                "Operation Failed with error"+ it.toString()
            )

        }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            if(progress == 100.0){

                operationOutput.value?.set("Status", "Operation finished")
                operationOutput.value?.set("value", imageRef.downloadUrl.toString()

                )

            }else{
                operationOutput.value?.set("Status", "Operation finished")
                operationOutput.value?.set("value","Upload is $progress% done" )

            }
        }


    return operationOutput
    }

    fun saveNewProduct(shopProduct: ShopProduct): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()

        mFirebaseDb.collection("shopProduct").add(shopProduct)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot written")
                operationOutput.value?.set("Status", "Success")
                operationOutput.value?.set("value","Record added" )

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                operationOutput.value?.set("Status", "Failed")
                operationOutput.value?.set("value",e.toString() )
            }


        return operationOutput
    }
}