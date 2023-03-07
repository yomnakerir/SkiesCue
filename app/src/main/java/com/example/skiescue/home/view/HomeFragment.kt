package com.example.skiescue.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.skiescue.databinding.FragmentHomeBinding
import com.example.skiescue.favourite.viewmodel.FavouriteViewModelFactory
import com.example.skiescue.home.viewmodel.HomeViewModel
import com.example.skiescue.home.viewmodel.HomeViewModelFactory
import com.example.skiescue.model.Repository

const val PERMISSION_ID = 40

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: HomeViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val repository = Repository(requireContext())
       val viewModelFactory = HomeViewModelFactory(repository =repository )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)

        // here i make  an observer
       viewModel.getWeatherDetails(30.668351057603832, 32.227911043392865)
          viewModel.weatherDetails.observe(viewLifecycleOwner){
              Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
          }
    }
}

