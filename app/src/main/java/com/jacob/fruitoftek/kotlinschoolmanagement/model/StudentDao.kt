package com.jacob.fruitoftek.kotlinschoolmanagement.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Dao
interface StudentDao {

    data class StudentDto(
        @SerializedName("student_id") val studentId: Int,
        @SerializedName("name") val name: String,
        @SerializedName("image_url") val imageUrl: String?
    )

    @Query("SELECT * FROM students")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students")
    suspend fun getAllStudentsList(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAllStudents(students: List<Student>) {
        deleteAll()
        insertStudents(students)
    }
}
