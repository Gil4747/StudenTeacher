package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Spinner

class ActivityRegister : AppCompatActivity() {
    private val profession = hashMapOf<TextView, Array<TextView>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btnRegister=findViewById<TextView>(R.id.btnRegister)
        val editUsername=findViewById<TextView>(R.id.editUsername)
        val editEmail=findViewById<TextView>(R.id.editEmail)
        val editCPassword=findViewById<TextView>(R.id.editCPassword)
        val editPassword=findViewById<TextView>(R.id.editPassword)
        val btnTeach=findViewById<Button>(R.id.btnTeach)
        val editAge=findViewById<TextView>(R.id.editAge)
        val editGender=findViewById<TextView>(R.id.editGender)
        val spnTest=findViewById<Spinner>(R.id.spnTest)



        val list : MutableList<String> = ArrayList()

        list.add("אזור")
        list.add("צפון")
        list.add("מרכז")
        list.add("יהודה  ושומרון")
        list.add("דרום")

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,list)
        spnTest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            val item = list[position]
                Toast.makeText(this@ActivityRegister,"$item selected",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?){}
        }


        spnTest.adapter=adapter
        limitDropDownHeight(spnTest)



        btnTeach.setOnClickListener {
            val intent= Intent(this,WhatIAmTeaching::class.java)
            startActivity(intent)
            }
        btnRegister.setOnClickListener {
           if(editUsername.text.trim().isNotEmpty() || editEmail.text.isNotEmpty() || editCPassword.text.isNotEmpty() ||  editPassword.text.isNotEmpty()){
//               val teach=Teacher(editUsername.toString(),editAge.toString(),editGender.toString())
               Toast.makeText(this, "Input provided", Toast.LENGTH_SHORT).show()
           }
            else{
               Toast.makeText(this, "Input required", Toast.LENGTH_SHORT).show()
           }

//            if(editTeach.text.isNotEmpty()){
//                var teachers = arrayOf(editUsername)
//                teachers=append(teachers,editUsername)
//                profession.put(editTeach,teachers)
//            }

        }

    }
    fun limitDropDownHeight(spnTest: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true

        val popupWindow :ListPopupWindow =popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (200 * resources.displayMetrics.density).toInt()
    }
    fun append(arr: Array<TextView>, element: TextView): Array<TextView> {
        val list: MutableList<TextView> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

}