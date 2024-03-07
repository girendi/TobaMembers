package com.girendi.tobamembers.persentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.girendi.tobamembers.R
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.databinding.ActivityLoginBinding
import com.girendi.tobamembers.persentation.admin.AdminActivity
import com.girendi.tobamembers.persentation.main.MainActivity
import com.girendi.tobamembers.persentation.register.RegisterActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelLogin: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        handleStreamText()
        handleOnClick()
        handleObserveViewModel()
    }

    private fun handleObserveViewModel() {
        viewModelLogin.validateLogin.observe(this) { user ->
            if (user != null) {
                if (user.role == "Admin") {
                    startActivity(Intent(this, AdminActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
        }
        viewModelLogin.uiState.observe(this) { state ->
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

    private fun handleOnClick() {
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            viewModelLogin.userLogin(email, password)
        }
    }

    @SuppressLint("CheckResult")
    private fun handleStreamText() {
        val emailStream = RxTextView.textChanges(binding.edEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.edPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }
        val invalidFieldsStream = Observable.combineLatest(emailStream, passwordStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean ->
            !emailInvalid && !passwordInvalid
        }

        invalidFieldsStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.edEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.edPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }
}