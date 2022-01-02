package com.example.helloworld.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.Spinner
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.example.helloworld.R
import com.example.helloworld.firebase.FirebaseCallback
import com.example.helloworld.firebase.FirestoreClass
import com.example.helloworld.firebase.Response
import com.example.helloworld.firebase.UsersViewModel
import com.example.helloworld.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.activity_sign_in_g.*
import kotlinx.android.synthetic.main.dropdown_item.view.*


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
        lateinit var currentUser: User
        lateinit var itemA: String
        lateinit var itemZ: String
        lateinit var profession: String
//        private var teachers: MutableList<User> = ArrayList()
    }

    private var list: MutableList<String> = ArrayList()
    private lateinit var viewModel: UsersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val areaArr: MutableList<String> = ArrayList()
        areaArr.add("Area")
        areaArr.add("צפון")
        areaArr.add("מרכז")
        areaArr.add("יהודה  ושומרון")
        areaArr.add("דרום")
        val spn_area_hp = findViewById<Spinner>(R.id.spn_area_hp)
        val arrayAdapter =ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,areaArr)
        spn_area_hp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemA=areaArr[position]
                Toast.makeText(this@HomePageActivity, "$itemA selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spn_area_hp.adapter = arrayAdapter
        limitDropDownHeight(spn_area_hp)
        val zoomArr: MutableList<String> = ArrayList()
        val spn_zoom_hp = findViewById<Spinner>(R.id.spn_zoom_hp)
        zoomArr.add("zoom")
        zoomArr.add("Yes")
        zoomArr.add("No")

        val arrayAdapter2 =ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,zoomArr)
        spn_zoom_hp.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemZ=zoomArr[position]
                Toast.makeText(this@HomePageActivity, "$itemZ selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spn_zoom_hp.adapter = arrayAdapter2
        limitDropDownHeight(spn_zoom_hp)
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
        viewModel = ViewModelProvider(this)
            .get(UsersViewModel::class.java)
        getResponseUsingCallback()

        searchView.queryHint = "What would you like to learn?"
//        val textViewToChange= findViewById<TextView>(R.id.tv_hello_user)
//        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
//        val name2:String = currentUser.
//        val str = "hello $name2 :)"
//        textViewToChange.text = str
        btn_search.setOnClickListener {
//            Toast.makeText(this,"${teachers}", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@HomePageActivity, MainActivity2::class.java))
        }

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
            R.id.delete_user -> {
                val user = Firebase.auth.currentUser!!

                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseFirestore.getInstance().collection("users").document(user.uid).delete()
                            FirebaseFirestore.getInstance().collection("teachers").document(user.uid).delete()
                            FirebaseDatabase.getInstance().reference.child("user").child(user.uid).removeValue()
                            Log.d(TAG, "User account deleted.")
                        }
                    }
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
    private fun print(response: Response) {
        val search = findViewById<SearchView>(R.id.searchView)
        val listView = findViewById<ListView>(R.id.listView)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
//        listView.adapter = adapter
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if (query != null) {
                    profession=query
                    Log.d("HomePage: ", profession)
                }

                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(applicationContext, "Item mot found", Toast.LENGTH_LONG).show()
                }
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        }

        )
        response.users?.let { users ->
            users.forEach { user ->
                user.allProfession.let {
                    for (j in user.allProfession) {
                        Log.i(TAG, j)
                        if (list.contains(j)) {
                        } else {
                            list.add(j)
                        }

                    }
                }
            }
        }

        response.exception?.message?.let {
            Log.e(TAG, it)
        }
    }
    @SuppressLint("DiscouragedPrivateApi")
    fun limitDropDownHeight(spnTest: Spinner) {
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true

        val popupWindow: ListPopupWindow = popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (200 * resources.displayMetrics.density).toInt()
    }
    private fun getResponseUsingCallback() {
        viewModel.getResponseUsingCallback(object : FirebaseCallback {
            override fun onResponse(response: Response) {
                print(response)
                val u=getCurrentUser(response)
                if(u!=null) {
                    currentUser=User(u.uid,u.name,u.email,u.allProfession,u.mobile,u.area,u.gender,u.image,u.age,u.price)
//                    Toast.makeText(this@HomePageActivity, "${currentUser.allProfession}", Toast.LENGTH_LONG).show()

                }

            }
        })
    }
    private fun getCurrentUser(response: Response): User? {
        response.users?.let { users ->
            users.forEach { user ->
                user.email.let {
                    Toast.makeText(this@HomePageActivity,user.uid,Toast.LENGTH_LONG).show()
                    if(user.uid == FirestoreClass().getCurrentUserID()) {
                        return user
                    }
                }
            }
        }

        response.exception?.message?.let {
            Log.e(ContentValues.TAG, it)
        }
        return null

    }



}