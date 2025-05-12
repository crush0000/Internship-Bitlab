package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.CourseDTO;
import com.example.internshipbitlab.exception.NotFoundException;
import com.example.internshipbitlab.mapper.CourseMapper;
import com.example.internshipbitlab.model.Course;
import com.example.internshipbitlab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public CourseDTO findById(Long id) {
        logger.info("Fetching course with ID: {}", id);
        try {
            CourseDTO course = courseRepository.findById(id)
                    .map(courseMapper::toDto)
                    .orElseThrow(() -> new NotFoundException("Course with id " + id + " not found"));
            logger.debug("Found course: {}", course);
            return course;
        } catch (NotFoundException e) {
            logger.error("Course not found with ID: {}", id, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> findAll() {
        logger.info("Fetching all courses");
        List<CourseDTO> courses = courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .toList();
        logger.debug("Found {} courses", courses.size());
        return courses;
    }

    @Transactional
    public CourseDTO create(CourseDTO courseDTO) {
        logger.info("Creating new course");
        logger.debug("Course data: {}", courseDTO);

        try {
            Course course = courseMapper.toEntity(courseDTO);
            Course savedCourse = courseRepository.save(course);
            logger.info("Course created successfully with ID: {}", savedCourse.getId());
            logger.debug("Created course details: {}", savedCourse);
            return courseMapper.toDto(savedCourse);
        } catch (Exception e) {
            logger.error("Failed to create course: {}", e.getMessage(), e);
            throw new RuntimeException("Course creation failed", e);
        }
    }

    @Transactional
    public CourseDTO update(Long id, CourseDTO courseDTO) {
        logger.info("Updating course with ID: {}", id);
        logger.debug("Update data: {}", courseDTO);

        try {
            Course existingCourse = courseRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Course not found"));

            courseMapper.updateCourseFromDto(courseDTO, existingCourse);
            Course updatedCourse = courseRepository.save(existingCourse);
            logger.info("Course with ID {} updated successfully", id);
            logger.debug("Updated course details: {}", updatedCourse);
            return courseMapper.toDto(updatedCourse);
        } catch (NotFoundException e) {
            logger.error("Course not found for update with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update course with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Course update failed", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting course with ID: {}", id);
        try {
            if (!courseRepository.existsById(id)) {
                throw new NotFoundException("Course not found");
            }
            courseRepository.deleteById(id);
            logger.info("Course with ID {} deleted successfully", id);
        } catch (NotFoundException e) {
            logger.error("Course not found for deletion with ID: {}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete course with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Course deletion failed", e);
        }
    }
}