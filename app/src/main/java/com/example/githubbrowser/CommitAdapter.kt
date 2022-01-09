package com.example.githubbrowser

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class CommitAdapter(private val commitList: List<Commits>,private val context: Context) : RecyclerView.Adapter<CommitAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.commit_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commit= commitList[position]
        holder.date.text = commit.date
        holder.message.text = commit.message
        holder.commiter.text = commit.userName
        Picasso.get().load(commit.imageUrl).into(holder.pic)
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        Log.i("maryRitiki","gg "+commitList.size)
        return commitList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val date: TextView =itemView.findViewById(R.id.date)
        val pic: CircleImageView =itemView.findViewById(R.id.pic)
        val commiter: TextView =itemView.findViewById(R.id.commiter)
        val message: TextView =itemView.findViewById(R.id.message)



    }
}