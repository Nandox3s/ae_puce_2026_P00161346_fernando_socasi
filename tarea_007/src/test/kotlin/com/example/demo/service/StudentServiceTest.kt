package com.example.demo.service

import com.example.demo.Mappers.StudentMapper
import com.example.demo.dto.StudentRequest
import com.example.demo.dto.StudentResponse
import com.example.demo.entity.Student
import com.example.demo.exceptions.BlankMesaggeException
import com.example.demo.exceptions.StudentNotFoundException
import com.example.demo.repository.StudentRepository
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
class StudentServiceTest {

    @Mock
    private lateinit var repository: StudentRepository

    @Mock
    private lateinit var mapper: StudentMapper

    @InjectMocks
    private lateinit var service: StudentService

    @Test
    fun `create devuelve respuesta cuando el nombre es válido`() {
        val req = StudentRequest(name = "Ana", email = "ana@test.com")
        val entity = Student(id = 1L, name = "Ana", email = "ana@test.com")
        val response = StudentResponse(id = 1L, name = "Ana", email = "ana@test.com")

        Mockito.`when`(mapper.toEntity(req)).thenReturn(entity)
        Mockito.`when`(repository.save(entity)).thenReturn(entity)
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.create(req)

        assertEquals(response, result)
    }

    @Test
    fun `create lanza excepción cuando el nombre está vacío`() {
        val req = StudentRequest(name = "", email = "empty@test.com")

        assertFailsWith<BlankMesaggeException> {
            service.create(req)
        }
    }

    @Test
    fun `getAll devuelve todos los estudiantes mapeados`() {
        val entity = Student(id = 2L, name = "Luis", email = "luis@test.com")
        val response = StudentResponse(id = 2L, name = "Luis", email = "luis@test.com")

        Mockito.`when`(repository.findAll()).thenReturn(listOf(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getAll()

        assertEquals(listOf(response), result)
    }

    @Test
    fun `getById devuelve estudiante cuando existe`() {
        val entity = Student(id = 3L, name = "Marta", email = "marta@test.com")
        val response = StudentResponse(id = 3L, name = "Marta", email = "marta@test.com")

        Mockito.`when`(repository.findById(3L)).thenReturn(Optional.of(entity))
        Mockito.`when`(mapper.toResponse(entity)).thenReturn(response)

        val result = service.getById(3L)

        assertEquals(response, result)
    }

    @Test
    fun `getById lanza excepción cuando no existe`() {
        Mockito.`when`(repository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            service.getById(99L)
        }
    }

    @Test
    fun `update devuelve respuesta cuando el nombre es válido`() {
        val req = StudentRequest(name = "Ana Updated", email = "ana.updated@test.com")
        val existing = Student(id = 4L, name = "Ana", email = "ana@test.com")
        val updated = Student(id = 4L, name = "Ana Updated", email = "ana.updated@test.com")
        val response = StudentResponse(id = 4L, name = "Ana Updated", email = "ana.updated@test.com")

        Mockito.`when`(repository.findById(4L)).thenReturn(Optional.of(existing))
        Mockito.`when`(repository.save(Mockito.any(Student::class.java))).thenReturn(updated)
        Mockito.`when`(mapper.toResponse(updated)).thenReturn(response)

        val result = service.update(4L, req)

        assertEquals(response, result)
    }

    @Test
    fun `update lanza excepción cuando el nombre está vacío`() {
        val req = StudentRequest(name = "   ", email = "empty@test.com")

        assertFailsWith<BlankMesaggeException> {
            service.update(4L, req)
        }
    }

    @Test
    fun `update lanza excepción cuando el estudiante no existe`() {
        val req = StudentRequest(name = "Ana", email = "ana@test.com")

        Mockito.`when`(repository.findById(10L)).thenReturn(Optional.empty())

        assertFailsWith<StudentNotFoundException> {
            service.update(10L, req)
        }
    }

    @Test
    fun `delete elimina cuando el estudiante existe`() {
        Mockito.`when`(repository.existsById(5L)).thenReturn(true)

        assertEquals(Unit, service.delete(5L))
    }

    @Test
    fun `delete lanza excepción cuando el estudiante no existe`() {
        Mockito.`when`(repository.existsById(6L)).thenReturn(false)

        assertFailsWith<StudentNotFoundException> {
            service.delete(6L)
        }
    }
}
