package com.jacob.fruitoftek.kotlinschoolmanagement.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.jacob.fruitoftek.kotlinschoolmanagement.model.ApiResponse
import com.jacob.fruitoftek.kotlinschoolmanagement.network.LoginRequest
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RetrofitClient
import com.jacob.fruitoftek.kotlinschoolmanagement.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.jacob.fruitoftek.kotlinschoolmanagement.databinding.ActivityLoginBinding

import android.os.Build
import android.view.WindowInsetsController
import android.view.WindowManager
import com.jacob.fruitoftek.kotlinschoolmanagement.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(
                android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars()
            )
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email required"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password required"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            val request = LoginRequest(email = email, password = password)

            RetrofitClient.instance.loginUser(request)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful && response.body()?.user != null) {
                            SharedPrefManager.getInstance(applicationContext)
                                .saveUser(response.body()!!.user!!)
                            startActivity(Intent(applicationContext, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.message ?: "Login failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                })
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}