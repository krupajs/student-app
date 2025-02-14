package com.example.studentapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.studentapp.data.db.DatabaseHelper
import com.example.studentapp.data.models.Student

class StudentEditActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var courseEditText: EditText
    private lateinit var gradeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var discardButton: Button
    private var studentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_edit)

        dbHelper = DatabaseHelper(this)

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        courseEditText = findViewById(R.id.courseEditText)
        gradeEditText = findViewById(R.id.gradeEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        saveButton = findViewById(R.id.saveButton)
        discardButton = findViewById(R.id.discardButton)

        // Get Student ID from Intent
        studentId = intent.getLongExtra("studentId", -1)
        if (studentId != -1L) {
            val student = dbHelper.getStudentById(studentId)
            student?.let { populateFields(it) }
        }

        // Save button logic
        saveButton.setOnClickListener {
            saveStudentChanges()
        }

        // Discard button logic


        discardButton.setOnClickListener {
            showCustomToast("Changes Discarded", R.drawable.ic_discard_toast)
            navigateToStudentView()
        }

    }

    private fun populateFields(student: Student) {
        nameEditText.setText(student.name)
        courseEditText.setText(student.course)
        gradeEditText.setText(student.grade)
        emailEditText.setText(student.email)
        phoneEditText.setText(student.phone)
    }

    private fun saveStudentChanges() {
        val updatedStudent = Student(
            id = studentId,
            name = nameEditText.text.toString(),
            course = courseEditText.text.toString(),
            grade = gradeEditText.text.toString(),
            email = emailEditText.text.toString(),
            phone = phoneEditText.text.toString()
        )

        dbHelper.updateStudent(updatedStudent)
//        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
        showCustomToast("Saved Successfully", R.drawable.ic_save_toast)
        navigateToStudentView()
    }

    private fun navigateToStudentView() {
        val intent = Intent(this, StudentViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun showCustomToast(message: String, iconResId: Int) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val toastIcon = layout.findViewById<ImageView>(R.id.toastIcon)
        val toastText = layout.findViewById<TextView>(R.id.toastMessage)

        toastIcon.setImageResource(iconResId)
        toastText.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }




}
