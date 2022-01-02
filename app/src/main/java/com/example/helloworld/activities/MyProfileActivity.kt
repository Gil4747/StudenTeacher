package com.example.helloworld.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.helloworld.R
import com.example.helloworld.firebase.FirebaseCallback
import com.example.helloworld.firebase.FirestoreClass
import com.example.helloworld.firebase.Response
import com.example.helloworld.firebase.UsersViewModel
import com.example.helloworld.models.User
import com.example.helloworld.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.view.ViewGroup.MarginLayoutParams
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dropdown_item.view.*


class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE =1
        private const val PICK_IMAGE_REQUEST_CODE =2
        lateinit var priceET: TextView
        lateinit var priceTV: TextView
        lateinit var AddPriceET: EditText
        lateinit var AddPpriceTV: TextView
        private lateinit var CurrentUser:User
    }
    var list_of_id_professions: ArrayList<EditText> = ArrayList()
    var list_of_id_professionsTV: ArrayList<TextView> = ArrayList()
    var allEd: ArrayList<EditText> = ArrayList()
    private lateinit var viewModel: UsersViewModel
    // TODO (Add a global variable for URI of a selected image from phone storage.)
    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // TODO (Add the global variables for UserDetails and Profile Image URL.)
    // START
    // A global variable for user details.
    private lateinit var mUserDetails: User


    // A global variable for a user profile image URL
    private var mProfileImageURL: String = ""
    // END

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        // TODO (Call a function to setup action bar.)
        setupActionBar()
        viewModel = ViewModelProvider(this)
            .get(UsersViewModel::class.java)
        getResponseUsingCallback()

        // TODO (Add a click event for iv_profile_user_image.)
        // START
        iv_profile_user_image.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // TODO (Call the image chooser function.)
                // START
                showImageChooser()
                // END
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        // TODO (Add a click event for updating the user profile data to the database.)
        // START
        btn_update.setOnClickListener {

            // Here if the image is not selected then update the other details of user.
            if (mSelectedImageFileUri != null) {

                uploadUserImage()
            } else {

                showProgressDialog(resources.getString(R.string.please_wait))

                // Call a function to update user details in the database.
                updateUserProfileData()
            }
        }
        getResponseUsingCallback()
        // END

        FirebaseFirestore.getInstance().collection(Constants.USERS).document(FirestoreClass().getCurrentUserID()).get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser =
                    document.toObject(User::class.java)!!
                if(loggedInUser.allProfession.isNotEmpty()) {
//            setContentView(R.layout.activity_sign_up)
//            val allEds: MutableList<EditText> = ArrayList()
                    val ll_my_profile = findViewById<View>(R.id.ll_my_profile) as LinearLayout

                    val display: Display =
                        (applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                    val width: Int = display.getWidth() / 3
                    for (i in 1..HomePageActivity.currentUser.allProfession.size) {
                        val l = LinearLayout(this)
                        l.orientation = LinearLayout.HORIZONTAL
                        val tv=TextView(this)
                        val et = EditText(this)///לשנות את זה לTextView
                        val p = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        tv.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        et.layoutParams = p
                        et.hint="profession"
                        tv.hint="profession"
                        et.id = i * 5
                        tv.id = (-i) * 5
                        list_of_id_professionsTV.add(tv)
                        list_of_id_professions.add(et)
                        Log.d("MyProfile: ", "${i*5}")
                        ll_my_profile.addView(tv)
                        ll_my_profile.addView(et)
                        val lpt = tv.layoutParams as MarginLayoutParams
                        lpt.setMargins(20, lpt.topMargin, lpt.rightMargin, lpt.bottomMargin)

                    }
                    val l = LinearLayout(this)
                    l.orientation = LinearLayout.HORIZONTAL
                    val et = EditText(this)
                    val tvP=TextView(this)
                    val p = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    tvP.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    et.layoutParams = p
                    et.hint="Price"
                    tvP.hint="Price"
                    et.id = -1
                    tvP.id = -2
                    priceET=et
                    priceTV=tvP
                    ll_my_profile.addView(tvP)
                    ll_my_profile.addView(et)

                    val lpt = tvP.layoutParams as MarginLayoutParams
                    lpt.setMargins(20, lpt.topMargin, lpt.rightMargin, lpt.bottomMargin)
                }
            }
            else {
                Log.d("loadUser", "No such document")
            }

        }
