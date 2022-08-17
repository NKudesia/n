package com.example.healthgates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.ProfileItem
import com.example.healthgates.databinding.AdapterProfileBinding

class ProfileScreenAdapter : RecyclerView.Adapter<ProfileScreenAdapter.MyViewHolder>() {

    private val itemList = ArrayList<ProfileItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        AdapterProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding) {
            tvValue.text = currentItem.value
            tvType.text = currentItem.type
//            if (currentItem.sideDividerVisible) {
//                divider2.setBackgroundColor(ContextCompat.getColor(root.context, R.color.line_color))
//            } else {
//                divider2.setBackgroundColor(ContextCompat.getColor(root.context, android.R.color.transparent))
//            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(updatedList: List<ProfileItem>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterProfileBinding) : RecyclerView.ViewHolder(viewBinding.root)

}