package com.girendi.tobamembers.persentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.girendi.tobamembers.databinding.ActivitySplashBinding
import com.girendi.tobamembers.persentation.admin.AdminActivity
import com.girendi.tobamembers.persentation.login.LoginActivity
import com.girendi.tobamembers.persentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModelSplash: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModelSplash.userSession.observe(this) { user ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (user == null) {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                } else {
                    if (user.role == "Admin") {
                        startActivity(Intent(this@SplashActivity, AdminActivity::class.java))
                    } else {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }
                }
                finish()
            }, 3000)
        }
    }
}