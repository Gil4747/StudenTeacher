package com.example.helloworld.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworld.R
import com.example.helloworld.firebase.FirestoreClass
import com.example.helloworld.models.User
import com.example.helloworld.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main2.*


class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var receiveUser: User

    var receiveRoom: String? =null
    var senderRoom: String? =null

    companion object {
        private lateinit var chatMapR: HashMap<String,User>
        private lateinit var chatMapS: HashMap<String,User>
        var receiverUid: String? = null
        lateinit var receiverImage: String
    }

//    private lateinit var binding: ResultProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//        binding = ResultProfileBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
    setContentView(R.layout.activity_chat)




    val name = intent.getStringExtra("name")
    receiverUid = intent.getStringExtra("uid")
    Log.d("Reciver", "${receiverUid}")
//        ksdljjjjjjjjjjakdljdslkjflkdj
    val senderUid1 = FirebaseAuth.getInstance().currentUser?.uid
    val senderUid = Firebase.auth.currentUser?.uid
    if (senderUid != null) {
        Log.d("Senser", senderUid)
    }
//        Toast.makeText(this, senderUid, Toast.LENGTH_LONG)
    mDbRef = FirebaseDatabase.getInstance().reference
    senderRoom = receiverUid + senderUid
    receiveRoom = senderUid + receiverUid

    supportActionBar?.title = name
    supportActionBar?.subtitle = "<- To teacher's profile"

    chatRecyclerView = findViewById(R.id.chatRecyclerView)
    messageBox = findViewById(R.id.messageBox)
    sendButton = findViewById(R.id.sentButton)
    messageList = ArrayList()
    chatMapR= HashMap()
    chatMapS= HashMap()
    messageAdapter = messageAdapter(this, messageList)

    chatRecyclerView.layoutManager = LinearLayoutManager(this)
    chatRecyclerView.adapter = messageAdapter

    FirebaseDatabase.getInstance().reference.child("user").addValueEventListener(object: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            Log.d("mainActivity:","$snapshot")
            for (postSnapshot in snapshot.children){
                val currentUser = postSnapshot.getValue(User::class.java)
                if(receiverUid == currentUser?.uid){
                    if (currentUser != null) {
                        Log.d("mainActivity:", "image:${currentUser.image}")
                    }
                    if (currentUser != null) {
                        receiverImage = currentUser.image
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {}

    })
    //logic for adding data to recyclerView
    mDbRef.child("chats").child(senderRoom!!).child("messages")
        .addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children) {
                    val message = postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        //adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)
            if (senderUid != null) {
                FirebaseDatabase.getInstance().reference.child("user").child(senderUid).child("chatList").addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var isInChatList: Boolean = false
                        for (postSnapshot in snapshot.children) {
                            val currentUser = postSnapshot.getValue(User::class.java)
                            if (receiverUid == currentUser?.uid) {
                                isInChatList = true
                                break
                            }
                        }
                        if (isInChatList == false) {
                            if (receiverUid != null) {
                                val docRef =
                                    FirebaseFirestore.getInstance().collection(Constants.USERS)
                                        .document(receiverUid!!)
                                docRef.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val loggedInUser = document.toObject(User::class.java)!!
                                        receiveUser = loggedInUser
                                        Log.d("chatActivity", "היייייייייייייייייייייייייייייייייייייייי")
                                        chatMapR.put(loggedInUser.uid,loggedInUser)
                                        Log.d("chatActivity", "$chatMapR")
                                    }
                                }

                            }
                            val docRef =
                                FirebaseFirestore.getInstance().collection(Constants.USERS)
                                    .document(senderUid)
                            docRef.get().addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val loggedInUser = document.toObject(User::class.java)!!
//                                   senderUser = loggedInUser
                                    Log.d("chatActivity", "היייייייייייייייייייייייייייייייייייייייי")
                                    chatMapS.put(loggedInUser.uid,loggedInUser)
                                    Log.d("chatActivity", "$chatMapS")
                                }
                            }

                        }
                    }



                    override fun onCancelled(error: DatabaseError) {
                    }

                })
                FirebaseDatabase.getInstance().reference.child("user").child(senderUid).child("chatList").setValue(
                    chatMapR).addOnSuccessListener {
                }
                if (receiverUid != null) {
                    FirebaseDatabase.getInstance().reference.child("user").child(receiverUid!!).child("chatList").setValue(
                        chatMapS).addOnSuccessListener {
                    }
                }
            }



            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiveRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")

        }
//    updateNavigationUserDetails(receiveUser)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res

        menuInflater.inflate(R.menu.menu_profile, menu)
//        Log.d("mainActivity:","image:${receiverImage}")
//        var m= menu
        // Load the user image in the ImageView.
//        Glide
//            .with(this@ChatActivity)
//            .load(receiverImage) // URL of the image
////                .centerCrop() // Scale type of the image.
////                .placeholder(R.drawable.ic_user_place_holder)
//            .into(m) // A default place holder                .into(navUserImage) // the view in which the image will be loaded.
//        Log.d("mainActivity:","יששששששששששששששששששששש")

        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.userButton) {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
//    @SuppressLint("CheckResult")
//    fun updateNavigationUserDetails(user: User) {
//        // The instance of the header view of the navigation view.
////        val headerView = card_view.getHeaderView(0)
//
//        // The instance of the user image of the navigation view.
////            val navUserImage = findViewById<ImageView>(R.id.iv_profile_user_card_image)
//        Log.d("mainActivity:","image:${user.image}")
//        // Load the user image in the ImageView.
//        Glide
//            .with(this@ChatActivity)
//            .load(user.image) // URL of the image
////                .centerCrop() // Scale type of the image.
////                .placeholder(R.drawable.ic_user_place_holder)
//            .into(userButton) // A default place holder                .into(navUserImage) // the view in which the image will be loaded.
//        Log.d("mainActivity:","יששששששששששששששששששששש")
//
//    }
}


