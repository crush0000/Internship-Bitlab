package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.CourseDTO;
import com.example.internshipbitlab.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Course Management", description = "API endpoints for managing courses")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Get all courses", description = "Returns list of all available courses")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved courses",
            content = @Content(schema = @Schema(implementation = CourseDTO.class)))
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        log.info("Received request to get all courses");
        List<CourseDTO> courses = courseService.findAll();
        log.debug("Returning {} courses", courses.size());
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Get course by ID", description = "Returns a single course by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found",
                    content = @Content(schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(
            @Parameter(description = "ID of course to be retrieved", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Received request to get course by ID: {}", id);
        CourseDTO course = courseService.findById(id);
        log.debug("Returning course details for ID {}: {}", id, course);
        return ResponseEntity.ok(course);
    }

    @Operation(summary = "Create new course", description = "Creates a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully",
                    content = @Content(schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(
            @Parameter(description = "Course data to create", required = true)
            @Valid @RequestBody CourseDTO courseDTO) {
        log.info("Received request to create new course");
        log.debug("Course creation data: {}", courseDTO);
        CourseDTO createdCourse = courseService.create(courseDTO);
        log.info("Course created successfully with ID: {}", createdCourse.getId());
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @Operation(summary = "Update course", description = "Updates an existing course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                    content = @Content(schema = @Schema(implementation = CourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @Parameter(description = "ID of course to be updated", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated course data", required = true)
            @Valid @RequestBody CourseDTO courseDTO) {
        log.info("Received request to update course with ID: {}", id);
        log.debug("Course update data: {}", courseDTO);
        CourseDTO updatedCourse = courseService.update(id, courseDTO);
        log.info("Course with ID {} updated successfully", id);
        return ResponseEntity.ok(updatedCourse);
    }

    @Operation(summary = "Delete course", description = "Deletes a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID of course to be deleted", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Received request to delete course with ID: {}", id);
        courseService.delete(id);
        log.info("Course with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}