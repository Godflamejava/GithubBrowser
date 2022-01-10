package com.example.githubbrowser.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.githubbrowser.R
import org.json.JSONException

class AddRepo : AppCompatActivity() {
    private lateinit var repoName:String
    private lateinit var owner:String
    private lateinit var etowner:EditText
    private lateinit var etreponame:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_repo)
       etowner=findViewById<EditText>(R.id.et_owner)
         etreponame=findViewById<EditText>(R.id.et_reponame)

       val  back=findViewById<ImageView>(R.id.iv_back)
        back.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java);
            startActivity(intent)
            this.finish()

        }


        val add=findViewById<Button>(R.id.add)
        add.setOnClickListener{
            jsonParse()
        }

    }

    @SuppressLint("MutatingSharedPrefs")
    fun jsonParse(){
        val requestQueue = Volley.newRequestQueue(this)
        val syntax="https://api.github.com/repos"
        repoName=etreponame.text.toString()
        owner=etowner.text.toString()
        val url= "$syntax/$owner/$repoName"
        Log.i("ritik",url)


        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {

            val sharedPreferences: SharedPreferences = this.getSharedPreferences("sharedPrefFile",
                Context.MODE_PRIVATE)
            val repoSet = sharedPreferences.getStringSet("repoSet", mutableSetOf())
            repoSet?.add("$owner:$repoName")

            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putStringSet("repoSet",repoSet)
            editor.apply()
            Toast.makeText(this,"Added Successfully!",Toast.LENGTH_LONG ).show()
            val intent = Intent (this, MainActivity::class.java);
            startActivity(intent)
            finish()
                }
        catch (e: JSONException) {
            Toast.makeText(this,"Error Occured!",Toast.LENGTH_LONG ).show()

            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
}