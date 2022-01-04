package com.example.helloworld.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworld.R
import com.example.helloworld.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_message.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_in_g.*

class MyMessage : AppCompatActivity() {
    companion object {
        private lateinit var chatList: ArrayList<User>
    }

    private lateinit var adapter: ChatAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_message)
        supportActionBar?.title = "My Messages"
//        setupActionBar()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        chatList = ArrayList()
        var map: HashMap<String, User> = HashMap()
        mDbRef = FirebaseDatabase.getInstance().reference

        //////// chat list associated with current user /////////
        Firebase.auth.currentUser?.let { mDbRef.child("user").child(it.uid).addValueEventListener(object:
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.child("chatList").children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    Log.d("MyMessage","$currentUser")
                 if (currentUser != null) {
                        chatList.add(currentUser)
}
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
            }
        adapter = ChatAdapter(this, chatList)
        userRecyclerView = findViewById(R.id.recyclerViewChat)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.back_from_my_messages, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.back) {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A function for actionBar Setup.
     */


}