//        Toast.makeText(this,SignUpActivity().allEd.size,Toast.LENGTH_LONG)

        btn_teach_my_profile.setOnClickListener {
            val et_add_classes: String = et_add_classes.text.toString().trim { it <= ' ' }
            val num_of_classes:Int = et_add_classes.toByte().toInt()

//            setContentView(R.layout.activity_sign_up)
            val ll_adding_professions = findViewById<View>(R.id.ll_adding_professions) as LinearLayout
            val display: Display =
                (applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val width: Int = display.getWidth() / 3
            for (i in 1..num_of_classes) {
                val l = LinearLayout(this)
                l.orientation = LinearLayout.HORIZONTAL
                val et = EditText(this)
                val p = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                et.layoutParams = p
                et.id = (i*10)
                et.hint="Profession$i"
                allEd.add(et)
                ll_adding_professions.addView(et)
            }///להוסיף מחיר רק אם הוא לא היה מורה לפני כן
            if(list_of_id_professions.isEmpty() || list_of_id_professions.size==0){
                val l = LinearLayout(this)
                l.orientation = LinearLayout.HORIZONTAL
                val et = EditText(this)
                val tvP=TextView(this)
                val p = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                tvP.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                et.layoutParams = p
                et.hint="Price"
//                tvP.hint="Price"
                et.id = -1000
                tvP.id = -2000
                AddPriceET=et
                AddPpriceTV=tvP
                ll_adding_professions.addView(tvP)
                ll_adding_professions.addView(et)

            }
        }

        FirestoreClass().loudUserData(this)



    }
    // TODO (Get the result of the image selection based on the constant code.)
    // START
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data

            try {
                // Load the user image in the ImageView.
                Glide
                    .with(this@MyProfileActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                    .into(iv_profile_user_image) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    // END

    // TODO (Check the result of runtime permission after the user allows or deny based on the unique code.)
    // START
    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO (Call the image chooser function.)
                // START
                showImageChooser()
                // END
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    // END
    // TODO (Create a function for image selection from phone storage.)
    // START
    /**
     * A function for user profile image selection from phone storage.
     */
    private fun showImageChooser() {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
// END


    // TODO (Create a function to setup action bar.)
    // START
    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }
    // END
    // TODO (Update the user profile details into the database.)
    // START
    /**
     * A function to update the user profile details into the database.
     */

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String,Any>()


        val userHashMap2 = HashMap<String,ArrayList<String>>()




        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if (et_name.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = et_name.text.toString()

        }
        if (et_area.text.toString() != mUserDetails.area) {
            userHashMap[Constants.AREA] = et_area.text.toString()

        }
        if (et_gender.text.toString() != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = et_gender.text.toString()

        }

        if(mUserDetails.mobile!=0L){
        if (et_mobile.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = et_mobile.text.toString().toLong()
        }}
        else{
            if(et_mobile.text?.isNotEmpty() == true){
            userHashMap[Constants.MOBILE] = et_mobile.text.toString().toLong()
        }}
//        if(allEd.isNotEmpty()){
//            for(i in allEd){
////                mUserDetails.allProfession.add(i.text.toString())
//                list_of_id_professions.add(i)
//            }
//        }
        if(allEd.size>0) {
            var count = 0
            var listAddP: ArrayList<String> = ArrayList()
            for (i in allEd) {
                if (count < mUserDetails.allProfession.size) {
                    if (i.text.toString() != mUserDetails.allProfession[count]) {
                        listAddP.add(i.text.toString())
                    }
                    count++
                } else {
                    listAddP.add(i.text.toString())
                    count++
                }
            }
            if(mUserDetails.allProfession.isNotEmpty()) {
                for (i in allEd) {
                        if (!mUserDetails.allProfession.contains(i.text.toString()))
                            mUserDetails.allProfession.add(i.text.toString())
                    }
                }
            else{
                for (i in allEd) {
                    if (!mUserDetails.allProfession.contains(i.text.toString()))
                        mUserDetails.allProfession.add(i.text.toString())
                }
                mUserDetails.price= AddPriceET.text.toString().toInt()
                userHashMap[Constants.PRICE]= mUserDetails.price
            }
            userHashMap2[Constants.ALLPROFESSION]= mUserDetails.allProfession
        }

        // Update the data in the database.

        FirestoreClass().updateUserProfileData(this@MyProfileActivity, userHashMap)
        FirestoreClass().updateUserProfileData2(this@MyProfileActivity, userHashMap2)
//        setUserDataInUI(mUserDetails)
        FirestoreClass().updateUserToDatabase(this@MyProfileActivity, userHashMap)
        FirestoreClass().updateUserClassesToDatabase(this@MyProfileActivity, userHashMap2)

    }
    // END

    // TODO (Create a function to set the existing data in UI.)
    // START
    /**
     * A function to set the existing details in UI.
     */
    fun setUserDataInUI(user: User) {
        // TODO (Initialize the user details variable)
        // START
        // Initialize the user details variable
        mUserDetails = user
        // END
        Glide
            .with(this@MyProfileActivity)
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
        if(user.allProfession.isNotEmpty()) {
            var count = 0
//            priceTV.hint="Price"
            priceET.text = user.price.toString()

            for (i in 0 until list_of_id_professions.size) {
                if(list_of_id_professionsTV.isNotEmpty() && i<list_of_id_professionsTV.size) {
                    list_of_id_professionsTV[i].hint = "Profession${count + 1}"
                }
                list_of_id_professions[i].setText(user.allProfession[count])
                count++
                }

            }
        CurrentUser= User(user.uid,user.name,user.email,user.allProfession,user.mobile,user.area,user.gender,user.image,user.age,user.price)
    }
    // END

    // TODO (Create a function to upload the selected user image to storage and get the url of it to store in the database.)
    // START
    // Before start with database we need to perform some steps in Firebase Console and after adding a dependency in Gradle file.
    // Follow the Steps:
    // Step 1: Go to the "Storage" tab in the Firebase Console in your project details in the navigation bar under "Develop".
    // Step 2: In the Storage Page click on the Get Started. Click on Next
    // Step 3: As we have already selected the storage location while creating the database so now click the Done button.
    // Step 4: Now the storage bucket is created.
    // Step 5: For more details visit the link: https://firebase.google.com/docs/storage/android/start
    // Step 6: Now add the code to upload image.
    /**
     * A function to upload the selected user image to firebase cloud storage.
     */
    private fun uploadUserImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(mSelectedImageFileUri))

            //adding the file to reference
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                    // The image upload is success
                    Log.i(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    // Get the downloadable url from the task snapshot
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            // assign the image url to the variable.
                            mProfileImageURL = uri.toString()
                            // Call a function to update user details in the database.
                           updateUserProfileData()
                        }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        this@MyProfileActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

                    hideProgressDialog()
                }
        }
    }

    // TODO (Create a function to get the extension of the selected image.)
    // START
    /**
     * A function to get the extension of selected image.
     */
    private fun getFileExtension(uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    // END
    // TODO (Create a function to notify the user profile is updated successfully.)
    // START
    /**
     * A function to notify the user profile is updated successfully.
     */
    fun profileUpdateSuccess() {

        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()

    }
    // END
    private fun getResponseUsingCallback() {
        viewModel.getResponseUsingCallback(object : FirebaseCallback {
            override fun onResponse(response: Response) {
                val u=getCurrentUser(response)
                if(u!=null) {
                    HomePageActivity.currentUser=User(u.uid,u.name,u.email,u.allProfession,u.mobile,u.area,u.gender,u.image)
                }

            }
        })
    }
    private fun getCurrentUser(response: Response): User? {
        response.users?.let { users ->
            users.forEach { user ->
                user.email.let {
                   if(user.uid == FirestoreClass().getCurrentUserID())
                       return user
                }
            }
        }

        response.exception?.message?.let {
            Log.e(ContentValues.TAG, it)
        }
        return null

    }

}
