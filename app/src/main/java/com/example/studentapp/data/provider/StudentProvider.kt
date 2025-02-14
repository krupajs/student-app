package com.example.studentapp.data.provider

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.studentapp.data.dao.StudentDao
import com.example.studentapp.data.db.DatabaseHelper

class StudentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.studentapp.provider"
        private const val STUDENTS_TABLE = "students"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$STUDENTS_TABLE")

        private const val STUDENTS = 1
        private const val STUDENT_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, STUDENTS_TABLE, STUDENTS)
            addURI(AUTHORITY, "$STUDENTS_TABLE/#", STUDENT_ID)
        }
    }

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        context?.let {
            dbHelper = DatabaseHelper(it)
            return true
        }
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor? = when (uriMatcher.match(uri)) {
            STUDENTS -> db.query(StudentDao.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            STUDENT_ID -> {
                val id = uri.lastPathSegment ?: return null
                db.query(StudentDao.TABLE_NAME, projection, "id=?", arrayOf(id), null, null, null)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        cursor?.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw SecurityException("Insert operation is not allowed.")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw SecurityException("Update operation is not allowed.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw SecurityException("Delete operation is not allowed.")
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            STUDENTS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$STUDENTS_TABLE"
            STUDENT_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.$STUDENTS_TABLE"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
