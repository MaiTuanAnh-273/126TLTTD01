package com.example.studentprofile.model

import java.io.Serializable

data class Student(
    val id: String,
    val name: String,
    val className: String,
    val email: String,
    val gpa: Double
) : Serializable {

    val isHonorStudent: Boolean
        get() = gpa >= 3.6

    companion object {
        val DEFAULT = Student(
            id = "22505120005",
            name = "Nguyễn Văn An",
            className = "22CT111",
            email = "an.nv@ute.udn.vn",
            gpa = 3.75
        )
    }
}