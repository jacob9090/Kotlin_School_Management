package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import com.jacob.fruitoftek.kotlinschoolmanagement.auth.LoginActivity
import com.jacob.fruitoftek.kotlinschoolmanagement.utils.SharedPrefManager

import com.jacob.fruitoftek.kotlinschoolmanagement.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = SharedPrefManager.getInstance(this).getUser()

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Set user info
        binding.userFNameTv.text = user.firstName  // or user.fname if that's the property
        binding.userEmailTv.text = user.email

        binding.userProfileIv.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        binding.viewStudentCv.setOnClickListener {
            startActivity(Intent(this, StudentListActivity::class.java))
        }
    }
}