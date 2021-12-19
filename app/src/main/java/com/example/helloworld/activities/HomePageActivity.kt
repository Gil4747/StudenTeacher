package com.example.helloworld.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.example.helloworld.R
import com.example.helloworld.firebase.FirestoreClass
import com.example.helloworld.models.User
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.app_bar_main.*

// TODO (Implement the NavigationView.OnNavigationItemSelectedListener and add the implement members of it.)
class HomePageActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    // TODO (Create a companion object and a constant variable for My profile Screen result.)
    // START
    /**
     * A companion object to declare the constants.
     */
    companion object {
        //A unique code for starting the activity for result
        const val MY_PROFILE_REQUEST_CODE: Int = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // TODO (Call the setup action bar function here.)
        // START
        setupActionBar()
        // END

        // TODO (Assign the NavigationView.OnNavigationItemSelectedListener to navigation view.)
        // START
        // Assign the NavigationView.OnNavigationItemSelectedListener to navigation view.
        nav_view.setNavigationItemSelectedListener(this)
        // END
        FirestoreClass().loudUserData(this)
    }

    // TODO (Add a onBackPressed function and check if the navigation drawer is open or closed.)
    // START
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit()
        }
    }
    // END

    // TODO (Create a function to update the user details in the navigation view.)
    // START
    /**
     * A function to get the current user details from firebase.
     */
    fun updateNavigationUserDetails(user: User) {
        // The instance of the header view of the navigation view.
        val headerView = nav_view.getHeaderView(0)

        // The instance of the user image of the navigation view.
        val navUserImage = headerView.findViewById<ImageView>(R.id.nav_user_image)

        // Load the user image in the ImageView.
        Glide
            .with(this@HomePageActivity)
            .load(user.image) // URL of the image
            .centerCrop() // Scale type of the image.
            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
            .into(navUserImage) // the view in which the image will be loaded.

        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name
    }
    // END
    // TODO (Add the onActivityResult function and check the result of the activity for which we expect the result.)
    // START
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE
        ) {
            // Get the user updated details.
            FirestoreClass().loudUserData(this@HomePageActivity)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }
    // END

    // TODO (Implement members of NavigationView.OnNavigationItemSelectedListener.)
    // START
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        // TODO (Add the click events of navigation menu items.)
        // START
        when (menuItem.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this,MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)

            }

            R.id.nav_sign_out -> {
                // Here sign outs the user from firebase in this device.
                FirebaseAuth.getInstance().signOut()

                // Send the user to the intro screen of the application.
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        // END
        return true
    }
    // END

    // TODO (Create a function to setup action bar.)
    // START
    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        // TODO (Add click event for navigation in the action bar and call the toggleDrawer function.)
        // START
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
        // END
    }
    // END

    // TODO (Create a function for opening and closing the Navigation Drawer.)
    // START
    /**
     * A function for opening and closing the Navigation Drawer.
     */
    private fun toggleDrawer() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

}