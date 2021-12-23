package com.example.helloworld.firebase

import com.example.helloworld.models.User

data class Response(
    var users: List<User>? = null,
    var exception: Exception? = null
)