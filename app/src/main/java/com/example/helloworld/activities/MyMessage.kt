package com.example.helloworld.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworld.R
import com.example.helloworld.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_message.*

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
}