package com.example.helloworld.activities

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.helloworld.R
import com.example.helloworld.firebase.FirestoreClass
import com.example.helloworld.models.User
import com.example.helloworld.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_profile.*

class UserProfileActivity : AppCompatActivity() {
    // TODO (Add the global variables for UserDetails and Profile Image URL.)
    // START
    // A global variable for user details.
    private lateinit var mUserDetails: User
    var list_of_id_professions: ArrayList<EditText> = ArrayList()
    var list_of_id_professionsTV: ArrayList<TextView> = ArrayList()
    var list_of_id_price: ArrayList<EditText> = ArrayList()
    var list_of_id_priceTV: ArrayList<TextView> = ArrayList()

    // A global variable for a user profile image URL
    private var mProfileImageURL: String = ""
    // END

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        FirestoreClass().loudUserData(this)





        FirebaseFirestore.getInstance().collection(Constants.USERS)
            .document(FirestoreClass().getCurrentUserID()).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Here we have received the document snapshot which is converted into the User Data model object.
                    val loggedInUser =
                        document.toObject(User::class.java)!!
                    if (loggedInUser.allProfession.isNotEmpty()) {
                        val ll_my_profile = findViewById<View>(R.id.ll_my_profile) as LinearLayout

                        val display: Display =
                            (applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                        val width: Int = display.getWidth() / 3
                        for (i in 1..HomePageActivity.currentUser.allProfession.size) {
                            val l = LinearLayout(this)
                            l.orientation = LinearLayout.HORIZONTAL
                            val tv = TextView(this)
                            val et = EditText(this)///לשנות את זה לTextView
                            val tvP = TextView(this)
                            val etP = EditText(this)///לשנות את זה לTextView
                            val p = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                            tv.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            tvP.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                            et.layoutParams = p
                            etP.layoutParams = p
                            et.hint = "Profession"
                            tv.hint = "Profession"
                            etP.hint = "Price"
                            tvP.hint = "Price"
                            et.id = i * 5
                            tv.id = (-i) * 5
                            list_of_id_professionsTV.add(tv)
                            list_of_id_professions.add(et)
                            list_of_id_priceTV.add(tvP)
                            list_of_id_price.add(etP)
                            Log.d("MyProfile: ", "${i * 5}")
                            ll_my_profile.addView(tv)
                            ll_my_profile.addView(et)
                            ll_my_profile.addView(tvP)
                            ll_my_profile.addView(etP)
                            val lpt = tv.layoutParams as ViewGroup.MarginLayoutParams
                            lpt.setMargins(20, lpt.topMargin, lpt.rightMargin, lpt.bottomMargin)
                            val lpt2 = tvP.layoutParams as ViewGroup.MarginLayoutParams
                            lpt.setMargins(20, lpt2.topMargin, lpt2.rightMargin, lpt2.bottomMargin)

                        }
                    }
                }
            }
    }
                fun setUserDataInUI(user: User) {
                    // TODO (Initialize the user details variable)
                    // START
                    // Initialize the user details variable
                    mUserDetails = user
                    // END
                    Glide
                        .with(this@UserProfileActivity)
                        .load(user.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(iv_profile_user_image)


                    et_name.setText(user.name)
                    et_email.setText(user.email)
                    et_area.setText(user.area)
                    et_gender.setText(user.gender)
                    et_age.setText(user.age.toString())
                    if (user.mobile != 0L) {
                        et_mobile.setText(user.mobile.toString())
                    }
                    if (user.allProfession.isNotEmpty()) {
                        var count = 0
                        var count2 = 0
//            priceTV.hint="Price"
//            priceET.text = user.price.toString()

                        for (i in 0 until list_of_id_professions.size) {
                            if (list_of_id_professionsTV.isNotEmpty() && i < list_of_id_professionsTV.size) {
                                list_of_id_professionsTV[i].hint = "Profession${count + 1}"
                            }
                            list_of_id_professions[i].setText(user.allProfession[count])
                            count++
                        }
                        for (i in 0 until list_of_id_price.size) {
                            if (list_of_id_priceTV.isNotEmpty() && i < list_of_id_priceTV.size) {
                                list_of_id_priceTV[i].hint = "Price"
                            }
                            list_of_id_price[i].setText("${user.price.get(list_of_id_professions[i].text.toString())}")
                            count2++
                        }

                    }
                }
                // END
            }

