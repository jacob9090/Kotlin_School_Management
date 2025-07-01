package com.jacob.fruitoftek.kotlinschoolmanagement.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import com.jacob.fruitoftek.kotlinschoolmanagement.model.ApiResponse
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RegisterRequest
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.jacob.fruitoftek.kotlinschoolmanagement.databinding.ActivityRegisterBinding
import android.util.Log
import android.view.View
import java.net.SocketTimeoutException

import com.jacob.fruitoftek.kotlinschoolmanagement.network.*
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            attemptRegistration()
        }
    }

    private fun attemptRegistration() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val district = binding.etDistrict.text.toString().trim()
        val community = binding.etCommunity.text.toString().trim()
        val cooperative = binding.etCooperative.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (!validateInputs(firstName, lastName, email, password, confirmPassword)) {
            return
        }

        val request = RegisterRequest(
            first_name = firstName,
            last_name = lastName,
            role = "user",
            user_phone = phone,
            user_district = district,
            user_community = community,
            user_cooperative = cooperative,
            user_address = address,
            email = email,
            password = password
        )

        showLoading(true)
        Log.d(TAG, "Attempting registration for: ${request.email}")

        RetrofitClient.instance.registerUser(request)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    showLoading(false)
                    val raw = response.raw()
                    val bodyString = response.errorBody()?.string()

                    Log.d(TAG, "Raw response: $raw")
                    Log.d(TAG, "Error body: $bodyString")

                    try {
                        if (response.isSuccessful && response.body() != null) {
                            handleRegistrationResponse(response.body()!!)
                        } else {
                            showError("Server error: $bodyString")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception parsing response", e)
                        showError("Parse error: ${e.localizedMessage}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    showLoading(false)
                    handleNetworkError(t)
                }
            })
    }

    private fun handleRegistrationResponse(apiResponse: ApiResponse) {
        when {
            apiResponse.status.equals("success", ignoreCase = true) -> {
                Log.i(TAG, "Registration successful")
                showSuccess("Registration successful! Please login.")
                finish()
            }
            !apiResponse.message.isNullOrEmpty() -> {
                Log.w(TAG, "Registration failed: ${apiResponse.message}")
                showError(apiResponse.message)
            }
            else -> {
                Log.w(TAG, "Unexpected success response: $apiResponse")
                showError("Unexpected server response")
            }
        }
    }

    private fun handleErrorResponse(response: Response<ApiResponse>) {
        val errorCode = response.code()
        val errorBody = try {
            response.errorBody()?.string() ?: "No error details"
        } catch (e: Exception) {
            "Failed to read error details"
        }

        Log.e(TAG, "Registration failed. Code: $errorCode, Body: $errorBody")

        when (errorCode) {
            400 -> showError("Invalid request: $errorBody")
            401 -> showError("Unauthorized")
            403 -> showError("Forbidden")
            404 -> showError("Endpoint not found")
            409 -> showError("Email already registered")
            500 -> showError("Server error: $errorBody")
            else -> showError("Error $errorCode: $errorBody")
        }
    }

    private fun handleNetworkError(t: Throwable) {
        when (t) {
            is SocketTimeoutException -> {
                Log.e(TAG, "Connection timeout")
                showError("Connection timeout. Please try again.")
            }
            is HttpException -> {
                Log.e(TAG, "HTTP error", t)
                showError("Server error: ${t.message}")
            }
            is EmptyResponseException -> {
                Log.e(TAG, "Empty response")
                showError("Server returned empty response")
            }
            is NetworkException -> {
                Log.e(TAG, "Network error", t)
                showError(t.message ?: "Network error")
            }
            is IOException -> {
                Log.e(TAG, "IO error", t)
                showError("Network error: ${t.message}")
            }
            else -> {
                Log.e(TAG, "Unexpected error", t)
                showError("Unexpected error: ${t.message}")
            }
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            binding.etFirstName.error = "First name required"
            binding.etFirstName.requestFocus()
            isValid = false
        }

        if (lastName.isEmpty()) {
            binding.etLastName.error = "Last name required"
            binding.etLastName.requestFocus()
            isValid = false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email required"
            binding.etEmail.requestFocus()
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            binding.etEmail.requestFocus()
            isValid = false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password required"
            binding.etPassword.requestFocus()
            isValid = false
        } else if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            binding.etPassword.requestFocus()
            isValid = false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords don't match"
            binding.etConfirmPassword.requestFocus()
            isValid = false
        }

        return isValid
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}