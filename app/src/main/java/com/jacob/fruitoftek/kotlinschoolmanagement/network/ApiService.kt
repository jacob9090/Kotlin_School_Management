package com.jacob.fruitoftek.kotlinschoolmanagement.network

import com.jacob.fruitoftek.kotlinschoolmanagement.model.ApiResponse
import com.jacob.fruitoftek.kotlinschoolmanagement.model.Student
import com.jacob.fruitoftek.kotlinschoolmanagement.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth.php")
    fun registerUser(@Body request: RegisterRequest): Call<ApiResponse>

    @POST("auth.php")
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse>

    @POST("auth.php")
    fun getUserDetails(@Body request: UserDetailsRequest): Call<User>

    @GET("students")
    suspend fun getStudents(): List<Student>

    @POST("students/upsert")
    suspend fun addOrUpdateStudent(@Body student: Student): Student
}

data class RegisterRequest(
    val action: String = "register",
    val first_name: String,
    val last_name: String,
    val role: String,
    val user_phone: String,
    val user_district: String,
    val user_community: String,
    val user_cooperative: String,
    val user_address: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val action: String = "login",
    val email: String,
    val password: String
)

data class UserDetailsRequest(
    val action: String = "getUserDetails",
    val user_id: Int
)
