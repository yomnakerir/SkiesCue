package com.example.skiescue.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.skiescue.R
import com.example.skiescue.data.network.RetrofitInstance
import com.example.skiescue.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // catch data and display it (Test)
        val remote = RetrofitInstance().apiCall()
        lifecycleScope.launch {
         val dataResponse =  async {  remote.getCurrentWeather(30.668351057603832, 32.227911043392865)}
            if(dataResponse.await().isSuccessful){
                val allData = dataResponse.await().body()
                Toast.makeText(requireContext(), allData.toString(), Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding
        return binding.root
    }


}