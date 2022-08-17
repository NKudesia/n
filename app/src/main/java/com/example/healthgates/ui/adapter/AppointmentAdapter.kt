package com.example.healthgates.ui.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthgates.data.models.Appointment
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.databinding.AdapterAppointmentBinding
import com.example.healthgates.utils.loadImage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class AppointmentAdapter(private val listener: AppointmentInterface) : RecyclerView.Adapter<AppointmentAdapter.MyViewHolder>() {

    private val itemList = ArrayList<Appointment>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormat2 = SimpleDateFormat("dd-MM-yyyy")
    private val dateFormat3 = SimpleDateFormat("hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            AdapterAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding){
//            doctorProfileImg.loadImage(currentItem.physician_image)
            Glide.with(holder.itemView)
                .load(currentItem.physician_image)
                .into(doctorProfileImg)

            val date = dateFormat.parse(currentItem.date)
            tvTime.text = dateFormat3.format(date)
            tvDate.text = dateFormat2.format(date)
            tvDoctorName.text = currentItem.physician_name
            tvStatus.text = currentItem.state

            if(currentItem.booked_online) {
                tvOnlineStatus.visibility = View.VISIBLE
            }else{
                tvOnlineStatus.visibility = View.INVISIBLE
            }

            holder.itemView.setOnClickListener(){
                listener.onViewClick(currentItem)
            }
        }
//
//        holder.viewBinding.setOnClickListener {
////            listener.onViewClick(itemList[position])
//        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null) itemList.size else 0
    }

    fun updateList(updatedList: List<Appointment>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterAppointmentBinding) : RecyclerView.ViewHolder(viewBinding.root)
}

interface AppointmentInterface{
    fun onViewClick(appointment: Appointment)
}

