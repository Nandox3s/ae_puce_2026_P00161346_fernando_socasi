package com.pucetec.students.Mappers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entity.Student
import org.springframework.stereotype.Component

@Component
class StudentMapper {
    fun toEntity(req: StudentRequest) = Student(name = req.name, email = req.email)

    fun toResponse(s: Student) = StudentResponse(
        id = s.id,
        name = s.name,
        email = s.email
    )
}
