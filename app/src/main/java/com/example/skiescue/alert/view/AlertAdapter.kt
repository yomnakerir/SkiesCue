package com.example.skiescue.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.skiescue.databinding.ItemCardAlertBinding
import com.example.skiescue.model.convertToDate
import com.example.skiescue.model.convertToTime

class AlertAdapter (

    private val fragment: Fragment,
    private val onDelete: (model: AlertModel) -> Unit
    ) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
        private var weatherAlerts = emptyList<AlertModel>()
        fun setWeatherAlerts(weatherAlert: List<AlertModel>) {
            this.weatherAlerts = weatherAlert
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AlertAdapter.ViewHolder {
            return ViewHolder(
                ItemCardAlertBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: AlertAdapter.ViewHolder, position: Int) {
            holder.binding.txtFrom.text =
                getText(weatherAlerts[position].startTime!!, weatherAlerts[position].startDate!!)
            holder.binding.txtTo.text =
                getText(weatherAlerts[position].endTime!!, weatherAlerts[position].endDate!!)


            // handle delete
            holder.binding.imgDelete.setOnClickListener {
                onDelete(weatherAlerts[position])
            }
        }

        override fun getItemCount(): Int {
            return weatherAlerts.size
        }

        inner class ViewHolder(
            val binding: ItemCardAlertBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        }

        private fun getText(time: Long, day: Long): String {
            return convertToDate(day, fragment.requireContext()).plus("\n")
                .plus(convertToTime(time, fragment.requireContext()))
        }


    }