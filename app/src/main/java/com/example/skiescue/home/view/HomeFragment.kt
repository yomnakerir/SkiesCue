package com.example.skiescue.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.skiescue.R
import com.example.skiescue.databinding.FragmentHomeBinding
import com.example.skiescue.databinding.ItemCardGridSunriseSunsetBinding
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.home.viewmodel.HomeViewModel
import com.example.skiescue.home.viewmodel.HomeViewModelFactory
import com.example.skiescue.model.*
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_ID = 40

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: HomeViewModel
    lateinit var  temp  : TextView
    lateinit var feelsLike: TextView
    lateinit var city: TextView
    lateinit var date: TextView
    lateinit var weatherHourAdapter: WeatherHourAdapter
    lateinit var recyclerViewHour : RecyclerView
    lateinit var imageViewLottie : LottieAnimationView
    lateinit var weatherDayAdapter: WeatherDayAdapter
    lateinit var recyclerViewDaily : RecyclerView
    lateinit var sunriseTime : TextView
    lateinit var sunsetTime : TextView
    lateinit var wind: TextView
    lateinit var uvi: TextView
    lateinit var humidity: TextView


    companion object TimeOffest{
      var timeOffestValue = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding

        feelsLike = binding.txtFeelLikes
        temp = binding.txtTemp
        city = binding.txtCity
        date = binding.txtTime
        recyclerViewHour = binding.recyViewHourWeather
        imageViewLottie = binding.imgCurrent
        sunriseTime = binding.sunriseTime
        sunsetTime = binding.sunsetTime
        uvi = binding.uvindexTxt
        humidity = binding.humidityTxt
        wind = binding.windTxt

        return binding.root

        // catch data and display it (Test) retrofit
        /* val remote = RetrofitInstance().apiCall()
          val room = RoomDB.invoke(requireContext())

          val repository = Repository(requireContext())

          lifecycleScope.launch {
           val dataResponse =  async {  repository.getWeatherDetalis(30.668351057603832, 32.227911043392865)}
              if(dataResponse.await().isSuccessful){
                  val allData = dataResponse.await().body()
                  // catch data retrofit
                  Toast.makeText(requireContext(), allData.toString(), Toast.LENGTH_LONG).show()

                  // insert in database
                  allData?.let {
                      repository.insertFavourite(Favourite(weather = allData))
                  }

                  delay(3000)

                  // retrive from data base
                 val favouritesResponse = async{repository.getFavourites()}
                  Toast.makeText(requireContext(), "data in fav table : ${favouritesResponse.await().size}}", Toast.LENGTH_LONG).show()
                   delay(3000)

                  //delete from database
                  repository.deleteFavourite(favourite = favouritesResponse.await().get(0))
              }

           } */

    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

               initHourRecycle()
               intDayRecycle()

       // textView = view.findViewById(R.id.response)
       val repository = Repository(requireContext())
       val viewModelFactory = HomeViewModelFactory(repository =repository )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)

        // here i make  an observer
       //viewModel.getWeatherDetails(30.020847056268064, 31.1904858698064)

       // america
      viewModel.getWeatherDetails(-4.442039, -61.326854)

       // فارسكور
       //viewModel.getWeatherDetails(31.32805230565252, 31.715162800626036)

       //my location
      //viewModel.getWeatherDetails(30.791998, 31.9824553)
       viewModel.weatherDetails.observe(viewLifecycleOwner){

             timeOffestValue = it.timezone_offset!!
             // Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
              feelsLike.text = "Feels Like " + it.current?.feelsLike.toString()+"°"
              city.text = it.timezone.toString()
              temp.text = it.current?.temp.toString()+"°"
              val datecon:String =  convertToDate(it.current?.dt!!, requireContext())
              val timecon:String =  timestampToReadableTime(it.current?.dt!!)
              date.text = datecon + ", " + timecon

             // sunrise and sunset
              sunriseTime.text = timestampToReadableTime(it.current.sunrise!!)
              sunsetTime.text = timestampToReadableTime( it.current.sunset!!)

             // details of day
             uvi.text = it.current.uvi.toString()
             humidity.text = it.current.humidity.toString() + "%"
             wind.text = it.current.windSpeed.toString() + " km/h"


           // set image
           //imageViewLottie.setAnimation(getIconImage(it.icon.toString()))
           //holder.binding.imgWeaHour.setAnimation(getIconImage(weatherHours[position + 1].weather[0].icon!!))

              //recyclerView.adapter(ItemCardGridSunriseSunsetBinding.)

              // weather hour set
              bindHourlyWeather(it.hourly)

              // weather days set
              bindDailyWeather(it.daily)
          }

    }


    // hours
    private fun bindHourlyWeather(hourlyList: List<Hourly>) {
        weatherHourAdapter.setWeatherHours(hourlyList)
    }


    private fun initHourRecycle() {
        weatherHourAdapter = WeatherHourAdapter(requireParentFragment())
        _binding?.recyViewHourWeather.apply {
            this?.adapter = weatherHourAdapter
            this?.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.HORIZONTAL, false
            )
        }
    }

    // days

    private fun bindDailyWeather(dailyList: List<Daily>) {
        weatherDayAdapter.setWeatherDay(dailyList)
    }

    private fun intDayRecycle() {
        weatherDayAdapter = WeatherDayAdapter(requireParentFragment())
        _binding?.recyViewDailyWeather.apply {
            this?.adapter = weatherDayAdapter
            this?.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

}



