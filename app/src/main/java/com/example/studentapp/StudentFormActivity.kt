package com.example.studentapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.studentapp.data.db.DatabaseHelper
import com.example.studentapp.data.models.Student
import java.util.*

class StudentFormActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etGrade: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var spinnerCourse: Spinner
    private lateinit var btnPickDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnSubmit: Button
    private lateinit var btnReset: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_main)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Initialize UI components
        initializeViews()
        setupSpinner()
        setupClickListeners()
    }

    private fun initializeViews() {
        etName = findViewById(R.id.etName)
        etGrade = findViewById(R.id.etGrade)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        spinnerCourse = findViewById(R.id.spinnerCourse)
        btnPickDate = findViewById(R.id.btnPickDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnReset = findViewById(R.id.btnReset)
    }

    private fun setupSpinner() {
        val courses = arrayOf("Software Systems", "Data Science", "Decision and Computing Sciences", "Artificial Intelligence and Machine Learning","Cyber Security","Theoretical Computer Science")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, courses)
        spinnerCourse.adapter = adapter
    }

    private fun setupClickListeners() {
        btnPickDate.setOnClickListener {
            showDatePicker()
        }

        btnReset.setOnClickListener {
            resetForm()
        }

        btnSubmit.setOnClickListener {
            if (validateForm()) {
                saveStudentToDatabase()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            tvSelectedDate.text = "$dayOfMonth/${month + 1}/$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun resetForm() {
        etName.setText("")
        etGrade.setText("")
        etEmail.setText("")
        etPhone.setText("")
        tvSelectedDate.text = ""
        spinnerCourse.setSelection(0)
    }

    private fun saveStudentToDatabase() {
        val name = etName.text.toString()
        val grade = etGrade.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val course = spinnerCourse.selectedItem.toString()

        // Create Student object with all required parameters
        val student = Student(
            id = 0,  // This is nullable and will be auto-generated
            name = name,
            course = course,
            grade = grade,
            email = email,
            phone = phone,
            createdAt = null,  // These will be set by SQLite
            updatedAt = null   // These will be set by SQLite
        )

        val result = dbHelper.addStudent(student)

        if (result != -1L) {
            Toast.makeText(this, "Student Added Successfully!", Toast.LENGTH_SHORT).show()
            resetForm()
        } else {
            Toast.makeText(this, "Failed to add student. Email or phone may already exist.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun validateForm(): Boolean {
        if (etName.text.isEmpty()) {
            etName.error = "Name is required"
            return false
        }

        if (etGrade.text.isEmpty()) {
            etGrade.error = "Grade is required"
            return false
        }

        if (etEmail.text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()) {
            etEmail.error = "Valid email required"
            return false
        }

        if (etPhone.text.isEmpty() || etPhone.text.length != 10) {
            etPhone.error = "Enter a valid 10-digit phone number"
            return false
        }

        return true
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}