package com.example.student_recording_management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_recording_management.adapter.StudentAdapter
import com.example.student_recording_management.data_model.StudentModel
import com.example.student_recording_management.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindTotalStudent()

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

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.totalStudents.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.totalStudents.visibility = View.VISIBLE
        }
    }

    private fun bindTotalStudent() {
        setInProgress(true)
        Firebase.firestore
            .collection("students")
            .get()
            .addOnSuccessListener {
                binding.totalStudents.text = it.size().toString()
                setInProgress(false)
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

            addStudentDataToFirebase(student, view, dialog)
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun addStudentDataToFirebase(student: HashMap<String, String>, view: View, dialog: AlertDialog) {
        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.add_button).visibility = View.GONE
        val studentNumber = student["studentNumber"]
        Firebase.firestore
            .collection("students")
            .add(student)
            .addOnSuccessListener {
                Firebase.firestore
                    .collection("students")
                    .document(it.id)
                    .update("documentID", it.id)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added $studentNumber", Toast.LENGTH_SHORT)
                            .show()
                        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                        view.findViewById<Button>(R.id.add_button).visibility = View.VISIBLE
                        dialog.dismiss()
                    }
            }
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
            searchStudentNumberFromFirebase(studentNumber, view)
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun searchStudentNumberFromFirebase(studentNumber: String, view: View) {
        var studentFound = false
        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.search_button).visibility = View.GONE
        Firebase.firestore
            .collection("students")
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it != null) {
                    for (document in it) {
                        if (document.getString("studentNumber") == studentNumber) {
                            val student = document.toObject(StudentModel::class.java)
                            val intent = Intent(baseContext, StudentDisplayActivity::class.java).apply {
                                putExtra("documentID", student.documentID)
                            }
                            startActivity(intent)
                            view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                            view.findViewById<Button>(R.id.search_button).visibility = View.GONE
                            studentFound = true
                            break
                        }
                    }
                    if (!studentFound) {
                        view.findViewById<EditText>(R.id.student_number).error = "Student Number Not Found"
                        Toast.makeText(this, "Student Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun displayStudent() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_display, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val studentList = ArrayList<StudentModel>()
        val adapter = StudentAdapter(studentList)
        recyclerView.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Display Student")
            .create()
        dialog.show()

        getStudentsFromFirebase(studentList, view)
    }

    private fun getStudentsFromFirebase(studentList: ArrayList<StudentModel>, view: View) {
        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        view.findViewById<RecyclerView>(R.id.recycler_view).visibility = View.GONE
        Firebase.firestore
            .collection("students")
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty && it != null) {
                    for (document in it) {
                        val student = document.toObject(StudentModel::class.java)
                        studentList.add(student)
                    }
                    view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                    view.findViewById<RecyclerView>(R.id.recycler_view).visibility = View.VISIBLE
                }
            }
    }
}

//            if (studentNumber.isEmpty()) {
//                view.findViewById<EditText>(R.id.student_number).error = "Student Number is required"
//                return@setOnClickListener
//            }
//            else if (fullName.isEmpty()) {
//                view.findViewById<EditText>(R.id.full_name).error = "Full Name is required"
//                return@setOnClickListener
//            }
//            else if (age.isEmpty()) {
//                view.findViewById<EditText>(R.id.age).error = "Age is required"
//                return@setOnClickListener
//            }
//            else if (birthday.isEmpty()) {
//                view.findViewById<EditText>(R.id.birthday).error = "Birthday is required"
//                return@setOnClickListener
//            }
//            else if (barangayName.isEmpty()) {
//                view.findViewById<EditText>(R.id.barangay_name).error = "Barangay Name is required"
//                return@setOnClickListener
//            }
//            else if (cityName.isEmpty()) {
//                view.findViewById<EditText>(R.id.city_name).error = "City Name is required"
//                return@setOnClickListener
//            }
//            else if (provinceName.isEmpty()) {
//                view.findViewById<EditText>(R.id.province_name).error = "Province Name is required"
//                return@setOnClickListener
//            }
//            else if (phoneNumber.isEmpty()) {
//                view.findViewById<EditText>(R.id.phone_number).error = "Phone Number is required"
//                return@setOnClickListener
//            }
//            else if (emailAddress.isEmpty()) {
//                view.findViewById<EditText>(R.id.email_address).error = "Email Address is required"
//                return@setOnClickListener
//            }
//            else if (motherName.isEmpty()) {
//                view.findViewById<EditText>(R.id.mother_name).error = "Mother Name is required"
//                return@setOnClickListener
//            }
//            else if (fatherName.isEmpty()) {
//                view.findViewById<EditText>(R.id.father_name).error = "Father Name is required"
//                return@setOnClickListener
//            }