package com.example.githubbrowser.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.githubbrowser.Adapter.MyAdapter
import com.example.githubbrowser.R
import com.google.android.material.tabs.TabLayout
import org.json.JSONException


class RepoDetailActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
     var numberOfIssues=0;
    lateinit var repoName2:TextView
    lateinit var description:TextView
    lateinit var link:String

    @SuppressLint("MutatingSharedPrefs")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)
        val delete:ImageView=findViewById(R.id.iv_delete)
        val browser:ImageView=findViewById(R.id.iv_browser)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoData = sharedPreferences.getString("currentRepo", "")
        val repoSet=sharedPreferences.getStringSet("repoSet",mutableSetOf())
        val  back=findViewById<ImageView>(R.id.close)

        back.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java);
            startActivity(intent)
            this.finish()

        }





        delete.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Do you really want to whatever?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        Toast.makeText(
                            this,
                            "Deleted succesfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        repoSet?.remove(repoData)
                        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putStringSet("repoSet",repoSet)
                        editor.putString("currentRepo","")
                        editor.apply()
                        val intent = Intent (this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    })
                .setNegativeButton(android.R.string.no, null).show()








        }
        browser.setOnClickListener{



            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(link)
            startActivity(i)
        }


        repoName2=findViewById(R.id.repo_name)
        description=findViewById(R.id.repo_description)
        val strs = repoData?.split(":")?.toTypedArray()
        val owner= strs?.get(0)
        val repoName=strs?.get(1)
        if (owner != null) {
            if (repoName != null) {
                getDescription(owner, repoName )
            }
        }


        jsonParse()
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("BRANCHES"))
        tabLayout.addTab(tabLayout.newTab().setText("ISSUES($numberOfIssues)"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(this, supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun jsonParse(){
        val requestQueue = Volley.newRequestQueue(this)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoData = sharedPreferences.getString("currentRepo", "")
        val strs = repoData?.split(":")?.toTypedArray()
        val owner= strs?.get(0)
        val repoName=strs?.get(1)
        repoName2.setText(repoName)
        val syntax1="https://api.github.com/repos"
        val syntax2="/issues?state=open"
        val url= "$syntax1/$owner/$repoName$syntax2"
        Log.i("maryritiki3",url)
        val request = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->try {
            numberOfIssues=response.length()
            tabLayout.getTabAt(1)?.text = "ISSUES($numberOfIssues)"
            Log.i("maryritiki3",""+numberOfIssues)

        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
    private fun getDescription(owner:String, repoName:String ){
        val requestQueue = Volley.newRequestQueue(this)
        val syntax="https://api.github.com/repos"
        val url= "$syntax/$owner/$repoName"
        Log.i("maryritiki",url)


        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
                  link = response.getString("html_url")

            if(response.getString("description").equals("null"))
                        description.setText("No Description Available")
            else
                description.setText(response.getString("description"))

        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }





}