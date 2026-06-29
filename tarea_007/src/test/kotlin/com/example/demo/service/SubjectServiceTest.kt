package com.example.demo.service

import com.example.demo.Mappers.SubjectMapper
import com.example.demo.dto.ProfessorResponse
import com.example.demo.dto.SubjectRequest
import com.example.demo.dto.SubjectResponse
import com.example.demo.entity.Professor
import com.example.demo.entity.Subject
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.exceptions.SubjectNotFoundException
import com.example.demo.repository.ProfessorRepository
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
class SubjectServiceTest {

    @Mock
    private lateinit var repository: SubjectRepository

    @Mock
    private lateinit var profRepository: ProfessorRepository

    @Mock
    private lateinit var mapper: SubjectMapper

    @InjectMocks
    private lateinit var service: SubjectService

    @Test
    fun `create devuelve respuesta cuando el nombre y código son válidos`() {
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val req = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 1L)
        val entity = Subject(id = 10L, name = "Arquitectura", code = "AE-101", professor = professor)
        val response = SubjectResponse(id = 10L, name = "Arquitectura", code = "AE-101", professor = ProfessorResponse(1L, "Dr. García", "garcia@test.com"))

        Mockito.`when`(profRepository.findById(1L)).thenReturn(Optional.of(professor))
        Mockito.`when`(mapper.toEntity(req, professor)).thenReturn(entity)
        Mockito.`when`(repository.save(entity)).thenReturn(entity)
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.create(req)

        assertEquals(response, result)
    }

    @Test
    fun `create lanza excepción cuando el nombre o código están vacíos`() {
        val req = SubjectRequest(name = "", code = "", professorId = 1L)

        assertFailsWith<BlankMesaggeException> {
            service.create(req)
        }
    }

    @Test
    fun `create lanza excepción cuando solo el código está vacío`() {
        val req = SubjectRequest(name = "Arquitectura", code = "", professorId = 1L)

        assertFailsWith<BlankMesaggeException> {
            service.create(req)
        }
    }

    @Test
    fun `create lanza excepción cuando el profesor no existe`() {
        val req = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 7L)

        Mockito.`when`(profRepository.findById(7L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFoundException> {
            service.create(req)
        }
    }

    @Test
    fun `getAll devuelve todas las materias mapeadas`() {
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val entity = Subject(id = 11L, name = "Diseño", code = "DI-01", professor = professor)
        val response = SubjectResponse(id = 11L, name = "Diseño", code = "DI-01", professor = ProfessorResponse(1L, "Dr. García", "garcia@test.com"))

        Mockito.`when`(repository.findAll()).thenReturn(listOf(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()

        assertEquals(listOf(response), result)
    }

    @Test
    fun `getById devuelve materia cuando existe`() {
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val entity = Subject(id = 12L, name = "Base de Datos", code = "BD-01", professor = professor)
        val response = SubjectResponse(id = 12L, name = "Base de Datos", code = "BD-01", professor = ProfessorResponse(1L, "Dr. García", "garcia@test.com"))

        Mockito.`when`(repository.findById(12L)).thenReturn(Optional.of(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(12L)

        assertEquals(response, result)
    }

    @Test
    fun `getById lanza excepción cuando no existe`() {
        Mockito.`when`(repository.findById(88L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            service.getById(88L)
        }
    }

    @Test
    fun `update devuelve respuesta cuando la materia es válida`() {
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val req = SubjectRequest(name = "Arquitectura", code = "AE-202", professorId = 1L)
        val existing = Subject(id = 13L, name = "Arquitectura", code = "AE-101", professor = professor)
        val updated = Subject(id = 13L, name = "Arquitectura", code = "AE-202", professor = professor)
        val response = SubjectResponse(id = 13L, name = "Arquitectura", code = "AE-202", professor = ProfessorResponse(1L, "Dr. García", "garcia@test.com"))

        Mockito.`when`(repository.findById(13L)).thenReturn(Optional.of(existing))
        Mockito.`when`(profRepository.findById(1L)).thenReturn(Optional.of(professor))
        Mockito.`when`(repository.save(Mockito.any(Subject::class.java))).thenReturn(updated)
        Mockito.`when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(13L, req)

        assertEquals(response, result)
    }

    @Test
    fun `update lanza excepción cuando el nombre o código están vacíos`() {
        val req = SubjectRequest(name = "   ", code = "", professorId = 1L)

        assertFailsWith<BlankMesaggeException> {
            service.update(13L, req)
        }
    }

    @Test
    fun `update lanza excepción cuando solo el código está vacío`() {
        val req = SubjectRequest(name = "Arquitectura", code = "", professorId = 1L)

        assertFailsWith<BlankMesaggeException> {
            service.update(13L, req)
        }
    }

    @Test
    fun `update lanza excepción cuando la materia no existe`() {
        val req = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 1L)

        Mockito.`when`(repository.findById(14L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            service.update(14L, req)
        }
    }

    @Test
    fun `update lanza excepción cuando el profesor no existe`() {
        val req = SubjectRequest(name = "Arquitectura", code = "AE-101", professorId = 7L)
        val existing = Subject(id = 15L, name = "Arquitectura", code = "AE-101", professor = Professor(1L, "Dr. García", "garcia@test.com"))

        Mockito.`when`(repository.findById(15L)).thenReturn(Optional.of(existing))
        Mockito.`when`(profRepository.findById(7L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFoundException> {
            service.update(15L, req)
        }
    }

    @Test
    fun `delete elimina cuando la materia existe`() {
        Mockito.`when`(repository.existsById(16L)).thenReturn(true)

        assertEquals(Unit, service.delete(16L))
    }

    @Test
    fun `delete lanza excepción cuando la materia no existe`() {
        Mockito.`when`(repository.existsById(17L)).thenReturn(false)

        assertFailsWith<SubjectNotFoundException> {
            service.delete(17L)
        }
    }
}
