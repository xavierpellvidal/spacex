package com.spacex.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spacex.R
import com.spacex.SpaceXApp
import com.spacex.databinding.FragmentRocketListBinding
import com.spacex.data.model.Rocket
import com.spacex.ui.main.adapters.RocketListAdapter
import com.spacex.ui.main.viewmodels.RocketListViewModel
import com.spacex.ViewModelResponse
import com.spacex.util.haveConnection

class RocketListFragment : Fragment(), (Rocket) -> Unit {

    // Properties
    private lateinit var rocketsAdapter: RocketListAdapter

    // View model linked to parent activity
    private val viewModel: RocketListViewModel by activityViewModels()

    // Content
    private var _binding: FragmentRocketListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Content
        _binding = FragmentRocketListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRocketsList(SpaceXApp.mPrefs.filtersEnabled, SpaceXApp.mPrefs.activeFilterValue)

        setAdapter()
        setObservers()
        setListeners()
    }

    private fun setAdapter() {
        // Create adapter
        rocketsAdapter = RocketListAdapter(this, context)
        rocketsAdapter!!.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Load info
        binding.rocketsList.layoutManager =
            LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        binding.rocketsList.adapter = rocketsAdapter
    }

    private fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            reloadRockets()
        }

        binding.btnReload.setOnClickListener {
            reloadRockets()
        }
    }

    private fun reloadRockets() {
        // Hide error and reload data
        binding.linearError.visibility = View.GONE
        viewModel.getRocketsList(SpaceXApp.mPrefs.filtersEnabled, SpaceXApp.mPrefs.activeFilterValue)
    }

    private fun setObservers() {
        // Remove if any observer attached
        viewModel.rockets.removeObservers(viewLifecycleOwner)
        viewModel.loader.removeObservers(viewLifecycleOwner)
        viewModel.error.removeObservers(viewLifecycleOwner)

        viewModel.rockets.observe(viewLifecycleOwner) { rockets ->
            showRockets(rockets)
        }

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            binding.swipeRefresh.isRefreshing = loader
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            showError(error)
        }
    }

    private fun showError(error: ViewModelResponse) {
        when (error) {
            ViewModelResponse.NULL_EMPTY_DATA -> showUIError(getText(R.string.error_no_rockets))
            ViewModelResponse.NO_NETWORK -> {
                if (!haveConnection(context!!)) showUIError(getText(R.string.error_no_internet))
                else showUIError(getText(R.string.error_something_wrong))
            }
            ViewModelResponse.GENERIC_ERROR -> showUIError(getText(R.string.error_something_wrong))
        }
    }

    private fun showUIError(text: CharSequence) {
        binding.linearError.visibility = View.VISIBLE
        binding.txtError.text = text
        binding.principalContraint.visibility = View.GONE
    }

    private fun showRockets(rockets: MutableList<Rocket>) {
        //Update rocket list
        rockets?.let {
            rocketsAdapter.submitList(it)
        }

        //Show results and hide error div
        binding.linearError.visibility = View.GONE
        binding.principalContraint.visibility = View.VISIBLE

        // Hide loader
        binding.swipeRefresh.isRefreshing = false
    }

    // Rocket clicked
    override fun invoke(rocketClick: Rocket) {
        val bundle = bundleOf("rocketID" to rocketClick.id)
        findNavController(binding.root).navigate(
            R.id.action_RocketListFragment_to_RocketFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}