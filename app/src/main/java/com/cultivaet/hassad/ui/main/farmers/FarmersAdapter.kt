package com.cultivaet.hassad.ui.main.farmers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R
import com.cultivaet.hassad.domain.model.remote.responses.Farmer

class FarmersAdapter(
    private var mList: List<Farmer>
) : RecyclerView.Adapter<FarmersAdapter.ViewHolder>() {

    fun setItems(farmers: List<Farmer>) {
        mList = farmers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.farmer_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val farmer = mList[position]
        holder.apply {
            name.text = farmer.firstName
            phone.text = farmer.phoneNumber
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = itemView.findViewById(R.id.name)
        val phone: TextView = itemView.findViewById(R.id.phone)
    }
}