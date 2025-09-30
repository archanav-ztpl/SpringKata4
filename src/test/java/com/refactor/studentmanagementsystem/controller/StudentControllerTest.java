package com.refactor.studentmanagementsystem.controller;

import com.refactor.studentmanagementsystem.dto.StudentCreateDTO;
import com.refactor.studentmanagementsystem.dto.StudentDTO;
import com.refactor.studentmanagementsystem.dto.StudentUpdateDTO;
import com.refactor.studentmanagementsystem.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStudents_returnsPage() throws Exception {
        StudentDTO student = new StudentDTO();
        student.setId(1);
        student.setName("Test");
        student.setAge(20);
        student.setCourse("Math");
        Page<StudentDTO> page = new PageImpl<>(Collections.singletonList(student), PageRequest.of(0, 10), 1);
        Mockito.when(studentService.getAllStudents(any())).thenReturn(page);
        mockMvc.perform(get("/student/all").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void addStudent_valid_returnsCreatedStudent() throws Exception {
        StudentCreateDTO createDTO = new StudentCreateDTO();
        createDTO.setName("Test");
        createDTO.setAge(20);
        createDTO.setCourse("Math");
        StudentDTO student = new StudentDTO();
        student.setId(1);
        student.setName("Test");
        student.setAge(20);
        student.setCourse("Math");
        Mockito.when(studentService.addStudent(any())).thenReturn(student);
        mockMvc.perform(post("/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateStudent_valid_returnsUpdatedStudent() throws Exception {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setId(1);
        updateDTO.setName("Test");
        updateDTO.setAge(21);
        updateDTO.setCourse("Math");
        StudentDTO student = new StudentDTO();
        student.setId(1);
        student.setName("Test");
        student.setAge(21);
        student.setCourse("Math");
        Mockito.when(studentService.updateStudent(any())).thenReturn(student);
        mockMvc.perform(put("/student/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(21));
    }

    @Test
    void deleteStudent_valid_returnsOk() throws Exception {
        Mockito.doNothing().when(studentService).deleteStudent(1);
        mockMvc.perform(delete("/student/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted!"));
    }
}

