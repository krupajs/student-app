package com.example.studentapp.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentapp.data.dao.StudentDao
import com.example.studentapp.data.models.Student

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(StudentDao.CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${StudentDao.TABLE_NAME}")
        onCreate(db)
    }

    fun addStudent(student: Student): Long {
        return StudentDao(writableDatabase).insert(student)
    }

    fun getAllStudents(): List<Student> {
        return StudentDao(readableDatabase).getAllStudents()
    }

    fun deleteStudent(studentId: Long): Boolean {
        return StudentDao(writableDatabase).deleteStudent(studentId)
    }

    fun getStudentById(studentId: Long): Student? {
        return StudentDao(readableDatabase).getStudentById(studentId)
    }

    fun updateStudent(student: Student): Boolean {
        return StudentDao(writableDatabase).updateStudent(student)
    }
}