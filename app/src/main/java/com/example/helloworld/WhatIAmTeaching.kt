package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WhatIAmTeaching : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_iam_teaching)
        val btnRegister2 = findViewById<Button>(R.id.btnRegister2)

        btnRegister2.setOnClickListener {
            val intent= Intent(this,HomePageActivity::class.java)
            startActivity(intent)
        }

    }
}