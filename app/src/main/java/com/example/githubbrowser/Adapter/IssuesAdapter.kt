package com.example.githubbrowser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubbrowser.R
import com.squareup.picasso.Picasso


class IssuesAdapter(private val titleList: List<String>,private val imageList: List<String>,private val issueCretorList: List<String>,private val context: Context) : RecyclerView.Adapter<IssuesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.issues_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = titleList[position]
        val  image= imageList[position]
        val creator=issueCretorList[position]
       holder.issueTitle.setText(title)
       Picasso.get().load(image).into(holder.imageUser)
        holder.issueCreator.setText(creator)
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return titleList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val issueTitle: TextView =itemView.findViewById(R.id.issue)
        val imageUser:ImageView=itemView.findViewById(R.id.pic)
        val issueCreator: TextView =itemView.findViewById(R.id.issue_creator)


    }
}