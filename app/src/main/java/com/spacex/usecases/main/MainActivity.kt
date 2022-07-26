package com.spacex.usecases.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.spacex.R
import com.spacex.SpaceXApp
import com.spacex.databinding.ActivityMainBinding
import com.spacex.usecases.main.viewmodels.RocketListViewModel

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    // Private
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var showFilterItemMenu = true
    private var isSettingsTabUp: Boolean = false

    // Fragment viewmodel to interact to
    private val rocketsListViewModel: RocketListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Content
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Action bar
        setSupportActionBar(binding.toolbar)

        setListeners()
        fillSettingsTab()
    }

    private fun fillSettingsTab() {
        // Fill settings tab options
        if (SpaceXApp.mPrefs.isRocketsFilterEnabled()) {
            binding.radioActive.isChecked = SpaceXApp.mPrefs.getActiveFilter()
            binding.radioInactive.isChecked = !SpaceXApp.mPrefs.getActiveFilter()
        } else {
            SpaceXApp.mPrefs.clear()
            binding.radioGroup.clearCheck()
        }
    }

    private fun setListeners() {
        // Apply filters
        binding.buttonApply.setOnClickListener {
            if (binding.radioActive.isChecked || binding.radioInactive.isChecked) {
                SpaceXApp.mPrefs.setRocketsFilterEnabled(true)
                SpaceXApp.mPrefs.setActiveFilter(binding.radioActive.isChecked)
                rocketsListViewModel.getRocketsList()
            }
            settingsTabChangeState()
        }
        // Reset filters
        binding.buttonReset.setOnClickListener {
            SpaceXApp.mPrefs.clear()
            rocketsListViewModel.getRocketsList()
            settingsTabChangeState()
        }
        binding.linearBackMenu.setOnClickListener { settingsTabChangeState() }
        binding.linearMenu.setOnClickListener {}

        // Nav controller linked to action bar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set listener to know where we are
        navController.addOnDestinationChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu -> this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.item_filter -> {
                settingsTabChangeState()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.item_filter)
        item.isVisible = showFilterItemMenu
        return true
    }

    private fun enableFilterMenuItem(enable: Boolean) {
        showFilterItemMenu = enable
        // Invalidate to call onPrepareOptionsMenu() and update toolbar
        invalidateOptionsMenu()
    }

    fun changeToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || (super.onSupportNavigateUp())
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.RocketListFragment -> {
                enableFilterMenuItem(true)
                changeToolbarTitle(getString(R.string.rockets_list_label))
            }
            R.id.RocketFragment -> {
                enableFilterMenuItem(false)
                changeToolbarTitle("")
            }
        }
    }

    private fun showSettingsTab() {
        // Fill setting info selected/applied
        fillSettingsTab()

        // Slide up settings view
        binding.linearMenu.visibility = View.VISIBLE
        val animate = TranslateAnimation(0F, 0F, binding.linearMenu.height.toFloat(), 0F)
        animate.duration = 500
        binding.linearMenu.startAnimation(animate)

        // Show background
        binding.linearBackMenu.visibility = View.VISIBLE
        binding.linearBackMenu.animate()
            .alpha(1f)
            .duration = 500
    }

    private fun hideSettingsTab() {
        // Hide settings view
        val animate = TranslateAnimation(0F, 0F, 0F, binding.linearMenu.height.toFloat())
        animate.duration = 500
        binding.linearMenu.startAnimation(animate)
        binding.linearMenu.visibility = View.GONE

        // Hide background
        binding.linearBackMenu.animate()
            .alpha(0f)
            .setDuration(500)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    // Only gone if alpha 0 (hide animation)
                    if (binding.linearBackMenu.alpha == 0.0f)
                        binding.linearBackMenu.visibility = View.GONE
                }
            })
    }

    // Open/close settings view
    private fun settingsTabChangeState() {
        if (isSettingsTabUp) hideSettingsTab()
        else showSettingsTab()
        isSettingsTabUp = !isSettingsTabUp
    }

    override fun onBackPressed() {
        if (isSettingsTabUp) settingsTabChangeState()
        else super.onBackPressed()
    }
}
