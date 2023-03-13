package com.example.skiescue.setting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.skiescue.R
import com.example.skiescue.databinding.FragmentSettingBinding
import com.example.skiescue.model.*
import java.util.*




class SettingFragment : Fragment() {

   private var _binding: FragmentSettingBinding ?= null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        handleRadioButton(requireContext())

        // handle change in Units
        binding.radioButtonTempMetricCelsius.setOnClickListener {
            onUnitsRadioButtonClicked(it)
        }
        binding.radioButtonTempMetricKelvin.setOnClickListener {
            onUnitsRadioButtonClicked(it)

        }
        binding.radioButtonTempMetricFahrenheit.setOnClickListener {
            onUnitsRadioButtonClicked(it)
        }
        // handle changes in Lan
        binding.radioButtonEnglish.setOnClickListener {
            onLanRadioButtonClicked(it)
        }
        binding.radioButtonArabic.setOnClickListener {
            onLanRadioButtonClicked(it)
        }

    }


    private fun onUnitsRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            if(isConnected(requireContext())){
                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.radioButton_temp_metric_celsius ->
                        if (checked) {
                            initUNIT(Units.METRIC.name, requireContext())

                        }
                    R.id.radioButton_temp_metric_Kelvin ->
                        if (checked) {
                            initUNIT(Units.STANDARD.name, requireContext())

                        }
                    R.id.radioButton_temp_metric_Fahrenheit ->
                        if (checked) {
                            initUNIT(Units.IMPERIAL.name, requireContext())
                        }
                }
            }else{
                Toast.makeText(
                    requireContext(),
                    getString(R.string.YMCN),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun onLanRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            if(isConnected(requireContext())){
                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.radioButton_English ->
                        if (checked) {
                            initLan("en", requireContext())
                            setLan("en")
                        }
                    R.id.radio_button_Arabic ->
                        if (checked) {
                            initLan("ar", requireContext())
                            setLan("ar")
                        }
                }
            }


        }
        activity?.finish()
        activity?.startActivity(activity?.intent)
    }


    private fun setLan(language: String) {
        val metric = resources.displayMetrics
        val configuration = resources.configuration
        configuration.locale = Locale(language)
        Locale.setDefault(Locale(language))

        configuration.setLayoutDirection(Locale(language))
        // update configuration
        resources.updateConfiguration(configuration, metric)
        // notify configuration
        onConfigurationChanged(configuration)

    }




    private fun handleRadioButton(context: Context) {
        handleUnitRadio(context)
        handleLanRadio(context)
    }

    private fun handleUnitRadio(context: Context) {
        when (getCurrentUnit(requireContext())) {
            Units.METRIC.name -> {
                updateUnit(true)
            }
            Units.IMPERIAL.name -> {
                updateUnit(imperial = true)
            }
            Units.STANDARD.name -> {
                updateUnit(standard = true)
            }
        }
    }

    private fun handleLanRadio(context: Context) {
        // get First
        when (getCurrentLan(context)) {
            "en" -> {
                updateLan(english = true)
            }
            "ar" -> {
                updateLan(arabic = true)
            }
        }
    }


    private fun updateUnit(
        metric: Boolean = false,
        imperial: Boolean = false,
        standard: Boolean = false
    ) {
        binding.radioButtonTempMetricCelsius.isChecked = metric
        binding.radioButtonTempMetricFahrenheit.isChecked = imperial
        binding.radioButtonTempMetricKelvin.isChecked = standard
    }


    private fun updateLan(english: Boolean = false, arabic: Boolean = false) {
        binding.radioButtonEnglish.isChecked = english
        binding.radioButtonArabic.isChecked = arabic

    }
}