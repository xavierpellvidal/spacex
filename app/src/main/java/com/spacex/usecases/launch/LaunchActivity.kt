package com.spacex.usecases.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.spacex.R
import com.spacex.databinding.ActivityLaunchBinding
import com.spacex.usecases.main.MainActivity
import com.spacex.util.Constants

class LaunchActivity : AppCompatActivity() {

    // Life cycle
    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Content
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initApp()
    }

    private fun initApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            //Navigate to main activity
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
            finish()
        }, Constants.SPLASH_DELAY.toLong())
    }

}