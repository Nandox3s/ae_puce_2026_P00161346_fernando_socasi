package com.example.demo.service

import com.example.demo.Mappers.ProfessorMapper
import com.example.demo.dto.ProfessorRequest
import com.example.demo.dto.ProfessorResponse
import com.example.demo.entity.Professor
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.ProfessorNotFoundException
import com.example.demo.repository.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock
    private lateinit var repository: ProfessorRepository

    @Mock
    private lateinit var mapper: ProfessorMapper

    @InjectMocks
    private lateinit var service: ProfessorService

    @Test
    fun `create devuelve respuesta cuando el nombre es válido`() {
        val req = ProfessorRequest(name = "Dr. García", email = "garcia@test.com")
        val entity = Professor(id = 1L, name = "Dr. García", email = "garcia@test.com")
        val response = ProfessorResponse(id = 1L, name = "Dr. García", email = "garcia@test.com")

        Mockito.`when`(mapper.toEntity(req)).thenReturn(entity)
        Mockito.`when`(repository.save(entity)).thenReturn(entity)
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.create(req)

        assertEquals(response, result)
    }

    @Test
    fun `create lanza excepción cuando el nombre está vacío`() {
        val req = ProfessorRequest(name = "   ", email = "garcia@test.com")

        assertFailsWith<BlankMesaggeException> {
            service.create(req)
        }
    }

    @Test
    fun `getAll devuelve todos los profesores mapeados`() {
        val entity = Professor(id = 2L, name = "Dr. López", email = "lopez@test.com")
        val response = ProfessorResponse(id = 2L, name = "Dr. López", email = "lopez@test.com")

        Mockito.`when`(repository.findAll()).thenReturn(listOf(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()

        assertEquals(listOf(response), result)
    }

    @Test
    fun `getById devuelve profesor cuando existe`() {
        val entity = Professor(id = 3L, name = "Dr. Pérez", email = "perez@test.com")
        val response = ProfessorResponse(id = 3L, name = "Dr. Pérez", email = "perez@test.com")

        Mockito.`when`(repository.findById(3L)).thenReturn(Optional.of(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(3L)

        assertEquals(response, result)
    }

    @Test
    fun `getById lanza excepción cuando no existe`() {
        Mockito.`when`(repository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFoundException> {
            service.getById(99L)
        }
    }

    @Test
    fun `update devuelve respuesta cuando el nombre es válido`() {
        val req = ProfessorRequest(name = "Dr. García Updated", email = "garcia.updated@test.com")
        val existing = Professor(id = 4L, name = "Dr. García", email = "garcia@test.com")
        val updated = Professor(id = 4L, name = "Dr. García Updated", email = "garcia.updated@test.com")
        val response = ProfessorResponse(id = 4L, name = "Dr. García Updated", email = "garcia.updated@test.com")

        Mockito.`when`(repository.findById(4L)).thenReturn(Optional.of(existing))
        Mockito.`when`(repository.save(Mockito.any(Professor::class.java))).thenReturn(updated)
        Mockito.`when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(4L, req)

        assertEquals(response, result)
    }

    @Test
    fun `update lanza excepción cuando el nombre está vacío`() {
        val req = ProfessorRequest(name = "   ", email = "garcia@test.com")

        assertFailsWith<BlankMesaggeException> {
            service.update(4L, req)
        }
    }

    @Test
    fun `update lanza excepción cuando el profesor no existe`() {
        val req = ProfessorRequest(name = "Dr. García", email = "garcia@test.com")

        Mockito.`when`(repository.findById(10L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFoundException> {
            service.update(10L, req)
        }
    }

    @Test
    fun `delete elimina cuando el profesor existe`() {
        Mockito.`when`(repository.existsById(5L)).thenReturn(true)

        assertEquals(Unit, service.delete(5L))
    }

    @Test
    fun `delete lanza excepción cuando el profesor no existe`() {
        Mockito.`when`(repository.existsById(6L)).thenReturn(false)

        assertFailsWith<ProfessorNotFoundException> {
            service.delete(6L)
        }
    }
}
