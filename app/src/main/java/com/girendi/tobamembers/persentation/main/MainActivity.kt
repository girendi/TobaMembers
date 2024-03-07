package com.girendi.tobamembers.persentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.girendi.tobamembers.R
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse
import com.girendi.tobamembers.core.ui.SimpleRecyclerAdapter
import com.girendi.tobamembers.databinding.ActivityMainBinding
import com.girendi.tobamembers.databinding.ListItemPhotoBinding
import com.girendi.tobamembers.persentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterPhoto: SimpleRecyclerAdapter<PhotoResponse>
    private val viewModelMain: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        handleRecyclerView()
        handleObserveViewModel()
        handleOnClick()
    }

    private fun handleOnClick() {
        binding.ivLogout.setOnClickListener {
            viewModelMain.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleRecyclerView() {
        adapterPhoto = SimpleRecyclerAdapter(
            context = this,
            layoutResId = R.layout.list_item_photo,
            bindViewHolder = { view, item ->
                val itemBinding = ListItemPhotoBinding.bind(view)
                itemBinding.tvId.text = item.id.toString()
                itemBinding.tvTitle.text = item.title
                Glide.with(applicationContext)
                    .load(item.url)
                    .into(itemBinding.imageView)
            }
        )
        binding.rvPhotos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterPhoto
        }

        binding.rvPhotos.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvPhotos.canScrollVertically(1)) {
                        viewModelMain.fetchPhotos()
                    }
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun handleObserveViewModel() {
        viewModelMain.uiState.observe(this) { state ->
            handleUiState(state)
        }
        viewModelMain.listPhoto.observe(this) { photo ->
            adapterPhoto.setListItem(photo)
        }
        viewModelMain.userSession.observe(this) { user ->
            if (user != null) {
                binding.userLogin.text = "Hi ${user.username},"
            }
        }
    }

    private fun handleUiState(state: UiState) {
        when (state) {
            is UiState.Loading -> {
                showProgressBar(true)
            }
            is UiState.Success -> {
                showProgressBar(false)
            }
            is UiState.Error -> {
                showProgressBar(false)
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}