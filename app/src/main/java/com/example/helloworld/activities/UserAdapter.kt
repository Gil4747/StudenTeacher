package com.example.helloworld.activities

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworld.R
import com.example.helloworld.models.User

class UserAdapter(val context: TeacherFiltering, val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name
        holder.textAge.text = currentUser.age.toString()
        holder.textPrice.text = currentUser.price.get(HomePageActivity.profession).toString()
        holder.textMobile.text = currentUser.mobile.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName= itemView.findViewById<TextView>(R.id.tvName)
        val textPrice= itemView.findViewById<TextView>(R.id.tvPrice)
        val textMobile= itemView.findViewById<TextView>(R.id.tvMobile)
        val textAge= itemView.findViewById<TextView>(R.id.tvAge)
    }
}