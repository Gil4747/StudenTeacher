package com.example.helloworld.firebase

import com.example.helloworld.models.User
import com.example.helloworld.utils.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class UsersRepository(
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val userRef: CollectionReference = rootRef.collection(Constants.USERS)
) {
    fun getResponseFromFirestoreUsingCallback(callback: FirebaseCallback) {
        userRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.users = result.documents.mapNotNull { snapShot ->
                        snapShot.toObject(User::class.java)
                    }
                }
            } else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }
}