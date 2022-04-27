package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.MainViewModelFactory
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var application: Application
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this, MainViewModelFactory(application))[MainViewModel::class.java]

        val binding = FragmentMainBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = AsteroidListAdapter(AsteroidClickListener {
            viewModel.onNavigateToAsteroid(it)
            viewModel.doneNavigatingToAsteroid()
        })


        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
            val navController = findNavController()
            it?.let {
                navController.navigate(MainFragmentDirections.actionShowDetail(it))
            }
        })

        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.updateFilter(AsteroidFilter.SHOW_WEEK)
            R.id.show_today_menu ->  viewModel.updateFilter(AsteroidFilter.SHOW_TODAY)
            else -> viewModel.updateFilter(AsteroidFilter.SHOW_SAVED)
        }
        return true
    }
}
