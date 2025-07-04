package com.jacob.fruitoftek.kotlinschoolmanagement.model

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RetrofitClient

class StudentSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val repo = StudentRepository(db.studentDao(), RetrofitClient.instance, applicationContext)
        return try {
            repo.syncStudents()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}