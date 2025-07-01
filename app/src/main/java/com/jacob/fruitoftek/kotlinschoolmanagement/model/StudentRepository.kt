package com.jacob.fruitoftek.kotlinschoolmanagement.model

import androidx.lifecycle.LiveData
import com.jacob.fruitoftek.kotlinschoolmanagement.network.ApiService

class StudentRepository(
    private val dao: StudentDao,
    private val api: ApiService
) {
    val allStudents: LiveData<List<Student>> = dao.getAllStudents()

    suspend fun refreshStudentsFromServer() {
        // Fetch from API, update local DB
        val students = api.getStudents()
        dao.deleteAll()
        dao.insertStudents(students)
    }

    suspend fun addOrUpdateStudent(student: Student) {
        api.addOrUpdateStudent(student) // Upsert to server
        dao.insertStudent(student)      // Update local DB
    }
}