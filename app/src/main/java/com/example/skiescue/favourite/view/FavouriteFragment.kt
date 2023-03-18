package com.example.skiescue.favourite.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skiescue.R
import com.example.skiescue.data.local.Favourite
import com.example.skiescue.databinding.FragmentFavouriteBinding
import com.example.skiescue.favourite.viewmodel.FavouriteViewModel
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.model.Repository
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var favWeatherAdapter: FavouriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFavWeatherRecycle()

        // view model factory
        val repository = Repository(requireContext())
       val viewModelFactory = FavouriteViewModelFactory(repository =repository )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(FavouriteViewModel::class.java)

        viewModel.getFavouriteList()
        lifecycleScope.launch {
            viewModel.favouriteList.collect{
               // Toast.makeText(requireContext(),"length of fav is ${it.size}", Toast.LENGTH_LONG).show()
            }
        }



        // open intent google map
        binding.fabAddLocation.setOnClickListener(){
            Navigation.findNavController(requireView())
                .navigate(R.id.action_favourite_fragment_to_mapsFragment)
        }


       /* viewModel.favouriteList.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"length of fav is ${it.size}", Toast.LENGTH_LONG).show()
        }*/


   lifecycleScope.launch {
       viewModel.favouriteList.collect {
           if (it.isEmpty()) {
               binding.recFavWeathers.visibility = View.GONE
              // binding.imgEmpty.visibility = View.VISIBLE
               //binding.txtEmpty.visibility = View.VISIBLE

           } else {
               binding.recFavWeathers.visibility = View.VISIBLE
               //binding.imgEmpty.visibility = View.GONE
               //binding.txtEmpty.visibility = View.GONE
               bindFavWeather(it)
           }

       }

   }
    }



    private fun initFavWeatherRecycle() {
        favWeatherAdapter = FavouriteAdapter(requireParentFragment(), deleteAction)
        binding.recFavWeathers.apply {
            this.adapter = favWeatherAdapter
            this.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

    private fun bindFavWeather(favWeathers: List<Favourite>) {
        favWeatherAdapter.setFavWeather(favWeathers)
    }


    // lambda
    var deleteAction: (Favourite) -> Unit = {
          deleteFavWeather(it)
    }

    private fun deleteFavWeather(favourite: Favourite) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteFavourite(favourite)
            Toast.makeText(requireContext(), getString(R.string.SuccessDeleted), Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ".plus(favourite.city))
        builder.setMessage("are you want to delete ? ".plus(favourite.city))
        builder.create().show()

    }



}