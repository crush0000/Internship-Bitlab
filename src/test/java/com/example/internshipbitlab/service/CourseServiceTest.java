package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.CourseDTO;
import com.example.internshipbitlab.model.Course;
import com.example.internshipbitlab.exception.ResourceNotFoundException;
import com.example.internshipbitlab.mapper.CourseMapper;
import com.example.internshipbitlab.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void findAll_ShouldReturnAllCourses() {
        // Arrange
        Course course = new Course();
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        List<CourseDTO> result = courseService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(courseDTO, result.get(0));
        verify(courseRepository).findAll();
    }

    @Test
    void findById_ShouldReturnCourse_WhenCourseExists() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.findById(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseDTO, result);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void findById_ShouldThrowException_WhenCourseNotExists() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> courseService.findById(courseId));
        verify(courseRepository).findById(courseId);
    }

    @Test
    void create_ShouldSaveNewCourse() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("New Course");
        Course course = new Course();
        Course savedCourse = new Course();
        savedCourse.setId(1L);

        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.create(courseDTO);

        // Assert
        assertNotNull(result);
        verify(courseRepository).save(course);
    }

    @Test
    void update_ShouldUpdateExistingCourse() {
        // Arrange
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("Updated Course");

        Course existingCourse = new Course();
        existingCourse.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);
        when(courseMapper.toDto(existingCourse)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.update(courseId, courseDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseDTO.getTitle(), result.getTitle());
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void update_ShouldThrowException_WhenCourseNotExists() {
        // Arrange
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> courseService.update(courseId, courseDTO));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteCourse_WhenCourseExists() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        // Act
        courseService.delete(courseId);

        // Assert
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void delete_ShouldThrowException_WhenCourseNotExists() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> courseService.delete(courseId));
        verify(courseRepository, never()).deleteById(courseId);
    }
}