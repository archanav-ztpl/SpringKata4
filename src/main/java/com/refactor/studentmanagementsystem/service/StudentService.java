package com.refactor.studentmanagementsystem.service;

import com.refactor.studentmanagementsystem.entity.Student;
import com.refactor.studentmanagementsystem.dto.StudentDTO;
import com.refactor.studentmanagementsystem.dto.StudentCreateDTO;
import com.refactor.studentmanagementsystem.dto.StudentUpdateDTO;
import com.refactor.studentmanagementsystem.repository.StudentRepository;
import com.refactor.studentmanagementsystem.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public Page<StudentDTO> getAllStudents(Pageable pageable) {
        logger.info("Fetching students with pagination: {}", pageable);
        Page<StudentDTO> page = studentRepository.findAll(pageable).map(this::toDTO);
        logger.debug("Fetched {} students", page.getNumberOfElements());
        return page;
    }

    public StudentDTO addStudent(StudentCreateDTO createDTO) {
        logger.info("Adding new student: {}", createDTO);
        Student student = new Student();
        student.setName(createDTO.getName());
        student.setAge(createDTO.getAge());
        student.setCourse(createDTO.getCourse());
        Student saved = studentRepository.save(student);
        logger.info("Student saved with ID: {}", saved.getId());
        return toDTO(saved);
    }

    public StudentDTO updateStudent(StudentUpdateDTO updateDTO) {
        logger.info("Updating student: {}", updateDTO);
        Optional<Student> existingStudent = studentRepository.findById(updateDTO.getId());
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setName(updateDTO.getName());
            student.setAge(updateDTO.getAge());
            student.setCourse(updateDTO.getCourse());
            Student saved = studentRepository.save(student);
            logger.info("Student updated with ID: {}", saved.getId());
            return toDTO(saved);
        } else {
            logger.warn("Student not found with ID: {}", updateDTO.getId());
            throw new StudentNotFoundException("Student not found with id: " + updateDTO.getId());
        }
    }

    public void deleteStudent(Integer id) {
        logger.info("Deleting student with ID: {}", id);
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
            logger.info("Student deleted with ID: {}", id);
        } else {
            logger.warn("Student not found with ID: {}", id);
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }

    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setAge(student.getAge());
        dto.setCourse(student.getCourse());
        return dto;
    }
}
