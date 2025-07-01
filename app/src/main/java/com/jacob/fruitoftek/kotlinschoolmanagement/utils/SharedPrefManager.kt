package com.jacob.fruitoftek.kotlinschoolmanagement.utils

import android.content.Context
import android.content.SharedPreferences
import com.jacob.fruitoftek.kotlinschoolmanagement.model.User

class SharedPrefManager private constructor(private val mCtx: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "fedco_shared_pref"
        private const val KEY_USER_ID = "keyuserid"
        private const val KEY_FIRST_NAME = "keyfirstname"
        private const val KEY_LAST_NAME = "keylastname"
        private const val KEY_EMAIL = "keyemail"
        private const val KEY_ROLE = "keyrole"
        private const val KEY_PHONE = "keyphone"
        private const val KEY_DISTRICT = "keydistrict"
        private const val KEY_COMMUNITY = "keycommunity"
        private const val KEY_COOPERATIVE = "keycooperative"
        private const val KEY_ADDRESS = "keyaddress"
        private const val KEY_PHOTO = "keyphoto"

        @Volatile
        private var INSTANCE: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val sharedPreferences: SharedPreferences by lazy {
        mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(user: User) {
        sharedPreferences.edit().apply {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_FIRST_NAME, user.firstName)
            putString(KEY_LAST_NAME, user.lastName)
            putString(KEY_EMAIL, user.email)
            putString(KEY_ROLE, user.role)
            putString(KEY_PHONE, user.phone)
            putString(KEY_DISTRICT, user.district)
            putString(KEY_COMMUNITY, user.community)
            putString(KEY_COOPERATIVE, user.cooperative)
            putString(KEY_ADDRESS, user.address)
            putString(KEY_PHOTO, user.photo)
            apply()
        }
    }

    fun getUser(): User? {
        val id = sharedPreferences.getInt(KEY_USER_ID, -1)
        if (id == -1) return null

        return User(
            id = id,
            firstName = sharedPreferences.getString(KEY_FIRST_NAME, "") ?: "",
            lastName = sharedPreferences.getString(KEY_LAST_NAME, "") ?: "",
            role = sharedPreferences.getString(KEY_ROLE, "") ?: "",
            email = sharedPreferences.getString(KEY_EMAIL, "") ?: "",
            phone = sharedPreferences.getString(KEY_PHONE, null),
            district = sharedPreferences.getString(KEY_DISTRICT, "") ?: "",
            community = sharedPreferences.getString(KEY_COMMUNITY, "") ?: "",
            cooperative = sharedPreferences.getString(KEY_COOPERATIVE, "") ?: "",
            address = sharedPreferences.getString(KEY_ADDRESS, null),
            photo = sharedPreferences.getString(KEY_PHOTO, null)
        )
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getInt(KEY_USER_ID, -1) != -1
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

