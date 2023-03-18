package com.example.skiescue.dialog.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.skiescue.R
import com.example.skiescue.alert.view.*
import com.example.skiescue.databinding.FragmentAlertBinding
import com.example.skiescue.databinding.FragmentAlertDialogBinding
import com.example.skiescue.databinding.FragmentHomeBinding
import com.example.skiescue.databinding.FragmentSettingBinding
import com.example.skiescue.dialog.viewmodel.AlertViewModel
import com.example.skiescue.dialog.viewmodel.AlertViewModelFactory
import com.example.skiescue.model.Repository
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class AlertDialog : DialogFragment() {

    private var _binding: FragmentAlertDialogBinding?=null
    private val binding get() = _binding!!
    private lateinit var fromBtn : Button
    private lateinit var toBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var viewModel: AlertViewModel
    // model
    private lateinit var alertModel: AlertModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        binding

        // view model factory
        val repository = Repository(requireContext())
        val viewModelFactory = AlertViewModelFactory(repository =repository )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(AlertViewModel::class.java)


        fromBtn = binding.btnFrom
        toBtn = binding.btnTo
        saveBtn = binding.btnSaveAlert



        // intial
        setInitialData()

        // Events
        fromBtn.setOnClickListener {
            showDatePicker(true)
        }

        toBtn.setOnClickListener {
            showDatePicker(false)
        }

        saveBtn.setOnClickListener {
            viewModel.insertAlert(alertModel)
            dialog!!.dismiss()
        }

        // observe Insert
         lifecycleScope.launch {
             viewModel.stateInsetAlert.collect{id->
                 // Register Worker Here and send ID of alert
                 setPeriodWorkManger(id)
             }
         }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // intialize model

    }


    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }





    private fun setInitialData() {
        val rightNow = Calendar.getInstance()
        // init time
        val currentHour = TimeUnit.HOURS.toSeconds(rightNow.get(Calendar.HOUR_OF_DAY).toLong())
        val currentMinute = TimeUnit.MINUTES.toSeconds(rightNow.get(Calendar.MINUTE).toLong())
        val currentTime = (currentHour + currentMinute).minus(3600L * 2)
        val currentTimeText = timeConverterToString((currentTime + 60), requireContext())
        val afterOneHour = currentTime.plus(3600L)
        val afterOneHourText = timeConverterToString(afterOneHour, requireContext())
        // init day
        val year = rightNow.get(Calendar.YEAR)
        val month = rightNow.get(Calendar.MONTH)
        val day = rightNow.get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date, requireContext())
        val currentDate = dayConverterToString(dayNow, requireContext())
        //init model
        alertModel =
            AlertModel(
                startTime = (currentTime + 60),
                endTime = afterOneHour,
                startDate = dayNow,
                endDate = dayNow
            )
        //init text
        fromBtn.text = currentDate.plus("\n").plus(currentTimeText)
        toBtn.text = currentDate.plus("\n").plus(afterOneHourText)
    }



    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = dayConverterToString(datePicker, requireContext())
                val timeString = timeConverterToString(time, requireContext())
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    alertModel.startTime = time
                    alertModel.startDate = datePicker
                    fromBtn.text = text
                } else {
                    alertModel.endTime = time
                    alertModel.endDate = datePicker
                    toBtn.text = text
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle("Choose time")
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun showDatePicker(isFrom: Boolean) {
        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    showTimePicker(isFrom, convertDateToLong(date, requireContext()))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle("Choose date")
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }


    private fun setPeriodWorkManger(id: Long) {

         val data = Data.Builder()
         data.putLong("id", id)

         val constraints = Constraints.Builder()
             .setRequiresBatteryNotLow(true)
             .build()

         val periodicWorkRequest = PeriodicWorkRequest.Builder(
             AlertPeriodicWorkManager::class.java,
             24, TimeUnit.HOURS
         )
             .setConstraints(constraints)
             .setInputData(data.build())
             .build()

         WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
             "$id",
             ExistingPeriodicWorkPolicy.REPLACE,
             periodicWorkRequest
         )
     }
}