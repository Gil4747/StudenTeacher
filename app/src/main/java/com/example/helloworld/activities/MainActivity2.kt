package com.example.helloworld.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helloworld.R
import com.example.helloworld.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_home_page.nav_view
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main_2_1.*
import kotlinx.android.synthetic.main.user_layout.*


class MainActivity2 : AppCompatActivity() {
    companion object {
        private lateinit var userList: ArrayList<User>
    }
    private lateinit var userRecyclerView: RecyclerView

    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.title = "Select teacher"

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this,userList)



        userRecyclerView = findViewById(R.id.UserRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter
        FirebaseDatabase.getInstance().reference.child("user").addValueEventListener(object: ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        if(currentUser?.allProfession!!.size>0){
                            if(HomePageActivity.itemZ !="No") {
                                for (i in currentUser.allProfession) {
                                    if (i == HomePageActivity.profession) {
                                        Log.d("MA", "$currentUser")
                                        userList.add(currentUser!!)
                                    }
                                }
                            }
                            else{
                                if(currentUser.area==HomePageActivity.itemA){
                                    for (i in currentUser.allProfession) {
                                        Log.d("Main2---------לגךדח", "HomePageActivity.profession")

                                        if (i == HomePageActivity.profession) {
                                            Log.d("MA", "$currentUser")
                                            userList.add(currentUser!!)
                                        }
                                    }
                                }
                            }
                        }

                        Log.d("Main2-list", "$userList")
//                        updateNavigationUserDetails(userList)

                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            mAuth.signOut()
            finish()
            return true
        }
        return true
    }

}