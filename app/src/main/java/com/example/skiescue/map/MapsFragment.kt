package com.example.skiescue.map

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.skiescue.R
import com.example.skiescue.data.ApiResponse
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.favourite.viewmodel.FavouriteViewModel
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.model.Repository
import com.example.skiescue.model.WeatherResponse
import com.example.skiescue.model.initFavSharedPref
import com.example.skiescue.model.initSharedPref

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class MapsFragment : DialogFragment() {
    lateinit var saveBtn:Button
    lateinit var searchBar: SearchView
    lateinit var geocoder:Geocoder
    lateinit var favouriteViewModel: FavouriteViewModel
    //lateinit var repo: Repository

    companion object CityName{
         var city:String = ""     }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v:View = inflater.inflate(R.layout.fragment_maps, container, false)
        saveBtn = v.findViewById(R.id.btn_save)
        searchBar = v.findViewById(R.id.search_view)
        favouriteViewModel = ViewModelProvider(this, FavouriteViewModelFactory(Repository(requireContext())))
            .get(FavouriteViewModel::class.java)
        return v;
    }
    private val callback = OnMapReadyCallback { googleMap ->

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val location:String = searchBar.query.toString()
                var addressList : List<Address>

                if(location != null || location != ""){

                    geocoder = Geocoder(requireContext())
                    addressList = geocoder.getFromLocationName(location, 1)!!
                    var address:Address = addressList.get(0)
                     city = addressList.get(0).getAddressLine(0)
                    var latLng:LatLng = LatLng(address.latitude, address.longitude)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(location))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        googleMap.setOnMapClickListener {
            var lat: Double = 0.0
            var long: Double = 0.0

            val marker = MarkerOptions().apply {
                position(it)
                lat = it.latitude
                long = it.longitude
                title((lat).plus(long).toString())
                googleMap.clear()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 10f
                    )
                )
            }
            googleMap.addMarker(marker)


            saveBtn.setOnClickListener {
                lifecycleScope.launch {
                    favouriteViewModel.insertFavourite(Favourite(latitude = lat, longitude = long, city = city))
                }

                Toast.makeText(requireContext(),"inserted success", Toast.LENGTH_LONG ).show()
            }
        }

        googleMap.mapType

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



}