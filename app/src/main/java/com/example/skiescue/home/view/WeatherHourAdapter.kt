package com.example.skiescue.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.skiescue.databinding.ItemCardHourlyBinding
import com.example.skiescue.model.*

class WeatherHourAdapter(
    private val fragment: Fragment,

    ) : RecyclerView.Adapter<WeatherHourAdapter.ViewHolder>() {
    private var weatherHours = emptyList<Hourly>()
    fun setWeatherHours(weatherHours: List<Hourly>) {
        this.weatherHours = weatherHours
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHourAdapter.ViewHolder {
        return ViewHolder(
            ItemCardHourlyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherHourAdapter.ViewHolder, position: Int) {
        /*holder.binding.txtWeatHour.text =
            convertToTime(weatherHours[position + 1].dt, fragment.requireContext())*/

        holder.binding.txtWeatHour.text =
            timestampToReadableTime(weatherHours[position + 1].dt)

        var temNumber = ""
        temNumber = if (getCurrentLan(fragment.requireContext()) == "ar") {
            convertNumbersToArabic(weatherHours[position + 1].temp!!)
        } else {
            (weatherHours[position + 1].temp!!).toString()
        }
        holder.binding.txtWeatherTemp.text = temNumber.plus(" ").
        plus(
            getCurrentTemperature(fragment.requireContext())
        )
        //holder.binding.imgWeaHour.setImageResource(getIconImage(weatherHours[position + 1].weather[0].icon!!))
        holder.binding.imgWeaHour.setAnimation(getIconImage(weatherHours[position + 1].weather[0].icon!!))
    }

    override fun getItemCount(): Int {
        return weatherHours.size - 1
    }

    inner class ViewHolder(
        val binding: ItemCardHourlyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}
