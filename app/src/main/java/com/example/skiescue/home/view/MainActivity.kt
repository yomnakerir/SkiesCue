package com.example.skiescue.home.view

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skiescue.R
import com.example.skiescue.customdialog.CustomDialog
import com.example.skiescue.databinding.ActivityMainBinding
import com.example.skiescue.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*sharedPreferences = initSharedPref(this)
        if (isFirst()) {
            // check with shared
            // here init first shared for all
            initUNIT(Units.METRIC.name, this)
            when (Locale.getDefault().language.toString()) {
                "en" -> {
                    initLan("en", this)
                }
                "ar" -> {
                    initLan("ar", this)

                }
            }

            binding.container.visibility = View.INVISIBLE
            CustomDialog().show(supportFragmentManager, "MyCustomFragment")
        }*/

        // show dialog set up
/*
        binding.container.visibility = View.INVISIBLE
        CustomDialog().show(supportFragmentManager, "MyCustomFragment") */

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)

    }


    override fun onResume() {
        super.onResume()
        setLan(getCurrentLan(this))

    }


    private fun isFirst(): Boolean {
        return initSharedPref(this).getBoolean(getString(R.string.FIRST), true)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(PERMISSION_ID == 40){
            finish()
            startActivity(intent)

        }

    }
}
