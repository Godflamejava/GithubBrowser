package com.example.githubbrowser.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubbrowser.R
import com.example.githubbrowser.Adapter.RepoAdaptar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoSet = sharedPreferences.getStringSet("repoSet", mutableSetOf())
        val emptyScreen = findViewById<ConstraintLayout>(R.id.empty_screen)
        val recyclerView=findViewById<RecyclerView>(R.id.recycleView)

        if(repoSet?.isEmpty() == true)
        {
            emptyScreen.visibility=View.VISIBLE
            recyclerView.visibility=View.INVISIBLE
        }
        else
        {
            emptyScreen.visibility=View.INVISIBLE
            recyclerView.visibility=View.VISIBLE
        }

        val ivAddMore = findViewById<ImageView>(R.id.iv_add_more)
        val addMore = findViewById<Button>(R.id.add_more)
        ivAddMore.setOnClickListener{
            addMoreFunction()
        }
        addMore.setOnClickListener{
            addMoreFunction()
        }
        val list: List<String> = repoSet?.toList() as List<String>
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RepoAdaptar(list,this)
        recyclerView.adapter=adapter

    }
  private fun addMoreFunction(){
        val intent = Intent (this, AddRepo::class.java)
      startActivity(intent)
      finish()
    }
}