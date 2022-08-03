package com.spacex.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacex.R
import com.spacex.databinding.FragmentRocketBinding
import com.spacex.data.model.Launch
import com.spacex.data.model.Rocket
import com.spacex.ui.main.adapters.LaunchListAdapter
import com.spacex.ui.main.viewmodels.RocketViewModel
import com.spacex.ui.ViewModelResponse
import com.spacex.util.haveConnection

class RocketFragment : Fragment() {
    // Properties
    private lateinit var launchesAdapter: LaunchListAdapter
    private lateinit var rocketID: String

    // View model
    private val viewModel: RocketViewModel by viewModels()

    // Content
    private var _binding: FragmentRocketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Content
        _binding = FragmentRocketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get bundle rocketID param
        rocketID = arguments?.getString("rocketID")!!
        viewModel.loadRocket(rocketID)

        data()
        setListeners()

    }

    private fun setListeners() {
        setAdapter()

        binding.btnReload.setOnClickListener {
            viewModel.loadRocket(rocketID)
        }
    }

    private fun setAdapter() {
        // Create adapter
        launchesAdapter = LaunchListAdapter()
        launchesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Load info
        binding.recyclerLaunches.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerLaunches.adapter = launchesAdapter
        binding.recyclerLaunches.isNestedScrollingEnabled = false
    }

    // LiveData observers
    private fun data() {
        viewModel.rocket.observe(viewLifecycleOwner) { rocket ->
            showRocket(rocket)
        }

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                // Show loader
                binding.linearLoading.visibility = View.VISIBLE
            } else {
                // Hide loader
                binding.linearLoading.visibility = View.GONE
            }
        }

        viewModel.launches.observe(viewLifecycleOwner) { launches ->
            showLaunches(launches)
        }

        viewModel.rocketError.observe(viewLifecycleOwner) { error ->
            showRocketError(error)
        }

        viewModel.launchesError.observe(viewLifecycleOwner) { error ->
            showLaunchesError(error)
        }
    }

    private fun showRocketError(error: ViewModelResponse) {
        when (error) {
            ViewModelResponse.NULL_EMPTY_DATA -> showRocketUIError(getText(R.string.error_no_rockets))
            ViewModelResponse.NO_NETWORK -> {
                if (!haveConnection(context!!)) showRocketUIError(getText(R.string.error_no_internet))
                else showRocketUIError(getText(R.string.error_something_wrong))
            }
            ViewModelResponse.GENERIC_ERROR -> showRocketUIError(getText(R.string.error_something_wrong))
        }
    }

    private fun showLaunchesError(error: ViewModelResponse) {
        when (error) {
            ViewModelResponse.NULL_EMPTY_DATA -> showLaunchesUIError(getText(R.string.error_no_launches))
            ViewModelResponse.NO_NETWORK -> {
                if (!haveConnection(context!!)) showLaunchesUIError(getText(R.string.error_no_internet))
                else showLaunchesUIError(getText(R.string.error_something_wrong))
            }
            ViewModelResponse.GENERIC_ERROR -> showLaunchesUIError(getText(R.string.error_something_wrong))
        }
    }

    private fun showRocketUIError(text: CharSequence) {
        binding.linearError.visibility = View.VISIBLE
        binding.txtError.text = text
        binding.linearRocket.visibility = View.GONE
    }

    private fun showLaunchesUIError(text: CharSequence) {
        binding.txtNoLaunches.visibility = View.VISIBLE
        binding.txtNoLaunches.text = text
        binding.recyclerLaunches.visibility = View.GONE
    }

    private fun showLaunches(launches: MutableList<Launch>) {
        //Update launches list
        launches.let {
            launchesAdapter.submitList(it)
        }

        // Show launches
        binding.recyclerLaunches.visibility = View.VISIBLE
        binding.txtNoLaunches.visibility = View.GONE
    }

    private fun showRocket(rocket: Rocket) {
        // Set toolbar title
        (activity as MainActivity).changeToolbarTitle(rocket.name)

        // Fill ui
        binding.txtName.text = rocket.name + " "
        binding.txtDescription.text = rocket.description
        Glide.with(context!!).load(rocket.flickr_images[0]).into(binding.imgProfile)
        binding.txtLocation.text = rocket.country
        binding.txtEngines.text =
            rocket.engines.number.toString() + " " + getText(R.string.rocket_engines)
        binding.txtMass.text = rocket.mass.kg.toString() + " " + getText(R.string.rocket_mass)
        binding.txtHeight.text =
            rocket.height.meters.toString() + " " + getText(R.string.rocket_meters)
        if (rocket.active) binding.imgToggle.setColorFilter(
            context!!.resources.getColor(
                R.color.success,
                context!!.theme
            )
        )
        else binding.imgToggle.setColorFilter(
            context!!.resources.getColor(
                R.color.grey600,
                context!!.theme
            )
        )
        binding.txtLocation.text = rocket.country + " "

        // Show rocket
        binding.linearRocket.visibility = View.VISIBLE
        binding.linearError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
