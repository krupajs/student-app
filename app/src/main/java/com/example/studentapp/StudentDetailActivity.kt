package com.example.studentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.studentapp.data.db.DatabaseHelper
import com.example.studentapp.data.models.Student

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var nameText: TextView
    private lateinit var courseText: TextView
    private lateinit var gradeText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        dbHelper = DatabaseHelper(this)

        // Initialize views
        nameText = findViewById(R.id.nameText)
        courseText = findViewById(R.id.courseText)
        gradeText = findViewById(R.id.gradeText)
        emailText = findViewById(R.id.emailText)
        phoneText = findViewById(R.id.phoneText)
        backButton = findViewById(R.id.backButton)

        // Handle back button click
        backButton.setOnClickListener {
            val intent = Intent(this, StudentViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Closes this activity and goes back
            startActivity(intent)
            finish()
        }

        // Get student ID from Intent
        val studentId = intent.getLongExtra("studentId", -1)
        if (studentId != -1L) {
            val student = dbHelper.getStudentById(studentId)
            student?.let {
                displayStudentDetails(it)
            }
        }
    }

    private fun displayStudentDetails(student: Student) {
        nameText.text = "Name: ${student.name}"
        courseText.text = "Course: ${student.course}"
        gradeText.text = "Grade: ${student.grade}"
        emailText.text = "Email: ${student.email}"
        phoneText.text = "Phone: ${student.phone}"
    }
}
