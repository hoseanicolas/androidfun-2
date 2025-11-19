package com.sample.androidfundamental2.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.androidfundamental2.ViewModelFactory
import com.sample.androidfundamental2.databinding.FragmentSearchBinding
import com.sample.androidfundamental2.ui.adapter.EventAdapter
import com.sample.androidfundamental2.ui.detail.DetailActivity

class SearchFragment : Fragment() {
    
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    
    private lateinit var adapter: EventAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        
        binding.tvEmpty.visibility = View.VISIBLE
    }
    
    private fun setupRecyclerView() {
        adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        
        binding.rvSearchResults.adapter = adapter
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        viewModel.searchEvents(it)
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.clearSearch()
                    binding.tvEmpty.visibility = View.VISIBLE
                }
                return true
            }
        })
    }
    
    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            if (events.isEmpty() && !binding.searchView.query.isNullOrEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.tvEmpty.setText(com.sample.androidfundamental2.R.string.search_no_results)
            } else if (events.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
            }
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
