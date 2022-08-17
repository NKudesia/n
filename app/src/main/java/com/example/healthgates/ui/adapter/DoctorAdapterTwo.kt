package com.example.healthgates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.AdapterDoctorTwoBinding
import com.example.healthgates.utils.loadImage

class DoctorAdapterTwo(private val listener: DoctorsInterface) : RecyclerView.Adapter<DoctorAdapterTwo.MyViewHolder>() {

    private val itemList = ArrayList<Doctor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding = AdapterDoctorTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = MyViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            listener.onViewClick(itemList[viewHolder.bindingAdapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding) {
            doctorProfileImg.loadImage(currentItem.profileImg)
            tvDoctorName.text = currentItem.name
            tvSpecialist.text = String.format(root.context.getString(R.string.specialist), currentItem.speciality.name)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(updatedList: List<Doctor>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterDoctorTwoBinding) : RecyclerView.ViewHolder(viewBinding.root)

}
