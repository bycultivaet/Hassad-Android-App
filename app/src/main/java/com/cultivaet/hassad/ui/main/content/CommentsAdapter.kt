package com.cultivaet.hassad.ui.main.content

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.util.MediaType
import com.cultivaet.hassad.core.util.Utils

@SuppressLint("NotifyDataSetChanged")
class CommentsAdapter(
    private val context: Context,
    internal var mList: List<CommentDataItem> = mutableListOf(),
    private val viewMedia: (position: Int) -> Unit,
    private val answerTextById: (position: Int) -> Unit
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
            text.text = "${context.getString(R.string.farmer_name)}: ${task.farmerFirstName} ${task.farmerLastName}"
            answerText.text = "${context.getString(R.string.answer)}: ${task.answerText}"
            showMedia.setImageResource(
                if (Utils.getMediaType(task.mediaType).toString() == MediaType.IMAGE.toString()) {
                    R.drawable.ic_image
                } else {
                    R.drawable.ic_pdf
                }
            )
        }

        if (task.isExpandable) {
            holder.arrow.rotation = 180F
            holder.answerText.visibility = View.VISIBLE
        } else {
            holder.arrow.rotation = 0F
            holder.answerText.visibility = View.GONE
        }

        holder.showMedia.setOnClickListener { viewMedia.invoke(position) }

        holder.arrow.setOnClickListener { answerTextById.invoke(position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val answerText: TextView = itemView.findViewById(R.id.answerText)
        val showMedia: ImageView = itemView.findViewById(R.id.show_media)
        val arrow: ImageView = itemView.findViewById(R.id.ic_arrow)
    }
}