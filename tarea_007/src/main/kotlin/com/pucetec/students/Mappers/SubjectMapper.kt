package com.pucetec.students.Mappers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entity.Professor
import com.pucetec.students.entity.Subject
import org.springframework.stereotype.Component

@Component
class SubjectMapper(private val professorMapper: ProfessorMapper) {

    fun toEntity(req: SubjectRequest, prof: Professor) = Subject(
        name = req.name,
        code = req.code,
        professor = prof
    )

    fun toResponse(sub: Subject) = SubjectResponse(
        sub.id,
        sub.name,
        sub.code,
        professorMapper.toResponse(sub.professor)
    )
}
