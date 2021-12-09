package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast


class HomePageActivity : AppCompatActivity() {
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val spnArea=findViewById<Spinner>(R.id.spnArea)
        val l : MutableList<String> = ArrayList()

        l.add("צפון")
        l.add("מרכז")
        l.add("יהודה  ושומרון")
        l.add("דרום")
        var adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,l)
        spnArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = l[position]
                Toast.makeText(this@HomePageActivity,"$item selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?){}
        }


        spnArea.adapter=adapter
        limitDropDownHeight(spnArea)

        searchView = findViewById(R.id.searchView)
        listView = findViewById(R.id.listView)
        list = ArrayList()
        list.add("Apple")
        list.add("Banana")
        list.add("Pineapple")
        list.add("Orange")
        list.add("Mango")
        list.add("Grapes")
        list.add("Lemon")
        list.add("Melon")
        list.add("Watermelon")
        list.add("Papaya")
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(this@HomePageActivity, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

    }
    fun limitDropDownHeight(spnArea: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true

        val popupWindow : ListPopupWindow =popup.get(spnArea) as ListPopupWindow
        popupWindow.height = (200 * resources.displayMetrics.density).toInt()
    }
}