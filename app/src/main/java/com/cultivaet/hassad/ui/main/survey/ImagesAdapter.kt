package com.cultivaet.hassad.ui.main.survey

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R

@SuppressLint("NotifyDataSetChanged")
class ImagesAdapter(
    private var mList: MutableList<Bitmap> = mutableListOf(),
    private val addImage: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setItem(bitmap: Bitmap) {
        mList.add(0, bitmap)
        notifyDataSetChanged()
    }

    fun setItems(bitmaps: MutableList<Bitmap>) {
        mList = bitmaps
        notifyDataSetChanged()
    }

    fun getItems() = mList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADDING_IMAGE -> AddingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.add_image_item, parent, false)
            )

            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddingViewHolder) {
            holder.addImage.setOnClickListener { addImage.invoke() }
        } else {
            val bitmap = mList[position]

            (holder as ViewHolder).apply {
                image.setImageBitmap(bitmap)
            }

            holder.remove.setOnClickListener {
                mList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size - 1) ADDING_IMAGE else NORMAL_IMAGE
    }

    override fun getItemCount(): Int {
        return if (mList.size < 4) mList.size else 4
    }

    class AddingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addImage: ImageView = itemView.findViewById(R.id.addImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val remove: ImageView = itemView.findViewById(R.id.remove)
    }

    companion object {
        private const val NORMAL_IMAGE = 0
        private const val ADDING_IMAGE = 1
    }
}