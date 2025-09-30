package com.refactor.studentmanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping("/add")
    public String addStudent(@RequestBody Student student) {
        studentRepository.save(student);
        return "Student added!";
    }

    @PutMapping("/update")
    public String updateStudent(@RequestBody Student student) {
        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isPresent()) {
            studentRepository.save(student);
            return "Student updated!";
        } else {
            return "Student not found!";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
            return "Student deleted!";
        } else {
            return "Student not found!";
        }
    }
}
