package com.pucetec.students.service

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.Mappers.ProfessorMapper
import com.pucetec.students.entity.Professor
import com.pucetec.students.repository.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val repository: ProfessorRepository,
    private val mapper: ProfessorMapper
) {
    fun create(req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("El nombre no puede estar vacÃ­o")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.pucetec.students.exceptions.ProfessorNotFoundException("Profesor no encontrado") })

    fun update(id: Long, req: ProfessorRequest): ProfessorResponse {
        if (req.name.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("El nombre no puede estar vacÃ­o")
        val existing = repository.findById(id).orElseThrow { com.pucetec.students.exceptions.ProfessorNotFoundException("Profesor no encontrado") }
        val updated = Professor(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.pucetec.students.exceptions.ProfessorNotFoundException("Profesor no encontrado")
        repository.deleteById(id)
    }
}
