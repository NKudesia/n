package com.example.healthgates.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.data.models.HomeScreenItem
import com.example.healthgates.databinding.AdapterHomeBinding

class HomeAdapter(val listener: HomeInterface) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    private val itemList = ArrayList<HomeScreenItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding = AdapterHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = MyViewHolder(viewBinding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding) {
            root.backgroundTintList = ContextCompat.getColorStateList(root.context, currentItem.backgroundColor)
            taskImage.setImageResource(currentItem.image)
            tvTask.setTextColor(Color.parseColor(currentItem.textColor))
            tvTask.text = currentItem.text

            holder.itemView.setOnClickListener(){
                listener.onViewClick(position)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(updatedList: List<HomeScreenItem>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterHomeBinding) : RecyclerView.ViewHolder(viewBinding.root)
}

interface HomeInterface{
    fun onViewClick(position: Int)
}

