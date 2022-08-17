package com.example.healthgates.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.Appointment
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.data.models.LabTest
import com.example.healthgates.data.models.Prescription
import com.example.healthgates.databinding.AdapterLabTestBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat

class PrescriptionAdapter(private val context: Context, private val listener: PrescriptionInterface) : RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder>() {

    private val itemList = ArrayList<Prescription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            AdapterLabTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding){

            tvTestName.text = "Prescription: " + currentItem.prescription
            tvDate.text = "Date: " + currentItem.date
            tvStatus.text = "Prescribing Doctor: " + currentItem.physician

//            if(currentItem.status == "Done"){
//                layout1.setCardBackgroundColor(context.getColor(R.color.dark_green))
//            }else{
//                layout1.setCardBackgroundColor(context.getColor(R.color.brown_card_background_color))
//            }

            layout1.setOnClickListener(){
                listener.onViewClick(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null) itemList.size else 0
    }

    fun updateList(updatedList: List<Prescription>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterLabTestBinding) : RecyclerView.ViewHolder(viewBinding.root)
}

interface PrescriptionInterface{
    fun onViewClick(prescription: Prescription)
}

