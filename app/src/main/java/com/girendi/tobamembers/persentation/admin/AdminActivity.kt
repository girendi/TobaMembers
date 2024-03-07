package com.girendi.tobamembers.persentation.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.girendi.tobamembers.R
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.ui.SimpleRecyclerAdapter
import com.girendi.tobamembers.databinding.ActivityAdminBinding
import com.girendi.tobamembers.databinding.DialogChangeDataBinding
import com.girendi.tobamembers.databinding.DialogConfirmationPasswordBinding
import com.girendi.tobamembers.databinding.ListItemUserBinding
import com.girendi.tobamembers.persentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var adapterUser: SimpleRecyclerAdapter<UserEntity>
    private val viewModelAdmin: AdminViewModel by viewModel()
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        handleObserveViewModel()
        handleRecyclerView()
        handleOnClick()
    }

    private fun handleOnClick() {
        binding.ivLogout.setOnClickListener {
            viewModelAdmin.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleRecyclerView() {
        adapterUser = SimpleRecyclerAdapter(
            context = this,
            layoutResId = R.layout.list_item_user,
            bindViewHolder = { view, item ->
                val itemBinding = ListItemUserBinding.bind(view)
                itemBinding.tvId.text = item.id.toString()
                itemBinding.tvUsername.text = item.username
                itemBinding.tvEmail.text = item.email
                itemBinding.tvRole.text = item.role
                itemBinding.ivDelete.visibility = if (item.id == userId) View.GONE else View.VISIBLE
                itemBinding.ivUpdate.setOnClickListener {
                    showDialogUpdateUser(item)
                }
                itemBinding.ivDelete.setOnClickListener {
                    showDialogConfirmation(item)
                }
            }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@AdminActivity)
            adapter = adapterUser
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleObserveViewModel() {
        viewModelAdmin.users.observe(this) { users ->
            adapterUser.setListItem(users)
        }
        viewModelAdmin.userSession.observe(this) { user ->
            if (user != null) {
                userId = user.id
                binding.userLogin.text = "Hi ${user.username},"
            }
        }
        viewModelAdmin.uiState.observe(this) { state ->
            handleUiState(state)
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

    private fun showDialogConfirmation(item: UserEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Input your password!")
        val dialogBinding = DialogConfirmationPasswordBinding.inflate(layoutInflater)
        val editText  = dialogBinding.edPassword
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("OK") { _, _ ->
            viewModelAdmin.validateUserPassword(userId, editText.text.toString(), item)
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

    private fun showDialogUpdateUser(item: UserEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update the data!")
        val dialogBinding = DialogChangeDataBinding.inflate(layoutInflater)
        val edUsername = dialogBinding.edUsername
        val edPassword  = dialogBinding.edPassword
        val edEmail = dialogBinding.edEmail
        val dpRole = dialogBinding.dropdownRole
        val items = listOf("Admin", "User")
        val adapterRole = ArrayAdapter(this, R.layout.list_item_role, items)
        dpRole.setAdapter(adapterRole)
        edUsername.setText(item.username)
        edEmail.setText(item.email)
        edPassword.setText(item.password)
        dpRole.setText(item.role)
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("OK") { _, _ ->
            val newData = UserEntity(
                id = item.id,
                username = edUsername.text.toString(),
                email = edEmail.text.toString(),
                password = edPassword.text.toString(),
                role = dpRole.text.toString()
            )
            viewModelAdmin.updateUser(newData)
        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.show()
    }
}