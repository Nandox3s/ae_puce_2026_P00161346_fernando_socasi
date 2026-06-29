package com.example.demo.service

import com.example.demo.Mappers.EnrollmentMapper
import com.example.demo.dto.EnrollmentRequest
import com.example.demo.dto.EnrollmentResponse
import com.example.demo.dto.ProfessorResponse
import com.example.demo.dto.StudentResponse
import com.example.demo.dto.SubjectResponse
import com.example.demo.entity.Enrollment
import com.example.demo.entity.Professor
import com.example.demo.entity.Student
import com.example.demo.entity.Subject
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.EnrollmentNotFoundException
import com.example.demo.exceptions.StudentNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.repository.EnrollmentRepository
import com.example.demo.repository.StudentRepository
import com.example.demo.repository.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock
    private lateinit var repo: EnrollmentRepository

    @Mock
    private lateinit var sRepo: StudentRepository

    @Mock
    private lateinit var subRepo: SubjectRepository

    @Mock
    private lateinit var mapper: EnrollmentMapper

    @InjectMocks
    private lateinit var service: EnrollmentService

    @Test
    fun `enroll devuelve respuesta cuando el estudiante y la materia existen`() {
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val subject = Subject(id = 10L, name = "Arquitectura", code = "AE-101", professor = professor)
        val req = EnrollmentRequest(studentId = 1L, subjectId = 10L)
        val enrollment = Enrollment(id = 100L, student = student, subject = subject, status = "INSCRITO")
        val response = EnrollmentResponse(
            id = 100L,
            status = "INSCRITO",
            createdAt = enrollment.createdAt,
            student = StudentResponse(1L, "Ana", "ana@test.com"),
            subject = SubjectResponse(10L, "Arquitectura", "AE-101", ProfessorResponse(1L, "Dr. García", "garcia@test.com"))
        )

        Mockito.`when`(sRepo.findById(1L)).thenReturn(Optional.of(student))
        Mockito.`when`(subRepo.findById(10L)).thenReturn(Optional.of(subject))
        Mockito.`when`(repo.save(Mockito.any(Enrollment::class.java))).thenReturn(enrollment)
        Mockito.`when`(mapper.toResponse(enrollment)).thenReturn(response)

        val result = service.enroll(req)

        assertEquals(response, result)
    }

    @Test
    fun `enroll lanza excepción cuando el estudiante no existe`() {
        val req = EnrollmentRequest(studentId = 1L, subjectId = 10L)

        Mockito.`when`(sRepo.findById(1L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            service.enroll(req)
        }
    }

    @Test
    fun `enroll lanza excepción cuando la materia no existe`() {
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val req = EnrollmentRequest(studentId = 1L, subjectId = 10L)

        Mockito.`when`(sRepo.findById(1L)).thenReturn(Optional.of(student))
        Mockito.`when`(subRepo.findById(10L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            service.enroll(req)
        }
    }

    @Test
    fun `getAll devuelve todas las inscripciones mapeadas`() {
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val subject = Subject(id = 10L, name = "Arquitectura", code = "AE-101", professor = professor)
        val enrollment = Enrollment(id = 101L, student = student, subject = subject, status = "INSCRITO")
        val response = EnrollmentResponse(
            id = 101L,
            status = "INSCRITO",
            createdAt = enrollment.createdAt,
            student = StudentResponse(1L, "Ana", "ana@test.com"),
            subject = SubjectResponse(10L, "Arquitectura", "AE-101", ProfessorResponse(1L, "Dr. García", "garcia@test.com"))
        )

        Mockito.`when`(repo.findAll()).thenReturn(listOf(enrollment))
        Mockito.`when`(mapper.toResponse(enrollment)).thenReturn(response)

        val result = service.getAll()

        assertEquals(listOf(response), result)
    }

    @Test
    fun `getById devuelve inscripción cuando existe`() {
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val subject = Subject(id = 10L, name = "Arquitectura", code = "AE-101", professor = professor)
        val enrollment = Enrollment(id = 102L, student = student, subject = subject, status = "INSCRITO")
        val response = EnrollmentResponse(
            id = 102L,
            status = "INSCRITO",
            createdAt = enrollment.createdAt,
            student = StudentResponse(1L, "Ana", "ana@test.com"),
            subject = SubjectResponse(10L, "Arquitectura", "AE-101", ProfessorResponse(1L, "Dr. García", "garcia@test.com"))
        )

        Mockito.`when`(repo.findById(102L)).thenReturn(Optional.of(enrollment))
        Mockito.`when`(mapper.toResponse(enrollment)).thenReturn(response)

        val result = service.getById(102L)

        assertEquals(response, result)
    }

    @Test
    fun `getById lanza excepción cuando no existe`() {
        Mockito.`when`(repo.findById(103L)).thenReturn(Optional.empty())

        assertFailsWith<EnrollmentNotFoundException> {
            service.getById(103L)
        }
    }

    @Test
    fun `updateStatus actualiza el estado cuando el valor es válido`() {
        val student = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val subject = Subject(id = 10L, name = "Arquitectura", code = "AE-101", professor = professor)
        val enrollment = Enrollment(id = 104L, student = student, subject = subject, status = "INSCRITO")
        val response = EnrollmentResponse(
            id = 104L,
            status = "APROBADO",
            createdAt = enrollment.createdAt,
            student = StudentResponse(1L, "Ana", "ana@test.com"),
            subject = SubjectResponse(10L, "Arquitectura", "AE-101", ProfessorResponse(1L, "Dr. García", "garcia@test.com"))
        )

        Mockito.`when`(repo.findById(104L)).thenReturn(Optional.of(enrollment))
        Mockito.`when`(repo.save(enrollment)).thenReturn(enrollment)
        Mockito.`when`(mapper.toResponse(enrollment)).thenReturn(response)

        val result = service.updateStatus(104L, "APROBADO")

        assertEquals(response, result)
    }

    @Test
    fun `updateStatus lanza excepción cuando el estado está vacío`() {
        assertFailsWith<BlankMesaggeException> {
            service.updateStatus(104L, "   ")
        }
    }

    @Test
    fun `updateStatus lanza excepción cuando la inscripción no existe`() {
        Mockito.`when`(repo.findById(105L)).thenReturn(Optional.empty())

        assertFailsWith<EnrollmentNotFoundException> {
            service.updateStatus(105L, "APROBADO")
        }
    }

    @Test
    fun `delete elimina cuando la inscripción existe`() {
        Mockito.`when`(repo.existsById(106L)).thenReturn(true)

        assertEquals(Unit, service.delete(106L))
    }

    @Test
    fun `delete lanza excepción cuando la inscripción no existe`() {
        Mockito.`when`(repo.existsById(107L)).thenReturn(false)

        assertFailsWith<EnrollmentNotFoundException> {
            service.delete(107L)
        }
    }
}
