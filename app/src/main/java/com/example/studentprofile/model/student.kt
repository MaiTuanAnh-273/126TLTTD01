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
            id = "2415141122125",
            name = "Mai Tuấn Anh",
            className = "126TLTTD01",
            email = "2415141122125@sv.ute.udn.vn",
            gpa = 3.1
        )
    }
}