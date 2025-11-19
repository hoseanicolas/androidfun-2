package com.sample.androidfundamental2.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.androidfundamental2.ViewModelFactory
import com.sample.androidfundamental2.databinding.FragmentFinishedBinding
import com.sample.androidfundamental2.ui.adapter.EventAdapter
import com.sample.androidfundamental2.ui.detail.DetailActivity

class FinishedFragment : Fragment() {
    
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    
    private lateinit var adapter: EventAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
        
        viewModel.loadFinishedEvents()
    }
    
    private fun setupRecyclerView() {
        adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        
        binding.rvFinishedEvents.adapter = adapter
    }
    
    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
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
