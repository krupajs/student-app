package com.example.studentapp.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.studentapp.data.models.Student
//import com.google.firestore.v1.Cursor
import android.database.Cursor

class StudentDao(private val db: SQLiteDatabase) {

    companion object {
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "student_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_COURSE = "course"
        const val COLUMN_GRADE = "grade"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"

        // Create table SQL query
        val CREATE_TABLE_QUERY = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_COURSE TEXT NOT NULL,
                $COLUMN_GRADE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PHONE TEXT UNIQUE,
                $COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_UPDATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
    }

    fun insert(student: Student): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_COURSE, student.course)
            put(COLUMN_GRADE, student.grade)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }

        return try {
            db.insertOrThrow(TABLE_NAME, null, values)
        } catch (e: Exception) {
            -1
        }
    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()

        // Query all students from the database
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_COURSE, COLUMN_GRADE, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_CREATED_AT, COLUMN_UPDATED_AT),
            null, null, null, null, null
        )

        // Iterate over the result and add each student to the list
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val course = it.getString(it.getColumnIndexOrThrow(COLUMN_COURSE))
                val grade = it.getString(it.getColumnIndexOrThrow(COLUMN_GRADE))
                val email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL))
                val phone = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE))
                val createdAt = it.getString(it.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                val updatedAt = it.getString(it.getColumnIndexOrThrow(COLUMN_UPDATED_AT))

                students.add(Student(id, name, course, grade, email, phone, createdAt, updatedAt))
            }
        }

        return students
    }
    fun deleteStudent(studentId: Long): Boolean {
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(studentId.toString())) > 0
    }

    fun getStudentById(studentId: Long): Student? {
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_COURSE, COLUMN_GRADE, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_CREATED_AT, COLUMN_UPDATED_AT),
            "$COLUMN_ID = ?",
            arrayOf(studentId.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val student = Student(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                course = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE)),
                grade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
            )
            cursor.close()
            student
        } else {
            cursor.close()
            null
        }
    }

    fun updateStudent(student: Student): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_COURSE, student.course)
            put(COLUMN_GRADE, student.grade)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
            put(COLUMN_UPDATED_AT, System.currentTimeMillis().toString()) // Update timestamp
        }

        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(student.id.toString())) > 0
    }


}