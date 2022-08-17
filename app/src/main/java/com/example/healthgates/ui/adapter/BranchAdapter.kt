package com.example.healthgates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.data.models.Branch
import com.example.healthgates.databinding.AdapterBranchBinding
import com.example.healthgates.utils.loadImage

class BranchAdapter(private val listener: BranchInterface) : RecyclerView.Adapter<BranchAdapter.MyViewHolder>() {

    private val itemList = ArrayList<Branch>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding = AdapterBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = MyViewHolder(viewBinding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding) {

//            ivMaps.loadImage(currentItem.branch_logo)
            tvName.text = currentItem.name
            tvNameArabic.text = currentItem.arabic_name
            tvAddress.text = currentItem.address

            bookBtn.setOnClickListener(){
                listener.onViewClick(currentItem, 0)
            }

            callBtn.setOnClickListener(){
                listener.onViewClick(currentItem, 1)
            }

            locationBtn.setOnClickListener(){
                listener.onViewClick(currentItem, 2)
            }

            ivMaps.setOnClickListener(){
                listener.onViewClick(currentItem, 2)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(updatedList: List<Branch>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterBranchBinding) : RecyclerView.ViewHolder(viewBinding.root)
}

interface BranchInterface{
    fun onViewClick(branch: Branch, type: Int)
}

