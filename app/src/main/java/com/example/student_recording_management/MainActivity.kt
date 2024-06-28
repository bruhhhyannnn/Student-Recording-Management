package com.example.student_recording_management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_recording_management.adapter.StudentAdapter
import com.example.student_recording_management.data_model.StudentModel
import com.example.student_recording_management.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            getStudentInformation()
        }
        binding.searchButton.setOnClickListener {
            getStudentNumber()
        }
        binding.displayButton.setOnClickListener {
            displayStudent()
        }
    }

    private fun getStudentInformation() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_add, null)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Add Student")
            .create()
        dialog.show()

        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            val studentNumber = view.findViewById<EditText>(R.id.student_number).text.toString()
            val fullName = view.findViewById<EditText>(R.id.full_name).text.toString()
            val age = view.findViewById<EditText>(R.id.age).text.toString()
            val birthday = view.findViewById<EditText>(R.id.birthday).text.toString()
            val barangayName = view.findViewById<EditText>(R.id.barangay_name).text.toString()
            val cityName = view.findViewById<EditText>(R.id.city_name).text.toString()
            val provinceName = view.findViewById<EditText>(R.id.province_name).text.toString()
            val phoneNumber = view.findViewById<EditText>(R.id.phone_number).text.toString()
            val emailAddress = view.findViewById<EditText>(R.id.email_address).text.toString()
            val motherName = view.findViewById<EditText>(R.id.mother_name).text.toString()
            val fatherName = view.findViewById<EditText>(R.id.father_name).text.toString()

            if (studentNumber.isEmpty()) {
                view.findViewById<EditText>(R.id.student_number).error = "Student Number is required"
                return@setOnClickListener
            }
            else if (fullName.isEmpty()) {
                view.findViewById<EditText>(R.id.full_name).error = "Full Name is required"
                return@setOnClickListener
            }
            else if (age.isEmpty()) {
                view.findViewById<EditText>(R.id.age).error = "Age is required"
                return@setOnClickListener
            }
            else if (birthday.isEmpty()) {
                view.findViewById<EditText>(R.id.birthday).error = "Birthday is required"
                return@setOnClickListener
            }
            else if (barangayName.isEmpty()) {
                view.findViewById<EditText>(R.id.barangay_name).error = "Barangay Name is required"
                return@setOnClickListener
            }
            else if (cityName.isEmpty()) {
                view.findViewById<EditText>(R.id.city_name).error = "City Name is required"
                return@setOnClickListener
            }
            else if (provinceName.isEmpty()) {
                view.findViewById<EditText>(R.id.province_name).error = "Province Name is required"
                return@setOnClickListener
            }
            else if (phoneNumber.isEmpty()) {
                view.findViewById<EditText>(R.id.phone_number).error = "Phone Number is required"
                return@setOnClickListener
            }
            else if (emailAddress.isEmpty()) {
                view.findViewById<EditText>(R.id.email_address).error = "Email Address is required"
                return@setOnClickListener
            }
            else if (motherName.isEmpty()) {
                view.findViewById<EditText>(R.id.mother_name).error = "Mother Name is required"
                return@setOnClickListener
            }
            else if (fatherName.isEmpty()) {
                view.findViewById<EditText>(R.id.father_name).error = "Father Name is required"
                return@setOnClickListener
            }

            val student = hashMapOf(
                "studentNumber" to studentNumber,
                "fullName" to fullName,
                "age" to age,
                "birthday" to birthday,
                "barangayName" to barangayName,
                "cityName" to cityName,
                "provinceName" to provinceName,
                "phoneNumber" to phoneNumber,
                "emailAddress" to emailAddress,
                "motherName" to motherName,
                "fatherName" to fatherName
            )

            addStudentDataToFirebase(student)
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun addStudentDataToFirebase(student: HashMap<String, String>) {
        val studentNumber = student["studentNumber"]
        Toast.makeText(this, "Added $studentNumber", Toast.LENGTH_SHORT).show()
    }

    private fun getStudentNumber() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_search, null)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Search Student")
            .create()
        dialog.show()

        view.findViewById<Button>(R.id.search_button).setOnClickListener {
            val studentNumber = view.findViewById<EditText>(R.id.student_number).text.toString()
            searchStudentNumFromFirebase(studentNumber)
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun searchStudentNumFromFirebase(studentNumber: String) {
        Toast.makeText(this, "Found $studentNumber", Toast.LENGTH_SHORT).show()
    }

    private fun displayStudent() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_display, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val studentList = ArrayList<StudentModel>()
        recyclerView.adapter = StudentAdapter(studentList)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Display Student")
            .create()
        dialog.show()

        val tempData = mapOf(
            "21-020131" to "John Doe",
            "21-020132" to "Jane Doe",
            "21-020133" to "Michael Doe",
            "21-020134" to "Emily Doe",
            "21-020135" to "David Doe",
            "21-020136" to "John Smith",
            "21-020137" to "Jane Smith",
            "21-020138" to "Michael Smith",
            "21-020139" to "Emily Smith",
            "21-020140" to "David Smith",
        )
        tempData.forEach {
            studentList.add(StudentModel(it.key, it.value))
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}