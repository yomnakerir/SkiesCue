package com.example.skiescue.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.skiescue.R
import com.example.skiescue.databinding.ActivitySplashBinding
import com.example.skiescue.home.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding
    private val splashScreenScope = lifecycleScope


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // will be go to main Screen
        splashScreenScope.launch(Dispatchers.Default) {
            delay(2500)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        splashScreenScope.cancel()
    }
}