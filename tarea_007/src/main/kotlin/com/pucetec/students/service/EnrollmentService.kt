package com.pucetec.students.service

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entity.Enrollment
import com.pucetec.students.Mappers.EnrollmentMapper
import com.pucetec.students.repository.*
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val repo: EnrollmentRepository,
    private val sRepo: StudentRepository,
    private val subRepo: SubjectRepository,
    private val mapper: EnrollmentMapper
) {
    fun enroll(req: EnrollmentRequest): EnrollmentResponse {
        val s = sRepo.findById(req.studentId).orElseThrow { com.pucetec.students.exceptions.StudentNotFoundException("Estudiante no encontrado") }
        val sub = subRepo.findById(req.subjectId).orElseThrow { com.pucetec.students.exceptions.SubjectNotFoundException("Materia no encontrada") }
        return mapper.toResponse(repo.save(Enrollment(student = s, subject = sub)))
    }

    fun getAll() = repo.findAll().map { mapper.toResponse(it) }
    fun getById(id: Long) = mapper.toResponse(repo.findById(id).orElseThrow { com.pucetec.students.exceptions.EnrollmentNotFoundException("InscripciÃ³n no encontrada") })

    fun updateStatus(id: Long, newStatus: String): EnrollmentResponse {
        if (newStatus.isBlank()) throw com.pucetec.students.exceptions.BlankMesaggeException("El estado no puede estar vacÃ­o")
        val e = repo.findById(id).orElseThrow { com.pucetec.students.exceptions.EnrollmentNotFoundException("InscripciÃ³n no encontrada") }
        e.status = newStatus
        return mapper.toResponse(repo.save(e))
    }

    fun delete(id: Long) {
        if (!repo.existsById(id)) throw com.pucetec.students.exceptions.EnrollmentNotFoundException("InscripciÃ³n no encontrada")
        repo.deleteById(id)
    }
}
