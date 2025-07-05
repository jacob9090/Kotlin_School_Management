package com.jacob.fruitoftek.kotlinschoolmanagement.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.jacob.fruitoftek.kotlinschoolmanagement.network.ApiService
import androidx.work.*

class StudentRepository(
    private val dao: StudentDao,
    private val api: ApiService,
    private val context: Context
) {
    val allStudents: LiveData<List<Student>> = dao.getAllStudents()

    suspend fun refreshStudentsFromServer() {
        // Fetch from API, update local DB
        val dtos = api.getStudents()
        val students = dtos.map { dto ->
            val parts = dto.name.split(" ", limit = 2)
            val first = parts.getOrNull(0) ?: ""
            val last = parts.getOrNull(1) ?: ""
            Student(
                id = dto.student_id,
                user_id = "",
                first_name = first,
                last_name = last,
                gender = "",
                dob = null,
                nationality = null,
                language_preferences = null,
                photo = dto.image_url,
                cert_id = null,
                email = null,
                student_phone = null,
                password = null,
                admission_date = null,
                current_class = null,
                previous_school = null,
                document_name = null,
                cert_photo = null,
                health_conditions = "",
                on_medications = "",
                blood_type = "",
                take_immunization = "",
                dietary_restrictions = "",
                restricted_diets = null,
                has_disability = "",
                disability_type = null,
                fathers_name = null,
                fathers_occupation = null,
                fathers_contact = null,
                mothers_name = null,
                mothers_occupation = null,
                mothers_contact = null,
                guardians_name = null,
                guardians_occupation = null,
                guardians_contact = null,
                on_create = null,
                on_update = null
            )
        }
        dao.deleteAll()
        dao.insertStudents(students)
    }

    suspend fun addOrUpdateStudent(student: Student) {
        try {
            api.addOrUpdateStudent(student) // Upsert to server
        } catch (_: Exception) {
            // Ignore network errors; worker will retry when online
        }
        dao.insertStudent(student)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val work = OneTimeWorkRequestBuilder<StudentSyncWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "student_sync",
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    // Push all local data to the server then refresh from the API
    suspend fun syncStudents() {
        val local = dao.getAllStudentsList()
        for (student in local) {
            try {
                addOrUpdateStudent(student)
            } catch (e: Exception) {
                // If any push fails, rethrow to trigger retry
                throw e
            }
        }
        refreshStudentsFromServer()
    }
}