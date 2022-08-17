package com.example.healthgates.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthgates.R
import com.example.healthgates.data.models.Appointment
import com.example.healthgates.data.models.Doctor
import com.example.healthgates.data.models.LabTest
import com.example.healthgates.databinding.AdapterLabTestBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat

class LabTestAdapter(private val context: Context, private val listener: LabTestInterface) : RecyclerView.Adapter<LabTestAdapter.MyViewHolder>() {

    private val itemList = ArrayList<LabTest>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            AdapterLabTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        with(holder.viewBinding){
//            if(currentItem.diagnosis.size > 0) {
//                tvTestName.text = "Test Name: " + currentItem.diagnosis[0].name
//                tvDate.text = "Date: + curr"
//                tvStatus.text = "Status:"
////                tvNormalRange.text = "Normal Range: " + currentItem.diagnosis[0].normal_range
////                tvResult.text = "Result: " + currentItem.diagnosis[0].result_type
//            }else{
//                tvTestName.text = "Test Name:"
//                tvDate.text = "Date:"
//                tvStatus.text = "Status:"
//            }

                tvTestName.text = "Test Name: " + currentItem.test
                tvDate.text = "Date:" + currentItem.date
                tvStatus.text = "Status:" + currentItem.status

            if(currentItem.status == "Done"){
                layout1.setCardBackgroundColor(context.getColor(R.color.dark_green))
            }else{
                layout1.setCardBackgroundColor(context.getColor(R.color.brown_card_background_color))
            }

//            holder.itemView.setOnClickListener(){
//                listener.onViewClick(currentItem)
//            }

            layout1.setOnClickListener(){
                listener.onViewClick(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (itemList != null) itemList.size else 0
    }

    fun updateList(updatedList: List<LabTest>) {
        itemList.clear()
        itemList.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewBinding: AdapterLabTestBinding) : RecyclerView.ViewHolder(viewBinding.root)
}

interface LabTestInterface{
    fun onViewClick(labTest: LabTest)
}

