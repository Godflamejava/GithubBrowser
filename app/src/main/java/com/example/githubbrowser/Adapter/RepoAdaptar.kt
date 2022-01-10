package com.example.githubbrowser.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.githubbrowser.Activity.MainActivity
import com.example.githubbrowser.Activity.RepoDetailActivity
import com.example.githubbrowser.R
import org.json.JSONException

class RepoAdaptar(private val mList: List<String>,private val context: Context) : RecyclerView.Adapter<RepoAdaptar.ViewHolder>() {

    // create new views
    private var link:String=""
    private var description:String=""
    private val activity : MainActivity = context as MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_layout, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   val repoData = mList[position]
        val strs = repoData.split(":").toTypedArray()
        jsonParse( strs[0],strs[1],holder.repoDescription )
        holder.repoName.text = strs[1]

        if(description.isEmpty())
            description="No Description Available"

        holder.repoDescription.text = description

        holder.share.setOnClickListener{
            val sendIntent = Intent(Intent.ACTION_SEND)
           sendIntent.putExtra(Intent.EXTRA_TEXT,strs[1]+" "+ description+" "+link)
           sendIntent.type = "text/plain"
           sendIntent.putExtra(Intent.EXTRA_TITLE, "Share the link of the password Image")
           context.startActivity(Intent.createChooser(sendIntent, null))
       }

        holder.repoDetail.setOnClickListener{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefFile",
                Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("currentRepo",repoData)
            editor.apply()
            val intent = Intent (context, RepoDetailActivity::class.java)
            context.startActivity(intent)
            activity.finish()


        }

    }




    private fun jsonParse(owner:String, repoName:String ,repoDescription:TextView){
        val requestQueue = Volley.newRequestQueue(context)
        val syntax="https://api.github.com/repos"
        val url= "$syntax/$owner/$repoName"
        Log.i("maryritiki",url)


        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
                    link = response.getString("html_url")
            description=response.getString("description")
            if(description.equals("null"))
                description="No Description Available"
            else
            repoDescription.text = description
                }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue.add(request)
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
       val repoName: TextView =itemView.findViewById(R.id.repo_name)
        val repoDescription: TextView =itemView.findViewById(R.id.repo_description)
        val share:ImageView=itemView.findViewById(R.id.share)
        val repoDetail:ConstraintLayout=itemView.findViewById(R.id.repo_detail)

    }
}
