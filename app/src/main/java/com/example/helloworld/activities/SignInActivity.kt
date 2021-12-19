package com.example.helloworld.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.helloworld.R

import extensions.Extensions.toast
/** fix missing imports **/
//import kotlinx.android.synthetic.main.activity_sign_in.*
import com.example.helloworld.utils.FirebaseUtils.firebaseAuth

class SignInActivity : AppCompatActivity() {
    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val etSignInEmail=findViewById<EditText>(R.id.etSignInEmail)
        val etSignInPassword=findViewById<EditText>(R.id.etSignInPassword)
        val btnSignIn=findViewById<Button>(R.id.btnSignIn)
        val btnCreateAccount2=findViewById<Button>(R.id.btnCreateAccount2)

        signInInputsArray = arrayOf(etSignInEmail, etSignInPassword)
        btnCreateAccount2.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }

        btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    private fun signInUser() {
        val etSignInEmail=findViewById<EditText>(R.id.etSignInEmail)
        val etSignInPassword=findViewById<EditText>(R.id.etSignInPassword)
        signInEmail = etSignInEmail.text.toString().trim()
        signInPassword = etSignInPassword.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, HomePageActivity::class.java))
                        toast("signed in successfully")
                        finish()
                    } else {
                        toast("sign in failed")
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }
}