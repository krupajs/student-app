package com.example.studentapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.studentapp.data.db.DatabaseHelper
import com.example.studentapp.data.models.Student

class StudentViewActivity : AppCompatActivity() {
    private lateinit var container: LinearLayout
    private lateinit var emptyView: TextView
    private lateinit var searchBar: EditText
    private lateinit var dbHelper: DatabaseHelper
    private var allStudents: List<Student> = listOf()
    private var filteredStudents: List<Student> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_view)

        // Set up ActionBar
        supportActionBar?.apply {
            title = "Student Records"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize views and database
        container = findViewById(R.id.container)
        emptyView = findViewById(R.id.emptyView)
        searchBar = findViewById(R.id.searchBar)
        dbHelper = DatabaseHelper(this)

        // Set up search functionality
        setupSearch()

        // Initial load of students
        loadStudents()
    }

    private fun setupSearch() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterStudents(s?.toString() ?: "")
            }
        })
    }

    private fun filterStudents(query: String) {
        if (query.isEmpty()) {
            filteredStudents = allStudents
        } else {
            val lowercaseQuery = query.lowercase()
            filteredStudents = allStudents.filter { student ->
                student.name.lowercase().contains(lowercaseQuery) ||
                        student.email.lowercase().contains(lowercaseQuery) ||
//                        student.id.toString().contains(lowercaseQuery) ||
                        student.phone?.lowercase()?.contains(lowercaseQuery) == true ||
//                        student.address?.lowercase()?.contains(lowercaseQuery) == true ||
                        student.course?.lowercase()?.contains(lowercaseQuery) == true ||
                        student.grade?.lowercase()?.contains(lowercaseQuery) == true
            }
        }
        updateUI()
    }

    private fun updateUI() {
        container.removeAllViews()

        if (filteredStudents.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            emptyView.text = if (allStudents.isEmpty()) {
                "No student records found"
            } else {
                "No matching records found"
            }
        } else {
            emptyView.visibility = View.GONE
            filteredStudents.forEach { student -> addStudentCard(student) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadStudents() {
        // Load students from database
        allStudents = dbHelper.getAllStudents()
        filteredStudents = allStudents
        updateUI()
    }

    private fun addStudentCard(student: Student) {
        val cardView = CardView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            radius = 12f
            setCardBackgroundColor(Color.WHITE)
            setContentPadding(16, 16, 16, 16)
            cardElevation = 6f
            useCompatPadding = true
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER_VERTICAL
        }

        val studentInfo = TextView(this).apply {
            text = "${student.name}\n${student.email}"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        }

        val editButton = Button(this).apply {
            text = "Edit"
            setBackgroundColor(Color.parseColor("#4CAF50"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                val intent = Intent(context, StudentEditActivity::class.java).apply {
                    putExtra("studentId", student.id)
                }
                context.startActivity(intent)
            }
        }

        val deleteButton = Button(this).apply {
            text = "Delete"
            setBackgroundColor(Color.RED)
            setTextColor(Color.WHITE)
            setOnClickListener {
                dbHelper.deleteStudent(student.id)
                loadStudents()
            }
        }

        // Navigate to StudentDetailActivity when the student name is clicked
        studentInfo.setOnClickListener {
            val intent = Intent(container.context, StudentDetailActivity::class.java).apply {
                putExtra("studentId", student.id)
            }
            container.context.startActivity(intent)
        }

        layout.addView(studentInfo)
        layout.addView(editButton)
        layout.addView(deleteButton)
        cardView.addView(layout)
        container.addView(cardView)
    }

    override fun onResume() {
        super.onResume()
        // Reload students when returning to this activity
        loadStudents()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}