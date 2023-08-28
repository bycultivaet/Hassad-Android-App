package com.cultivaet.hassad.ui.main.tasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

@SuppressLint("NotifyDataSetChanged")
class TasksAdapter(
    private var mList: List<TaskDataItem> = listOf(),
    private val updateStatusByTaskId: (taskId: TaskDataItem) -> Unit
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    fun setItems(farmers: List<TaskDataItem>) {
        mList = farmers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = mList[position]
        holder.apply {
            title.text = task.title
            text.text = task.text
            from.text = task.from
            to.text = task.to
        }

        holder.checkIcon.setImageResource(if (task.isChecked) R.drawable.ic_checked else R.drawable.ic_unchecked)

        holder.checkIcon.setOnClickListener {
            task.isChecked = !task.isChecked
            updateStatusByTaskId.invoke(task)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkIcon: ImageView = itemView.findViewById(R.id.checkIcon)
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val from: TextView = itemView.findViewById(R.id.from)
        val to: TextView = itemView.findViewById(R.id.to)
    }
}