package com.jacob.fruitoftek.kotlinschoolmanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: Int,
    val user_id: String,
    val first_name: String,
    val last_name: String,
    val gender: String,
    val dob: String?,
    val nationality: String?,
    val language_preferences: String?,
    val photo: String?,
    val cert_id: String?,
    val email: String?,
    val student_phone: String?,
    val password: String?,
    val admission_date: String?,
    val current_class: String?,
    val previous_school: String?,
    val document_name: String?,
    val cert_photo: String?,
    val health_conditions: String,
    val on_medications: String,
    val blood_type: String,
    val take_immunization: String,
    val dietary_restrictions: String,
    val restricted_diets: String?,
    val has_disability: String,
    val disability_type: String?,
    val fathers_name: String?,
    val fathers_occupation: String?,
    val fathers_contact: String?,
    val mothers_name: String?,
    val mothers_occupation: String?,
    val mothers_contact: String?,
    val guardians_name: String?,
    val guardians_occupation: String?,
    val guardians_contact: String?,
    val on_create: String?,
    val on_update: String?
)
