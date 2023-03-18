package com.example.skiescue.alert.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.skiescue.R
import com.example.skiescue.databinding.FragmentAlertBinding
import com.example.skiescue.dialog.view.AlertDialog
import com.example.skiescue.dialog.viewmodel.AlertViewModel
import com.example.skiescue.dialog.viewmodel.AlertViewModelFactory
import com.example.skiescue.model.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding?= null
    private val binding get() = _binding!!
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var viewModel: AlertViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAlertWeatherRecycle()

        // view model factory
        val repository = Repository(requireContext())
        val viewModelFactory = AlertViewModelFactory(repository = repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(AlertViewModel::class.java)


        binding.fabAddAlert.setOnClickListener {

            AlertDialog ().show(
                    requireActivity().supportFragmentManager,
                    "MyAlertDialogFragment")
        }



        viewModel.getAlerts()
        lifecycleScope.launch {
            viewModel.stateGetAlert.collect(){
                if (it.isNullOrEmpty()) {

                    binding.recAlertWeathers.visibility = View.GONE
                } else {

                    binding.recAlertWeathers.visibility = View.VISIBLE
                    bindAlertWeathers(it)
                }

            }

        }

        // start service just for test
       /* lifecycleScope.launch {
            delay(5000)
            startAlertService("For test")
        }*/

    }

    private fun initAlertWeatherRecycle() {
        alertAdapter = AlertAdapter(requireParentFragment(), deleteAction)
        binding.recAlertWeathers.apply {
            this.adapter = alertAdapter
            this.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )

        }

    }


    // lambda
    private var deleteAction: (AlertModel) -> Unit = {
        viewModel.deleteAlert(it)
        WorkManager.getInstance()?.cancelUniqueWork(id.toString())

        Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT)
            .show()

    }


    private fun bindAlertWeathers(alertWeathers: List<AlertModel>) {
        alertAdapter.setWeatherAlerts(alertWeathers)
    }


    // for test
   /* private fun startAlertService(description: String){
        val intent = Intent(requireContext(), AlertService::class.java)
        intent.putExtra("description", description)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            ContextCompat.startForegroundService(requireContext(), intent)
        }else{
            requireActivity().startService(intent)
        }
    }*/

}