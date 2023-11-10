package com.cultivaet.hassad.ui.main.tasks.visits

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

@SuppressLint("NotifyDataSetChanged")
class VisitsAdapter(
    var mList: MutableList<VisitDataItem> = mutableListOf(),
) : RecyclerView.Adapter<VisitsAdapter.ViewHolder>() {

    fun setItems(visits: List<VisitDataItem>) {
        mList = visits.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.visit_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = mList[position]
        holder.apply {
            title.text = task.title
            text.text =
                "نوع الزيارة: " + if (task.visitType == "fv") "زيارة ميدانية" else "مدرسة المزارعين الحقلية"
            from.text = "من : ${task.from}"
            to.text = "إلى : ${task.to}"
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val from: TextView = itemView.findViewById(R.id.from)
        val to: TextView = itemView.findViewById(R.id.to)
    }
}