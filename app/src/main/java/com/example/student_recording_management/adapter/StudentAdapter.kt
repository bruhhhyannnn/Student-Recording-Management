package com.example.student_recording_management.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.student_recording_management.R
import com.example.student_recording_management.StudentDisplayActivity
import com.example.student_recording_management.data_model.StudentModel

class StudentAdapter(
    private val studentList: ArrayList<StudentModel>
): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_students, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentNumber.text = student.studentNumber
        holder.fullName.text = student.fullName

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Document ID: ${student.documentID}", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, StudentDisplayActivity::class.java).apply {
                putExtra("documentID", student.documentID)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentNumber: TextView = itemView.findViewById(R.id.student_number)
        val fullName: TextView = itemView.findViewById(R.id.full_name)
    }
}