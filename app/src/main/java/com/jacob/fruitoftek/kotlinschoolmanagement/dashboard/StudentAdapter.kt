package com.jacob.fruitoftek.kotlinschoolmanagement.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacob.fruitoftek.kotlinschoolmanagement.R
import com.jacob.fruitoftek.kotlinschoolmanagement.model.Student

class StudentAdapter(private var students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textName)
        // Add other fields if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.name.text = "${student.first_name} ${student.last_name}"
        // Bind more fields if needed
    }

    override fun getItemCount(): Int = students.size

    fun setStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
