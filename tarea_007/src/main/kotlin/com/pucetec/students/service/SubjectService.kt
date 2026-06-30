package com.pucetec.students.service

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.Mappers.SubjectMapper
import com.pucetec.students.entity.Subject
import com.pucetec.students.repository.ProfessorRepository
import com.pucetec.students.repository.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val repository: SubjectRepository,
    private val profRepository: ProfessorRepository,
    private val mapper: SubjectMapper
) {
    fun create(req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("Nombre y cÃ³digo no pueden estar vacÃ­os")
        val prof = profRepository.findById(req.professorId).orElseThrow { com.pucetec.students.exceptions.ProfessorNotFoundException("Profesor no encontrado") }
        return mapper.toResponse(repository.save(mapper.toEntity(req, prof)))
    }

    fun getAll() = repository.findAll().map { mapper.toResponse(it) }

    fun getById(id: Long) = mapper.toResponse(repository.findById(id).orElseThrow { com.pucetec.students.exceptions.SubjectNotFoundException("Materia no encontrada") })

    fun update(id: Long, req: SubjectRequest): SubjectResponse {
        if (req.name.isBlank() || req.code.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("Nombre y cÃ³digo no pueden estar vacÃ­os")
        val existing = repository.findById(id).orElseThrow { com.pucetec.students.exceptions.SubjectNotFoundException("Materia no encontrada") }
        val prof = profRepository.findById(req.professorId).orElseThrow { com.pucetec.students.exceptions.ProfessorNotFoundException("Profesor no encontrado") }

        
        val updated = Subject(id = existing.id, name = req.name, code = req.code, professor = prof)
        return mapper.toResponse(repository.save(updated))
    }

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw com.pucetec.students.exceptions.SubjectNotFoundException("Materia no encontrada")
        repository.deleteById(id)
    }
}
