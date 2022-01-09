package com.example.githubbrowser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class CommitsActivity : AppCompatActivity() {
    private var list:ArrayList<Commits> = ArrayList()
    private lateinit var adapter:CommitAdapter
    private  lateinit var pgbar:ProgressBar
    private  lateinit var brachNameTitle:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commits)
        val recyclerView:RecyclerView=findViewById(R.id.recycleView)
        val  back=findViewById<ImageView>(R.id.close)
        back.setOnClickListener{
            val intent = Intent (this,MainActivity::class.java);
            startActivity(intent)
            this.finish()

        }
        brachNameTitle=findViewById(R.id.branchName)
         pgbar=findViewById(R.id.pdbar)
        pgbar.visibility= View.INVISIBLE

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommitAdapter(list,this)
        recyclerView.adapter=adapter
        getCommitList()
    }

    private fun getCommitList(){
        pgbar.visibility= View.VISIBLE

        val requestQueue = Volley.newRequestQueue(this)
        val syntax1="https://api.github.com/repos"
        val syntax2="/commits?sha="

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoData = sharedPreferences.getString("currentRepo", "")
        val strs = repoData?.split(":")?.toTypedArray()
        val owner= strs?.get(0)
        val repoName=strs?.get(1)
        val branch =sharedPreferences.getString("branchname", "")
        brachNameTitle.setText(branch)

        val url= "$syntax1/$owner/$repoName$syntax2$branch"
        Log.i("maryritiki",url)


        val request = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->try {
            for (i in 0 until response.length())
            {
                val jsonobject = response.getJSONObject(i)
                val commit=jsonobject.getJSONObject("commit")
                val author=commit.getJSONObject("author")
                val date=author.getString("date")
                val username=author.getString("name")
                val message= commit.getString("message")
                val author2=jsonobject.getJSONObject("author")
                val avatar_url=author2.getString("avatar_url")

                val newCommit=Commits(date,message,username,avatar_url)
                list.add(newCommit)
                Log.i("maryritiki","ff"+newCommit)

            }
            pgbar.visibility= View.INVISIBLE

            Log.i("maryRitiki","ll"+list.size)
            if(list.size==0)
                Toast.makeText(this,"Cannot Display Too many commits!",Toast.LENGTH_LONG);

            adapter.notifyDataSetChanged()

        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)

    }
}