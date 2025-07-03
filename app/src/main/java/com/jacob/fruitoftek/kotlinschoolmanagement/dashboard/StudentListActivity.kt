package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacob.fruitoftek.kotlinschoolmanagement.R
import com.jacob.fruitoftek.kotlinschoolmanagement.model.AppDatabase
import com.jacob.fruitoftek.kotlinschoolmanagement.model.StudentRepository
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RetrofitClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.ExistingWorkPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import com.jacob.fruitoftek.kotlinschoolmanagement.model.StudentSyncWorker

class StudentListActivity : ComponentActivity() {
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStudents)
        adapter = StudentAdapter(listOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val dao = AppDatabase.getDatabase(this).studentDao()
        val api = RetrofitClient.instance
        val repo = StudentRepository(dao, api)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val work = OneTimeWorkRequestBuilder<StudentSyncWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            "student_sync",
            ExistingWorkPolicy.KEEP,
            work
        )

        repo.allStudents.observe(this) { students ->
            adapter.setStudents(students)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                repo.refreshStudentsFromServer()
            } catch (e: Exception) {
                // Handle offline, no crash!
            }
        }
    }
}
