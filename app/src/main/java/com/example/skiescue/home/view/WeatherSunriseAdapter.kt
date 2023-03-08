package com.example.skiescue.home.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.skiescue.databinding.ItemCardGridSunriseSunsetBinding
import com.example.skiescue.model.Daily
import com.example.skiescue.model.SunRise

class WeatherSunriseAdapter ( //private val fragment: Fragment,

                             ) : RecyclerView.Adapter<WeatherSunriseAdapter.ViewHolder>() {
    private var sunrise = emptyList<SunRise>()
    //private var sunset = emptyList<SunRise>()

    fun setSunrise(sunRise: List<SunRise>) {
        this.sunrise = sunRise
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherSunriseAdapter.ViewHolder {
        return ViewHolder(
            // here my item
            ItemCardGridSunriseSunsetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherSunriseAdapter.ViewHolder, position: Int) {
        //holder.binding.txtTime.text= sunrise[position].dt
        /*  convertLongToDay(weatherDays[position + 1].dt, fragment.requireContext())
        holder.binding.txtOneDes.text = weatherDays[position].weather[0].description
        holder.binding.txtOneHistory.text =
            convertToDate(weatherDays[position + 1].dt, fragment.requireContext())
        var temNumber = ""
        temNumber = if (getCurrentLan(fragment.requireContext()) == "ar") {
            convertNumbersToArabic(weatherDays[position + 1].temp!!.day!!)
        } else {
            (weatherDays[position + 1].temp!!.day ?: 0).toString()
        }
        holder.binding.txtOneTemp.text = temNumber.plus(" ")
            .plus(
                getCurrentTemperature(fragment.requireContext())
            )
        holder.binding.imgOne.setImageResource(getIconImage(weatherDays[position + 1].weather[0].icon!!))*/
    }

    override fun getItemCount(): Int {
        return sunrise.size
    }

    inner class ViewHolder(
        val binding: ItemCardGridSunriseSunsetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}