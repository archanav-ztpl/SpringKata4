package com.refactor.studentmanagementsystem.service;

import com.refactor.studentmanagementsystem.dto.StudentCreateDTO;
import com.refactor.studentmanagementsystem.dto.StudentDTO;
import com.refactor.studentmanagementsystem.dto.StudentUpdateDTO;
import com.refactor.studentmanagementsystem.entity.Student;
import com.refactor.studentmanagementsystem.exception.StudentNotFoundException;
import com.refactor.studentmanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    @Test
    void getAllStudents_returnsPage() {
        Student student = new Student();
        student.setId(1);
        student.setName("Test");
        student.setAge(20);
        student.setCourse("Math");
        Page<Student> page = new PageImpl<>(Collections.singletonList(student), PageRequest.of(0, 10), 1);
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<StudentDTO> result = studentService.getAllStudents(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("Test", result.getContent().get(0).getName());
    }

    @Test
    void addStudent_savesAndReturnsDTO() {
        StudentCreateDTO createDTO = new StudentCreateDTO();
        createDTO.setName("Test");
        createDTO.setAge(20);
        createDTO.setCourse("Math");
        Student student = new Student();
        student.setId(1);
        student.setName("Test");
        student.setAge(20);
        student.setCourse("Math");
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDTO result = studentService.addStudent(createDTO);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
    }

    @Test
    void updateStudent_existing_returnsUpdatedDTO() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setId(1);
        updateDTO.setName("Test");
        updateDTO.setAge(21);
        updateDTO.setCourse("Math");
        Student student = new Student();
        student.setId(1);
        student.setName("Test");
        student.setAge(20);
        student.setCourse("Math");
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDTO result = studentService.updateStudent(updateDTO);
        assertEquals(1, result.getId());
        assertEquals(21, result.getAge());
    }

    @Test
    void updateStudent_notFound_throwsException() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setId(99);
        updateDTO.setName("Test");
        updateDTO.setAge(21);
        updateDTO.setCourse("Math");
        when(studentRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(updateDTO));
    }

    @Test
    void deleteStudent_existing_deletes() {
        Student student = new Student();
        student.setId(1);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        Mockito.doNothing().when(studentRepository).deleteById(1);
        assertDoesNotThrow(() -> studentService.deleteStudent(1));
    }

    @Test
    void deleteStudent_notFound_throwsException() {
        when(studentRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(99));
    }
}
