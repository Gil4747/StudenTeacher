package com.example.helloworld.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.helloworld.R

class Spinner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)


        val list : MutableList<String> = ArrayList()

        for (i in 1..1040)
            list.add("Item $i")

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,list)

        val spnTest=findViewById<Spinner>(R.id.spnTest)
        spnTest.adapter=adapter
    }
}