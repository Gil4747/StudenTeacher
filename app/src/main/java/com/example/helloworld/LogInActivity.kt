package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnLogin=findViewById<Button>(R.id.btnLogin)//גישה לכפתור "היכנס"
        val edUsername=findViewById<EditText>(R.id.edUsername)
        val edPassword=findViewById<EditText>(R.id.edPassword)

        btnLogin.setOnClickListener {//כל מה שנכתוב כאן יקרה ברגע שנלחץ על בכפתור
            if(edUsername.text.trim().isNotEmpty() || edPassword.text.trim().isNotEmpty()){
                Toast.makeText(this, "input provided", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "input required", Toast.LENGTH_SHORT).show()
            }

        }
    }
}