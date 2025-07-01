package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacob.fruitoftek.kotlinschoolmanagement.model.User

import com.bumptech.glide.Glide
import com.jacob.fruitoftek.kotlinschoolmanagement.R
import com.jacob.fruitoftek.kotlinschoolmanagement.databinding.FragmentProfileBinding

class UserProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    companion object {
        private const val ARG_USER = "user"

        fun newInstance(user: User): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER) ?: throw IllegalStateException("User argument is required")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUserData()
    }

    private fun populateUserData() {
        with(binding) {
            // Set user name and email
            tvName.text = "${user.firstName} ${user.lastName}"
            tvEmail.text = user.email ?: "Not provided"

            // Load profile photo if available
            user.photo?.let { photoUrl ->
                Glide.with(this@UserProfileFragment)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(ivProfile)
            } ?: run {
                ivProfile.setImageResource(R.drawable.ic_profile)
            }

            // Set other user details
            tvPhone.text = user.phone ?: "Not provided"
            tvRole.text = user.role ?: "Not specified"
            tvDistrict.text = user.district.ifEmpty { "Not specified" }
            tvCommunity.text = user.community.ifEmpty { "Not specified" }
            tvCooperative.text = user.cooperative.ifEmpty { "Not specified" }
            tvAddress.text = user.address ?: "Not provided"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}