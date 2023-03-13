package com.example.skiescue.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.example.skiescue.R
import com.example.skiescue.model.initFavSharedPref
import com.example.skiescue.model.initSharedPref

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : DialogFragment() {
    lateinit var saveBtn:Button
   /* private val callback = OnMapReadyCallback { googleMap ->

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setOnMapClickListener {
//1 means GPS //2 means MAPS //3 draw Location
            // inti marker option
            var lat: Float = 0f
            var long: Float = 0f
            val marker = MarkerOptions().apply {
                position(it)
                lat = it.latitude.toFloat()
                long = it.longitude.toFloat()
                title((lat).plus(long).toString())
                googleMap.clear()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 10f
                    )
                )
            }
            googleMap.addMarker(marker)
    }

    } */



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v:View = inflater.inflate(R.layout.fragment_maps, container, false)
        saveBtn = v.findViewById(R.id.btn_save)
        return v;
    }

    private val callback = OnMapReadyCallback { googleMap ->


        googleMap.setOnMapClickListener {
//1 means GPS //2 means MAPS //3 draw Location
            // inti marker option
            var lat: Float = 0f
            var long: Float = 0f
            val marker = MarkerOptions().apply {
                position(it)
                lat = it.latitude.toFloat()
                long = it.longitude.toFloat()
                title((lat).plus(long).toString())
                googleMap.clear()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 10f
                    )
                )
            }
            googleMap.addMarker(marker)
            //changeSaveCondition(false)

            saveBtn.setOnClickListener {
                handleSaveClickable(lat, long)
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun changeSaveCondition(visible: Boolean) {
        if (visible) {
            saveBtn.visibility = View.INVISIBLE
            saveBtn.isClickable = false
        } else {
            saveBtn.visibility = View.VISIBLE
            saveBtn.isClickable = true
        }
    }

    private fun handleSaveClickable(lat: Float, long: Float) {

        when (Navigation.findNavController(requireView()).previousBackStackEntry?.destination?.id) {
            R.id.favourite_fragment-> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_mapsFragment_to_favourite_fragment)
                initFavSharedPref(requireContext())
                    .edit()
                    .apply {
                        putFloat(getString(R.string.LON), long)
                        putFloat(getString(R.string.LAT), lat)
                        putInt("SIGN",2)
                        apply()
                    }
            }
            else -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_mapsFragment_to_home_fragment)
                initSharedPref(requireContext())
                    .edit()
                    .apply {
                        putFloat(getString(R.string.LON), long)
                        putFloat(getString(R.string.LAT), lat)
                        putInt(getString(R.string.LOCATION), 3)
                        apply()
                    }
            }
        }

        changeSaveCondition(true)
    }
}