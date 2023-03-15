package com.example.skiescue.setting

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //notification
        settingManager()
    }


    private fun settingManager(){
        val notificationRadio:RadioButton = binding.radioButtonNotification
        val alertRadio:RadioButton = binding.radioButtonAlertDialog

        // intialize shared pref
       val sharedPref = requireContext().getSharedPreferences("MySetting",Context.MODE_PRIVATE )
       val editor = sharedPref.edit()

        // return state of radio button
        val isDialogState = sharedPref.getBoolean("IsDialog", false)
        if(isDialogState){
            notificationRadio.isChecked = false
            alertRadio.isChecked = true
        }

        else{
            notificationRadio.isChecked = true
            alertRadio.isChecked = false
        }

        // events

        notificationRadio.setOnCheckedChangeListener{_, isChecked->
            if(isChecked){
                editor.putBoolean("IsDialog", false)
                editor.apply()
            }
        }


        alertRadio.setOnCheckedChangeListener{_, isChecked->
            if(isChecked){
                editor.putBoolean("IsDialog", true)
                editor.apply()

                checkPermissionOfOverlay()
            }
        }


    }



    private fun checkPermissionOfOverlay(){
          if(!Settings.canDrawOverlays(requireContext())){
              val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
              alertDialogBuilder.setTitle("Display on top")
                  .setMessage("You should let us to draw on top")
                  .setPositiveButton("Okay"){dialog:DialogInterface, _:Int->

                      val intent = Intent(
                          Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                          Uri.parse("package:" + requireContext().applicationContext.packageName)
                      )
                      startActivityForResult(intent, 1)
                      dialog.dismiss()

                  }.setNegativeButton("No"){
                      dialog:DialogInterface,_:Int->
                      dialog.dismiss()
                  }.show()
          }
    }



}