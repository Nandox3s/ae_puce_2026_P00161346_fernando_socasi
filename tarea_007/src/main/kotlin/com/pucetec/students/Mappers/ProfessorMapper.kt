package com.pucetec.students.Mappers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.entity.Professor
import org.springframework.stereotype.Component

@Component
class ProfessorMapper {
    fun toEntity(req: ProfessorRequest) = Professor(name = req.name, email = req.email)

    fun toResponse(prof: Professor) = ProfessorResponse(
        id = prof.id,
        name = prof.name,
        email = prof.email
    )
}
