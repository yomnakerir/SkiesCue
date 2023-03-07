package com.example.skiescue.favourite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.skiescue.R
import com.example.skiescue.databinding.FragmentFavouriteBinding
import com.example.skiescue.databinding.FragmentSettingBinding
import com.example.skiescue.favourite.viewmodel.FavouriteViewModel
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.model.Repository


class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view model factory
        val repository = Repository(requireContext())
       val viewModelFactory = FavouriteViewModelFactory(repository =repository )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(FavouriteViewModel::class.java)

        viewModel.getFavouriteList()
        viewModel.favouriteList.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"length of fav is ${it.size}", Toast.LENGTH_LONG).show()
        }
    }
}