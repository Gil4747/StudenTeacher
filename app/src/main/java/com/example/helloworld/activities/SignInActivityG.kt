package com.example.helloworld.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.helloworld.R
import android.view.WindowManager
import android.widget.Toast
import com.example.helloworld.models.User

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in_g.*
import kotlinx.android.synthetic.main.activity_sign_in_g.et_email_sign_in
import kotlinx.android.synthetic.main.activity_sign_in_g.et_password_sign_in
import kotlinx.android.synthetic.main.activity_sign_up.*

// TODO (Add the Sign In activity.)
// START
class SignInActivityG : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_in_g)
        auth = Firebase.auth
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        btn_sign_in.setOnClickListener {
            singInRegisteredUser()
        }
        setupActionBar()
    }
    fun signInSuccess(user :User){
        hideProgressDialog()
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun singInRegisteredUser(){
        val email: String = et_email_sign_in.text.toString().trim { it <= ' ' }
        val password: String = et_password_sign_in.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign in", "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, HomePageActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign in", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    private fun validateForm(email: String, password: String): Boolean {
        return when {

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }

}