package com.example.student_recording_management.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.student_recording_management.R
import com.example.student_recording_management.data_model.SectionModel

class SectionAdapter(
    private val sections: ArrayList<SectionModel>
): RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_sections, parent, false)
        return SectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.sectionName.text = section.sectionName
        holder.studentLength.text = section.studentLength.toString()

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Section " + section.sectionName + " clicked", Toast.LENGTH_SHORT).show()
        }
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionName : TextView = itemView.findViewById(R.id.section_name)
        val studentLength : TextView = itemView.findViewById(R.id.total_students)
    }
}