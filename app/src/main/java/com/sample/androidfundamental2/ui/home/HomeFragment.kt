package com.sample.androidfundamental2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.androidfundamental2.ViewModelFactory
import com.sample.androidfundamental2.databinding.FragmentHomeBinding
import com.sample.androidfundamental2.ui.adapter.EventAdapter
import com.sample.androidfundamental2.ui.adapter.EventCarouselAdapter
import com.sample.androidfundamental2.ui.detail.DetailActivity

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    
    private lateinit var upcomingAdapter: EventCarouselAdapter
    private lateinit var finishedAdapter: EventAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        observeViewModel()
        
        viewModel.loadHomeData()
    }
    
    private fun setupRecyclerViews() {
        upcomingAdapter = EventCarouselAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        
        finishedAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        
        binding.rvUpcomingEvents.adapter = upcomingAdapter
        binding.rvFinishedEvents.adapter = finishedAdapter
    }
    
    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }
        
        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.tvError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.tvError.text = error
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
