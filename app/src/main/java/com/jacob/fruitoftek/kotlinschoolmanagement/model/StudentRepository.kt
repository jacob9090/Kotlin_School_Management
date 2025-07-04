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
        val students = api.getStudents()
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