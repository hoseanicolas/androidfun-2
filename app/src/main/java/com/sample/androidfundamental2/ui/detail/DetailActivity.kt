package com.sample.androidfundamental2.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sample.androidfundamental2.R
import com.sample.androidfundamental2.ViewModelFactory
import com.sample.androidfundamental2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var eventId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId != -1) {
            viewModel.loadEventDetail(eventId)
            observeFavoriteStatus()
        }

        observeViewModel()
    }
    
    private fun observeViewModel() {
        viewModel.event.observe(this) { event ->
            event?.let {
                binding.collapsingToolbar.title = it.name
                binding.tvEventName.text = it.name
                binding.tvOrganizer.text = it.ownerName
                binding.tvTime.text = it.beginTime
                binding.tvQuota.text = getString(R.string.detail_quota_format, it.remainingQuota)
                
                val description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(it.description)
                }
                binding.tvDescription.text = description
                
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.color.placeholder_color)
                    .error(R.color.placeholder_color)
                    .into(binding.ivEventImage)
                
                binding.btnRegister.setOnClickListener { _ ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                    startActivity(intent)
                }
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
            }
        }
    }

    private fun observeFavoriteStatus() {
        var currentFavoriteStatus = false

        viewModel.isEventFavorite(eventId).observe(this) { isFavorite ->
            currentFavoriteStatus = isFavorite
            updateFavoriteButton(isFavorite)
        }

        binding.fabFavorite.setOnClickListener {
            viewModel.event.value?.let { event ->
                viewModel.toggleFavorite(event, currentFavoriteStatus)
            }
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
            binding.fabFavorite.contentDescription = getString(R.string.remove_from_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.outline_favorite_border_24)
            binding.fabFavorite.contentDescription = getString(R.string.add_to_favorite)
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
