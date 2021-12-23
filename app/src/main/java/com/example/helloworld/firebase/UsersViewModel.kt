package com.example.helloworld.firebase

import androidx.lifecycle.ViewModel

class UsersViewModel (
    private val repository: UsersRepository = UsersRepository()
): ViewModel() {
    fun getResponseUsingCallback(callback: FirebaseCallback) = repository.getResponseFromFirestoreUsingCallback(callback)
}