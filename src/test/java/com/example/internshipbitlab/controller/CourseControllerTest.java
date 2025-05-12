package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.CourseDTO;
import com.example.internshipbitlab.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        when(courseService.findAll()).thenReturn(Collections.singletonList(courseDTO));

        // Act
        ResponseEntity<List<CourseDTO>> response = courseController.getAllCourses();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(courseService).findAll();
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenCourseExists() {
        // Arrange
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        when(courseService.findById(courseId)).thenReturn(courseDTO);

        // Act
        ResponseEntity<CourseDTO> response = courseController.getCourseById(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseDTO, response.getBody());
        verify(courseService).findById(courseId);
    }

    @Test
    void createCourse_ShouldReturnCreatedCourse() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        CourseDTO createdCourse = new CourseDTO();
        createdCourse.setId(1L);
        when(courseService.create(courseDTO)).thenReturn(createdCourse);

        // Act
        ResponseEntity<CourseDTO> response = courseController.createCourse(courseDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCourse, response.getBody());
        verify(courseService).create(courseDTO);
    }

    @Test
    void updateCourse_ShouldReturnUpdatedCourse() {
        // Arrange
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        CourseDTO updatedCourse = new CourseDTO();
        when(courseService.update(courseId, courseDTO)).thenReturn(updatedCourse);

        // Act
        ResponseEntity<CourseDTO> response = courseController.updateCourse(courseId, courseDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());
        verify(courseService).update(courseId, courseDTO);
    }

    @Test
    void deleteCourse_ShouldReturnNoContent() {
        // Arrange
        Long courseId = 1L;
        doNothing().when(courseService).delete(courseId);

        // Act
        ResponseEntity<Void> response = courseController.deleteCourse(courseId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(courseService).delete(courseId);
    }
}