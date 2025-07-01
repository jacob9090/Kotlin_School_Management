package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacob.fruitoftek.kotlinschoolmanagement.R
import com.jacob.fruitoftek.kotlinschoolmanagement.model.AppDatabase
import com.jacob.fruitoftek.kotlinschoolmanagement.model.StudentRepository
import com.jacob.fruitoftek.kotlinschoolmanagement.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val api = RetrofitClient.instance  // <-- FIXED LINE
        val repo = StudentRepository(dao, api)

        repo.allStudents.observe(this) { students ->
            adapter.setStudents(students)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repo.refreshStudentsFromServer()
            } catch (e: Exception) {
                // Handle offline, no crash!
            }
        }
    }
}
