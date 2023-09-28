package com.cultivaet.hassad.ui.main.content

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

@SuppressLint("NotifyDataSetChanged")
class CommentsAdapter(
    private val context: Context,
    internal var mList: List<CommentDataItem> = mutableListOf(),
    private val viewMedia: (position: Int) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    fun setItems(farmers: List<CommentDataItem>) {
        mList = farmers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = mList[position]
        holder.apply {
            title.text = task.text
            text.text =
                "${context.getString(R.string.farmer_name)}: ${task.farmerFirstName} ${task.farmerLastName}"
        }
        holder.showMedia.setOnClickListener {
            Log.d("Amrrr", "onBindViewHolder: ${task.base64}")
            viewMedia.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val showMedia: Button = itemView.findViewById(R.id.show_media)
    }
}