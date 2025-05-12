package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.LessonDTO;
import com.example.internshipbitlab.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Lesson Management", description = "API endpoints for managing lessons")
public class LessonController {

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonService lessonService;

    @Operation(summary = "Get lesson by ID", description = "Returns a single lesson by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson found",
                    content = @Content(schema = @Schema(implementation = LessonDTO.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(
            @Parameter(description = "ID of lesson to be retrieved", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("Received request to get lesson by ID: {}", id);
        LessonDTO lesson = lessonService.findById(id);
        logger.debug("Returning lesson: {}", lesson);
        return ResponseEntity.ok(lesson);
    }

    @Operation(summary = "Get lessons by chapter ID", description = "Returns all lessons for a specific chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LessonDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content)
    })
    @GetMapping("/by-chapter/{chapterId}")
    public ResponseEntity<List<LessonDTO>> getLessonsByChapter(
            @Parameter(description = "ID of the chapter", required = true, example = "1")
            @PathVariable Long chapterId) {
        logger.info("Received request to get lessons for chapter ID: {}", chapterId);
        List<LessonDTO> lessons = lessonService.findAllByChapterId(chapterId);
        logger.debug("Returning {} lessons", lessons.size());
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Create a new lesson", description = "Creates a new lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully",
                    content = @Content(schema = @Schema(implementation = LessonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(
            @Parameter(description = "Lesson data to create", required = true)
            @Valid @RequestBody LessonDTO lessonDTO) {
        logger.info("Received request to create new lesson");
        logger.debug("Lesson data: {}", lessonDTO);
        LessonDTO createdLesson = lessonService.create(lessonDTO);
        logger.info("Lesson created successfully with ID: {}", createdLesson.getId());
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }

    @Operation(summary = "Update lesson", description = "Updates an existing lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
                    content = @Content(schema = @Schema(implementation = LessonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(
            @Parameter(description = "ID of lesson to be updated", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated lesson data", required = true)
            @Valid @RequestBody LessonDTO lessonDTO) {
        logger.info("Received request to update lesson with ID: {}", id);
        logger.debug("Update data: {}", lessonDTO);
        LessonDTO updatedLesson = lessonService.update(id, lessonDTO);
        logger.info("Lesson with ID {} updated successfully", id);
        return ResponseEntity.ok(updatedLesson);
    }

    @Operation(summary = "Delete lesson", description = "Deletes a lesson by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lesson deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID of lesson to be deleted", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("Received request to delete lesson with ID: {}", id);
        lessonService.delete(id);
        logger.info("Lesson with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}