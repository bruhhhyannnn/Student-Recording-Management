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
import com.example.student_recording_management.data_model.StudentModel
import com.example.student_recording_management.databinding.ActivityStudentDisplayBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class StudentDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDisplayBinding
    private lateinit var documentID: String
    private lateinit var student: StudentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        documentID = intent.getStringExtra("documentID").toString()

        getStudentDataFromFirebase()

        binding.editButton.setOnClickListener {
            editDialog()
        }
        binding.deleteButton.setOnClickListener {
            deleteDialog()
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.studentInformation.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.studentInformation.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getStudentDataFromFirebase() {
        setInProgress(true)
        Firebase.firestore
            .collection("students")
            .document(documentID)
            .get()
            .addOnSuccessListener {
                if (it.exists() && it != null) {
                    student = it.toObject(StudentModel::class.java)!!
                    updateUI(student)
                    setInProgress(false)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                setInProgress(false)
            }
    }

    private fun updateUI(student: StudentModel) {
        binding.studentNumberValue.text = student.studentNumber
        binding.fullNameValue.text = student.fullName
        binding.ageValue.text = student.age
        binding.birthdayValue.text = student.birthday
        binding.fullAddressValue.text = student.barangayName + ", " + student.cityName + ", " + student.provinceName
        binding.phoneNumberValue.text = student.phoneNumber
        binding.emailAddressValue.text = student.emailAddress
        binding.motherNameValue.text = student.motherName
        binding.fatherNameValue.text = student.fatherName
    }

    private fun editDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_add, null)
        view.findViewById<Button>(R.id.add_button).text = "Save"

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setTitle("Edit Student")
            .create()
        dialog.show()

        bindDialogUI(view)

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

            val student = mapOf(
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

            updateDataToFirebase(student, view, dialog)
        }
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun bindDialogUI(view: View) {
        view.findViewById<EditText>(R.id.student_number).setText(student.studentNumber)
        view.findViewById<EditText>(R.id.full_name).setText(student.fullName)
        view.findViewById<EditText>(R.id.age).setText(student.age)
        view.findViewById<EditText>(R.id.birthday).setText(student.birthday)
        view.findViewById<EditText>(R.id.barangay_name).setText(student.barangayName)
        view.findViewById<EditText>(R.id.city_name).setText(student.cityName)
        view.findViewById<EditText>(R.id.province_name).setText(student.provinceName)
        view.findViewById<EditText>(R.id.phone_number).setText(student.phoneNumber)
        view.findViewById<EditText>(R.id.email_address).setText(student.emailAddress)
        view.findViewById<EditText>(R.id.mother_name).setText(student.motherName)
        view.findViewById<EditText>(R.id.father_name).setText(student.fatherName)
    }

    private fun updateDataToFirebase(student: Map<String, String>, view: View, dialog: AlertDialog) {
        view.findViewById<Button>(R.id.add_button).visibility = View.GONE
        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        Firebase.firestore
            .collection("students")
            .document(documentID)
            .update(student)
            .addOnSuccessListener {
                Toast.makeText(this, "Student Updated", Toast.LENGTH_SHORT).show()
                view.findViewById<Button>(R.id.add_button).visibility = View.VISIBLE
                view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                dialog.dismiss()
                getStudentDataFromFirebase()
            }
    }

    private fun deleteDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setMessage("Are you sure you want to delete this student?")
        builder.setPositiveButton("Delete") { _, _, ->
            deleteStudentFromFirebase()
        }
        builder.setNegativeButton("Cancel") { dialog, _, ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteStudentFromFirebase() {
        Firebase.firestore
            .collection("students")
            .document(documentID)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Student Deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }
}