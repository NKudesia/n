package com.example.healthgates.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.TimeSlot
import com.example.healthgates.databinding.AdapterTimeSlotBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class TimeSlotAdapter(private val listener: TimeSlotInterface) : RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder>() {

    private val itemList = ArrayList<TimeSlot>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val dateFormat2 = SimpleDateFormat("dd-MM-yyyy")
    private val dateFormat3 = SimpleDateFormat("HH:mm a")
    private var currentIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            AdapterTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val viewHolder = MyViewHolder(viewBinding)
//        viewBinding.root.setOnClickListener {
//            listener.onViewClick(itemList[viewHolder.bindingAdapterPosition])
//        }
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        val date = dateFormat.parse(currentItem.from_slot)
        with(holder.viewBinding){
//            ivDoctor.setImageResource(currentItem.image)
//            tvSpeciality.text = currentItem.textSpeciality
//            tvDate.text = dateFormat2.format(date)
//            tvTime.text = SimpleDateFormat("KK: mm a").format(date)
            tvTime.text = dateFormat3.format(date)


            if(currentIndex == position){
                layout1.setBackgroundResource(R.drawable.booking_slot_selected)
            }else if(currentItem.remaining_limit > 0){

                tvStatus.text = "Available"
                layout1.setBackgroundResource(R.drawable.booking_slot_available)
            }else{
                tvStatus.text = "Booked"
                layout1.setBackgroundResource(R.drawable.booking_slot_unavailable)
            }

            layout1.setOnClickListener(){
                if(currentItem.remaining_limit > 0){
                    currentIndex = position
                    notifyDataSetChanged()
                    listener.onViewClick(currentItem)
                }
            }

        }

//        holder.itemView.setOnClickListener {
//            listener.onViewClick(itemList[position])
//        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null) itemList.size else 0
    }

    fun updateList(updatedList: List<TimeSlot>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterTimeSlotBinding) : RecyclerView.ViewHolder(viewBinding.root)
}


interface TimeSlotInterface {
    fun onViewClick(timeSlot: TimeSlot)
}