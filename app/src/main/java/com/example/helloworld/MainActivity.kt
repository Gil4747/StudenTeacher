package com.example.helloworld
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText


open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_sign_in_intro = findViewById<Button>(R.id.btn_sign_in_intro)
        val btn_sign_up_intro = findViewById<Button>(R.id.btn_sign_up_intro)


        btn_sign_in_intro.setOnClickListener {
            val intent= Intent(this,LogInActivity::class.java)
            startActivity(intent)
        }


        btn_sign_up_intro.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
//        window.setFlags(
//            windowManager.LayoutParmas.FLAG_FULLSCREEN,
//            windowManager.LayoutParmas.FLAG_FULLSCREEN
//        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }




}