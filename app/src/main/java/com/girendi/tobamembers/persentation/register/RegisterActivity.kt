package com.girendi.tobamembers.persentation.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.girendi.tobamembers.R
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.ui.CustomTextWatcher
import com.girendi.tobamembers.databinding.ActivityRegisterBinding
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModelRegister: RegisterViewModel by viewModel()
    private var isBelievableUsername: Boolean = false
    private var isBelievableEmail: Boolean = false
    private var isValidButton: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        handleOnClick()
        handleStreamText()
        handleObserveViewModel()
    }

    private fun handleObserveViewModel() {
        viewModelRegister.uiState.observe(this) { state ->
            handleUiState(state)
        }
        viewModelRegister.items.observe(this) { items ->
            val adapterRole = ArrayAdapter(this, R.layout.list_item_role, items)
            binding.dropdownRole.setAdapter(adapterRole)
        }
        viewModelRegister.success.observe(this) { status ->
            if (status) {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        viewModelRegister.isUsernameExists.observe(this) { status ->
            isBelievableUsername = !status
            showUsernameMinimalAlert(status)
            updateButton()
        }
        viewModelRegister.isEmailExists.observe(this) { status ->
            isBelievableEmail = !status
            showEmailExistAlert(status)
            updateButton()
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

    private fun handleOnClick() {
        binding.btnSignIn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnRegister.setOnClickListener {
            val username = binding.edUsername.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val role = binding.dropdownRole.text.toString()
            viewModelRegister.registerUser(username, email, role, password)
        }
    }

    @SuppressLint("CheckResult")
    private fun handleStreamText() {
        binding.edEmail.addTextChangedListener(
            CustomTextWatcher { text ->
                runOnUiThread {
                    viewModelRegister.checkEmail(text)
                }
            }
        )
        binding.edUsername.addTextChangedListener(
            CustomTextWatcher { text ->
                runOnUiThread {
                    viewModelRegister.checkUsername(text)
                }
            }
        )

        val passwordStream = Observable.create { emitter ->
            binding.edPassword.addTextChangedListener(
                CustomTextWatcher { text ->
                    runOnUiThread {
                        emitter.onNext(text.length < 6)
                    }
                }
            )
        }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }
        val roleStream = Observable.create { emitter ->
            binding.dropdownRole.addTextChangedListener(
                CustomTextWatcher { text ->
                    runOnUiThread {
                        emitter.onNext(text.isEmpty())
                    }
                }
            )
        }
        roleStream.subscribe {
            showRoleAlert(it)
        }
        val invalidFieldsStream = Observable.combineLatest(
            passwordStream,
            roleStream,
        ) { passwordInvalid: Boolean, roleInvalid: Boolean ->
             !passwordInvalid && !roleInvalid
        }
        invalidFieldsStream.subscribe { isValid ->
            isValidButton = isValid
            updateButton()
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateButton() {
        binding.btnRegister.isEnabled = isValidButton && isBelievableEmail && isBelievableUsername
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.edEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showUsernameMinimalAlert(isNotValid: Boolean) {
        binding.edUsername.error = if (isNotValid) getString(R.string.username_not_valid) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.edPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }

    private fun showRoleAlert(isNotValid: Boolean) {
        binding.dropdownRole.error = if (isNotValid) getString(R.string.role_not_valid) else null
    }
}