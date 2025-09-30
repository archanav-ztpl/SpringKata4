package com.refactor.studentmanagementsystem.controller;

import com.refactor.studentmanagementsystem.service.StudentService;
import com.refactor.studentmanagementsystem.dto.StudentDTO;
import com.refactor.studentmanagementsystem.dto.StudentCreateDTO;
import com.refactor.studentmanagementsystem.dto.StudentUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching students: page={}, size={}", page, size);
        Page<StudentDTO> students = studentService.getAllStudents(PageRequest.of(page, size));
        logger.debug("Fetched {} students", students.getNumberOfElements());
        return ResponseEntity.ok(students);
    }

    @PostMapping("/add")
    public ResponseEntity<StudentDTO> addStudent(@Valid @RequestBody StudentCreateDTO studentCreateDTO) {
        logger.info("Received request to add student: {}", studentCreateDTO);
        StudentDTO createdStudent = studentService.addStudent(studentCreateDTO);
        logger.info("Student created with ID: {}", createdStudent.getId());
        return ResponseEntity.status(201).body(createdStudent);
    }

    @PutMapping("/update")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        logger.info("Received request to update student: {}", studentUpdateDTO);
        StudentDTO updatedStudent = studentService.updateStudent(studentUpdateDTO);
        logger.info("Student updated with ID: {}", updatedStudent.getId());
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        logger.info("Received request to delete student with ID: {}", id);
        studentService.deleteStudent(id);
        logger.info("Student deleted with ID: {}", id);
        return ResponseEntity.ok("Student deleted!");
    }
}
