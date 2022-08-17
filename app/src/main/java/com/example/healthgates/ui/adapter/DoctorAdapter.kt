package com.example.healthgates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.AdapterDoctorBinding
import com.example.healthgates.utils.loadImage

class DoctorAdapter(private val listener: DoctorsInterface) : RecyclerView.Adapter<DoctorAdapter.MyViewHolder>() {

    private val itemList = ArrayList<Doctor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            AdapterDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val viewHolder = MyViewHolder(viewBinding)
//        viewBinding.root.setOnClickListener {
//            listener.onViewClick(itemList[viewHolder.bindingAdapterPosition])
//        }
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding){
            ivDoctor.loadImage(currentItem.profileImg)
//            tvSpeciality.text = String.format(root.context.getString(R.string.specialist), currentItem.speciality.name)
            tvSpeciality.text = currentItem.speciality.name
            tvDoctorName.text = currentItem.name
        }

        holder.viewBinding.bookBtn.setOnClickListener {
            listener.onViewClick(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null) itemList.size else 0
    }

    fun updateList(updatedList: List<Doctor>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterDoctorBinding) : RecyclerView.ViewHolder(viewBinding.root)
}


interface DoctorsInterface {
    fun onViewClick(doctor: Doctor)
}