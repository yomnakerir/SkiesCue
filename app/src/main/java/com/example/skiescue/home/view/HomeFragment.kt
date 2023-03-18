package com.example.skiescue.home.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.app.LocaleManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.skiescue.R
import com.example.skiescue.data.ApiResponse
import com.example.skiescue.databinding.FragmentHomeBinding
import com.example.skiescue.databinding.ItemCardGridSunriseSunsetBinding
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.home.viewmodel.HomeViewModel
import com.example.skiescue.home.viewmodel.HomeViewModelFactory
import com.example.skiescue.model.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_ID = 60



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
    lateinit var sunriseTime : TextView
    lateinit var sunsetTime : TextView
    lateinit var wind: TextView
    lateinit var uvi: TextView
    lateinit var humidity: TextView
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
     private var lat : Double = 0.0
     private var long: Double = 0.0

    companion object TimeOffest{
        var timeOffestValue = 0

    }
    // location
    fun requestPermission(){

        ActivityCompat.requestPermissions(requireActivity(), listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ).toTypedArray()
            , PERMISSION_ID
        )
    }

    fun getLastLocation() {
        fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnCompleteListener {
                val location: Location? = it.result
                //Toast.makeText(requireContext(), "${it.longitude} and ${it.latitude}", Toast.LENGTH_LONG).show()
                if (location == null) {
                    Toast.makeText(requireContext(), "Null recived", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.getWeatherDetails(location.latitude
                        , location.longitude, "", getCurrentUnit(requireContext()), getCurrentLan(requireContext())
                    )
                    val geocoder = Geocoder(requireContext())
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    lat = location.latitude
                    long = location.longitude
                }


            }
    }


    fun isEnabledLocation():Boolean{
        val locationManager =  activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun checkPermission():Boolean{
        val findLoc =  ActivityCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val crossLoc = ActivityCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return findLoc && crossLoc
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding

        //println("*********************************************** ${lat}     ${long}")



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



    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

       initHourRecycle()
       intDayRecycle()

       // textView = view.findViewById(R.id.response)
       val repository = Repository(requireContext())
       val viewModelFactory = HomeViewModelFactory(repository = repository)
       viewModel =
           ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)

       
       val flag = initFavSharedPref(requireContext()).getInt(getString(R.string.FAV_FLAG), 0)
       if (flag == 1 &&
           Navigation.findNavController(requireView()).previousBackStackEntry?.destination?.id == R.id.favourite_fragment
       ) {

           //require get Data
           val lat = initFavSharedPref(requireContext()).getFloat(getString(R.string.LAT), 0f)
           val long = initFavSharedPref(requireContext()).getFloat(getString(R.string.LON), 0f)

           viewModel.getWeatherDetails(
               lat.toDouble(),
               long.toDouble(),
               "",
               getCurrentLan(requireContext()),
               getCurrentUnit(requireContext()),

               )


           //then update value
           initFavSharedPref(requireContext()).edit().apply {
               putInt(getString(R.string.FAV_FLAG), 0)
               apply()
           }

       } else {
           updateLocationFromGPS()
           if ((initSharedPref(requireContext()).getInt("B", 2) == 1)) {
               getLocation()
               initSharedPref(requireContext()).edit().apply {
                   putInt(getString(R.string.LOCATION), 3)
                   apply()
               }


           }

       }

       lifecycleScope.launch {
           viewModel.weatherDetails.collect { state ->
               when (state) {
                   is ApiResponse.OnSucess -> {

                       timeOffestValue = state.data.timezone_offset ?: 0
                       // Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                       feelsLike.text = "Feels Like " + state.data.current?.feelsLike.toString()
                       city.text = state.data.timezone.toString()
                       temp.text = state.data.current?.temp.toString() + "°"
                       val datecon: String =
                           convertToDate(state.data.current?.dt ?: 0, requireContext())
                       val timecon: String = timestampToReadableTime(state.data.current?.dt ?: 0)
                       date.text = datecon + ", " + timecon

                       // sunrise and sunset
                       sunriseTime.text = timestampToReadableTime(state.data.current?.sunrise ?: 0)
                       sunsetTime.text = timestampToReadableTime(state.data.current?.sunset ?: 0)

                       // details of day
                       uvi.text = state.data.current?.uvi?.toString() ?: ""
                       humidity.text =
                           state.data.current?.humidity?.toString()  +" %"?: (" " + "%")
                       wind.text =
                           state.data.current?.windSpeed?.toString() +" km/h"?: (" " + " km/h")


                       // set image
                       //imageViewLottie.setAnimation(getIconImage(state.data.icon.toString()))


                       //recyclerView.adapter(ItemCardGridSunriseSunsetBinding.)

                       // weather hour set
                       bindHourlyWeather(state.data.hourly)

                       // weather days set
                       bindDailyWeather(state.data.daily)

                   }
                   is ApiResponse.onError -> {}
                   is ApiResponse.OnLoading -> {}
               }

           }
       }




   }

    private fun updateLocationFromGPS() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if(checkPermission()){
            if(isEnabledLocation()){
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_LONG).show()
                getLastLocation()
            } else{
                Toast.makeText(requireContext(), "Should Enable Location", Toast.LENGTH_LONG).show()
            }

        }else{
            requestPermission()
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




