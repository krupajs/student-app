package com.example.studentapp.data.models

data class Student(
    val id: Long ,
    val name: String,
    val course: String,
    val grade: String,
    val email: String,
    val phone: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)