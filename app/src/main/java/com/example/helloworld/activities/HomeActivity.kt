//package com.example.helloworld.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import com.example.helloworld.R
////import kotlinx.android.synthetic.main.activity_home.*
//import utils.FirebaseUtils.firebaseAuth
//
//class HomeActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val btnSignOut=findViewById<Button>(R.id.btnSignOut)
//        setContentView(R.layout.activity_home)
//// sign out a user
//
//        btnSignOut.setOnClickListener {
//            firebaseAuth.signOut()
//            val intent = Intent(this, CreateAccountActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//}