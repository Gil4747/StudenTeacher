//package com.example.helloworld.activities
//import android.content.Intent
//import android.os.Bundle
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import com.example.helloworld.R
//import com.google.firebase.auth.FirebaseUser
//import extensions.Extensions.toast
//import kotlinx.android.synthetic.main.activity_create_account.*
//import com.example.helloworld.utils.FirebaseUtils.firebaseAuth
//import com.example.helloworld.utils.FirebaseUtils.firebaseUser
//
//
//class CreateAccountActivity : AppCompatActivity() {
//    lateinit var userEmail: String
//    lateinit var userPassword: String
//    lateinit var createAccountInputsArray: Array<EditText>
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        val etEmail = findViewById<EditText>(R.id.etEmail)
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val etConfirmPassword = findViewById<EditText>(R.id.etPassword)
////                val edUsername = findViewById<EditText>(R.id.editUsername)
////                val editGender = findViewById<EditText>(R.id.editGender)
////                val editAboutme = findViewById<EditText>(R.id.editAboutme)
////                val spnArea = findViewById<Spinner>(R.id.spnTest)
////                val btn_sign_up_intro = findViewById<Button>(R.id.btn_sign_up_intro)
////                val btnCreateAccount = findViewById<Button>(R.id.btnRegister)
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_account)
////                        createAccountInputsArray = arrayOf(etEmail, etPassword, etConfirmPassword)
//        btnCreateAccount.setOnClickListener {
//            signIn()
//        }
//
//
//        btnSignIn2.setOnClickListener {
//            startActivity(Intent(this, SignInActivity::class.java))
//            toast("please sign into your account")
//            finish()
//        }
//    }
//
//    /* check if there's a signed-in user*/
//
//    override fun onStart() {
//        super.onStart()
//        val user: FirebaseUser? = firebaseAuth.currentUser
//        user?.let {
//            startActivity(Intent(this, SignInActivity::class.java))
//            toast("welcome back")
//        }
//    }
//
//    private fun notEmpty(): Boolean {
//        val etEmail = findViewById<EditText>(R.id.etEmail)
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val etConfirmPassword = findViewById<EditText>(R.id.etPassword)
//        return etEmail.text.toString().trim().isNotEmpty() &&
//                etPassword.text.toString().trim().isNotEmpty() &&
//                etConfirmPassword.text.toString().trim().isNotEmpty()
//    }
//
//    private fun identicalPassword(): Boolean {
//        val etEmail = findViewById<EditText>(R.id.etEmail)
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val etConfirmPassword = findViewById<EditText>(R.id.etPassword)
//        var identical = false
//        if (notEmpty() &&
//            etPassword.text.toString().trim() == etConfirmPassword.text.toString().trim()
//        ) {
//            identical = true
//        } else if (!notEmpty()) {
//            createAccountInputsArray.forEach { input ->
//                if (input.text.toString().trim().isEmpty()) {
//                    input.error = "${input.hint} is required"
//                }
//            }
//        } else {
//            toast("passwords are not matching !")
//        }
//        return identical
//    }
//
//    private fun signIn() {
//        val etEmail = findViewById<EditText>(R.id.etEmail)
//        val etPassword = findViewById<EditText>(R.id.etPassword)
//        val etConfirmPassword = findViewById<EditText>(R.id.etPassword)
//        if (identicalPassword()) {
//            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
//            userEmail = etEmail.text.toString().trim()
//            userPassword = etPassword.text.toString().trim()
//
//
//                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                toast("created account successfully !")
//                                sendEmailVerification()
//                                startActivity(Intent(this, HomePageActivity::class.java))
//                                finish()
//                            } else {
//                                toast("failed to Authenticate !")
//                            }
//                        }
//        }
//    }
//
//    /* send verification email to the new user. This will only
//    *  work if the firebase user is not null.
//    */
//
//    private fun sendEmailVerification() {
//        firebaseUser?.let {
//            it.sendEmailVerification().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    toast("email sent to $userEmail")
//                }
//            }
//        }
//    }
//
//}