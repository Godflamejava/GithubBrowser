package com.example.githubbrowser.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.githubbrowser.Activity.CommitsActivity
import com.example.githubbrowser.Activity.RepoDetailActivity
import com.example.githubbrowser.R

class BranchAdapter(private val mList: List<String>,private val context: Context) : RecyclerView.Adapter<BranchAdapter.ViewHolder>() {
    private val activity : RepoDetailActivity = context as RepoDetailActivity

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.branch_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val repoData = mList[position]
        holder.brandName.setText(repoData)
       holder.listItem.setOnClickListener{
           val sharedPreferences: SharedPreferences = context.getSharedPreferences("sharedPrefFile",
               Context.MODE_PRIVATE)
           val editor: SharedPreferences.Editor =  sharedPreferences.edit()
           editor.putString("branchname",holder.brandName.text.toString())
           editor.apply()
           val i = Intent(context, CommitsActivity::class.java)
           context.startActivity(i)
           activity.finish()

       }


    }





    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
       val brandName: TextView =itemView.findViewById(R.id.brand_name)
        val listItem:ConstraintLayout=itemView.findViewById(R.id.list_item)
    }
}