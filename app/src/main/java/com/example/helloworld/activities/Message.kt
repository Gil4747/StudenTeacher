package com.example.helloworld.activities

import android.content.IntentSender

class Message {
    var massege: String? = null
    var senderId: String? = null

    constructor(){}
    constructor(message: String?, senderId: String?){
        this.massege = message
        this.senderId = senderId
    }
}