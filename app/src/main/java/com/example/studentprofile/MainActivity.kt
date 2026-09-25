package com.example.studentprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.studentprofile.databinding.ActivityMainBinding
import com.example.studentprofile.model.Student
import com.example.studentprofile.utils.gone
import com.example.studentprofile.utils.show
import com.example.studentprofile.utils.toAcademicRanking
import com.example.studentprofile.utils.toRankingColor
import com.example.studentprofile.utils.toast
import com.example.studentprofile.utils.trimmedText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentStudent: Student = Student.DEFAULT

    companion object {
        private const val KEY_STUDENT_DATA = "EXTRA_KEY_STUDENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khôi phục dữ liệu nếu Activity vừa bị tái tạo (xoay màn hình)
        savedInstanceState?.getSerializable(KEY_STUDENT_DATA)?.let { restored ->
            (restored as? Student)?.let { currentStudent = it }
        }

        bindStudentData(currentStudent)
        setupRealtimePreview()
        setupUpdateButton()
        setupResetButton()
        setupSendReportButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_STUDENT_DATA, currentStudent)
    }

    private fun bindStudentData(student: Student) {
        with(binding) {
            tvStudentName.text = student.name
            tvStudentDetails.text = "MSSV: ${student.id} • Lớp: ${student.className}"
            tvStudentEmail.text = "Email: ${student.email}"
            tvGpaBadge.text = "${student.gpa} GPA • ${student.gpa.toAcademicRanking()}"
            tvGpaBadge.setTextColor(student.gpa.toRankingColor())
            edtGpaInput.setText(student.gpa.toString())
        }
    }

    private fun setupRealtimePreview() {
        binding.edtGpaInput.doOnTextChanged { text, _, _, _ ->
            val input = text?.toString()?.trim() ?: ""
            binding.edtGpaInput.error = null

            val tempScore = input.toDoubleOrNull()
            if (tempScore != null && tempScore in 0.0..4.0) {
                binding.tvPreviewRanking.text = "Dự kiến: ${tempScore.toAcademicRanking()}"
                binding.tvPreviewRanking.show()
            } else {
                binding.tvPreviewRanking.gone()
            }
        }
    }

    private fun setupUpdateButton() {
        binding.btnUpdateGpa.setOnClickListener {
            val rawInput = binding.edtGpaInput.trimmedText()
            val newGpa = rawInput.toDoubleOrNull()

            if (newGpa == null || newGpa !in 0.0..4.0) {
                binding.edtGpaInput.error = "GPA phải từ 0.0 đến 4.0"
                binding.edtGpaInput.requestFocus()
                toast("Điểm số không hợp lệ, vui lòng kiểm tra lại!")
                return@setOnClickListener
            }

            binding.edtGpaInput.error = null
            currentStudent = currentStudent.copy(gpa = newGpa)
            bindStudentData(currentStudent)
            toast("Đã cập nhật GPA thành công!")
        }
    }

    private fun setupResetButton() {
        binding.btnReset.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Xác nhận khôi phục")
                setMessage("Bạn có chắc chắn muốn đặt lại điểm GPA ban đầu (${Student.DEFAULT.gpa}) không?")
                setNegativeButton("Hủy") { dialog, _ -> dialog.dismiss() }
                setPositiveButton("Đồng ý") { dialog, _ ->
                    currentStudent = Student.DEFAULT
                    bindStudentData(currentStudent)
                    toast("Đã khôi phục dữ liệu mặc định!")
                    dialog.dismiss()
                }
            }.show()
        }
    }

    private fun setupSendReportButton() {
        binding.btnSendReport.setOnClickListener {
            val subject = "[Báo cáo học tập] Sinh viên ${currentStudent.name} - MSSV ${currentStudent.id}"
            val body = buildString {
                append("Họ và tên: ${currentStudent.name}\n")
                append("Lớp: ${currentStudent.className}\n")
                append("Điểm GPA: ${currentStudent.gpa}\n")
                append("Xếp loại: ${currentStudent.gpa.toAcademicRanking()}")
            }

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${currentStudent.email}")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }

            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
                toast("Không tìm thấy ứng dụng Email trên thiết bị!")
            }
        }
    }
}