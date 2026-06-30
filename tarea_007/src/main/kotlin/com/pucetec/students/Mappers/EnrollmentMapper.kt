package com.pucetec.students.Mappers

import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entity.Enrollment
import com.pucetec.students.entity.Student
import com.pucetec.students.entity.Subject
import org.springframework.stereotype.Component

@Component
class EnrollmentMapper(
    private val studentMapper: StudentMapper,
    private val subjectMapper: SubjectMapper
) {
    fun toResponse(e: Enrollment) = EnrollmentResponse(
        id = e.id,
        status = e.status,
        createdAt = e.createdAt,
        student = studentMapper.toResponse(e.student),
        subject = subjectMapper.toResponse(e.subject)
    )
}
