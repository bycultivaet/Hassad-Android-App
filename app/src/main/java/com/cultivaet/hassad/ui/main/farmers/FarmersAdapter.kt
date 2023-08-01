package com.cultivaet.hassad.ui.main.farmers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

class FarmersAdapter(
    private val context: Context,
    private var mList: List<FarmerDataItem>
) : RecyclerView.Adapter<FarmersAdapter.ViewHolder>() {

    private val expandedHashSet: HashSet<Int> = hashSetOf()

    fun setItems(farmers: List<FarmerDataItem>) {
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
            name.text = "${context.getString(R.string.name)}: ${farmer.fullName}"
            phone.text = "${context.getString(R.string.phone_number)}: ${farmer.phoneNumber}"
            address.text = "${context.getString(R.string.address)}: ${farmer.address}"
            age.text = "${context.getString(R.string.age)}: ${farmer.age}"
            cropType.text = "${context.getString(R.string.cropType)}: ${farmer.cropType}"
            cropsHistory.text =
                "${context.getString(R.string.previouslyGrownCrops)}: ${farmer.cropsHistory}"
            possessionType.text =
                "${context.getString(R.string.possessionType)}: ${farmer.ownership}"
            zeroDay.text = "${context.getString(R.string.zeroDay)}: ${farmer.zeroDay}"
            landArea.text = "${context.getString(R.string.landArea)}: ${farmer.landArea}"
        }

        holder.restOfFarmerDataLinearLayout.visibility =
            if (expandedHashSet.contains(farmer.id)) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            if (expandedHashSet.contains(farmer.id))
                expandedHashSet.remove(farmer.id)
            else
                expandedHashSet.add(farmer.id)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restOfFarmerDataLinearLayout: LinearLayout =
            itemView.findViewById(R.id.restOfFarmerData)
        val name: TextView = itemView.findViewById(R.id.name)
        val phone: TextView = itemView.findViewById(R.id.phone)
        val address: TextView = itemView.findViewById(R.id.address)
        val age: TextView = itemView.findViewById(R.id.age)
        val cropType: TextView = itemView.findViewById(R.id.cropType)
        val cropsHistory: TextView = itemView.findViewById(R.id.cropsHistory)
        val possessionType: TextView = itemView.findViewById(R.id.possessionType)
        val zeroDay: TextView = itemView.findViewById(R.id.zeroDay)
        val landArea: TextView = itemView.findViewById(R.id.landArea)
    }
}