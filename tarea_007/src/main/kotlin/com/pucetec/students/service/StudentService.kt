package com.pucetec.students.service

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.Mappers.StudentMapper
import com.pucetec.students.entity.Student
import com.pucetec.students.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val repository: StudentRepository,
    private val mapper: StudentMapper
) {
    fun create(req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("El nombre no puede estar vacÃ­o")
        return mapper.toResponse(repository.save(mapper.toEntity(req)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.pucetec.students.exceptions.StudentNotFoundException("Estudiante no encontrado") })

    fun update(id: Long, req: StudentRequest): StudentResponse {
        if (req.name.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("El nombre no puede estar vacÃ­o")
        val existing = repository.findById(id).orElseThrow { com.pucetec.students.exceptions.StudentNotFoundException("Estudiante no encontrado") }
        val updated = Student(id = existing.id, name = req.name, email = req.email)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.pucetec.students.exceptions.StudentNotFoundException("Estudiante no encontrado")
        repository.deleteById(id)
    }
}
