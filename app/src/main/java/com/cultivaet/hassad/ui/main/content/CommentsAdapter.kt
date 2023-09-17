package com.cultivaet.hassad.ui.main.content

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

@SuppressLint("NotifyDataSetChanged")
class CommentsAdapter(
    private var mList: List<CommentDataItem> = listOf(),
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    fun setItems(farmers: List<CommentDataItem>) {
        mList = farmers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = mList[position]
        holder.apply {
            title.text = task.text
            text.text = task.text
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
    }
}