package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jacob.fruitoftek.kotlinschoolmanagement.R
import com.jacob.fruitoftek.kotlinschoolmanagement.auth.LoginActivity
import com.jacob.fruitoftek.kotlinschoolmanagement.databinding.ActivityUserProfileBinding
import com.jacob.fruitoftek.kotlinschoolmanagement.utils.SharedPrefManager

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = SharedPrefManager.getInstance(this).getUser()
        if (user == null) {
            finish()
            return
        }

        with(binding) {
            tvName.text = "${user.firstName} ${user.lastName}"
            tvEmail.text = user.email.ifEmpty { "Not provided" }

            user.photo?.let { photoUrl ->
                Glide.with(this@UserProfileActivity)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivProfile)
            } ?: ivProfile.setImageResource(R.drawable.ic_profile)

            tvPhone.text = user.phone ?: "Not provided"
            tvRole.text = user.role.ifEmpty { "Not specified" }
            tvDistrict.text = user.district.ifEmpty { "Not specified" }
            tvCommunity.text = user.community.ifEmpty { "Not specified" }
            tvCooperative.text = user.cooperative.ifEmpty { "Not specified" }
            tvAddress.text = user.address ?: "Not provided"

            btnLogout.setOnClickListener {
                SharedPrefManager.getInstance(this@UserProfileActivity).clear()
                val intent = Intent(this@UserProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}