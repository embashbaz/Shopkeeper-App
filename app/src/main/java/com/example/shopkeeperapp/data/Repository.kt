package com.example.shopkeeperapp.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Repository {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()

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
}