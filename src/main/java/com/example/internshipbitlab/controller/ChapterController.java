package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.ChapterDTO;
import com.example.internshipbitlab.service.ChapterService;
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
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
@Tag(name = "Chapter Management", description = "API endpoints for managing chapters")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);
    private final ChapterService chapterService;

    @Operation(summary = "Get chapter by ID", description = "Returns a single chapter by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter found",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> getChapterById(
            @Parameter(description = "ID of chapter to be retrieved", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("Received request to get chapter by ID: {}", id);
        ChapterDTO chapter = chapterService.findById(id);
        logger.debug("Returning chapter: {}", chapter);
        return ResponseEntity.ok(chapter);
    }

    @Operation(summary = "Get chapters by course ID", description = "Returns all chapters for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapters retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<ChapterDTO>> getChaptersByCourse(
            @Parameter(description = "ID of the course", required = true, example = "1")
            @PathVariable Long courseId) {
        logger.info("Received request to get chapters for course ID: {}", courseId);
        List<ChapterDTO> chapters = chapterService.findAllByCourseId(courseId);
        logger.debug("Returning {} chapters", chapters.size());
        return ResponseEntity.ok(chapters);
    }

    @Operation(summary = "Create a new chapter", description = "Creates a new chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chapter created successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ChapterDTO> createChapter(
            @Parameter(description = "Chapter data to create", required = true)
            @Valid @RequestBody ChapterDTO chapterDTO) {
        logger.info("Received request to create new chapter");
        logger.debug("Chapter data: {}", chapterDTO);
        ChapterDTO createdChapter = chapterService.create(chapterDTO);
        logger.info("Chapter created successfully with ID: {}", createdChapter.getId());
        return new ResponseEntity<>(createdChapter, HttpStatus.CREATED);
    }

    @Operation(summary = "Update chapter", description = "Updates an existing chapter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter updated successfully",
                    content = @Content(schema = @Schema(implementation = ChapterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Chapter not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTO> updateChapter(
            @Parameter(description = "ID of chapter to be updated", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated chapter data", required = true)
            @Valid @RequestBody ChapterDTO chapterDTO) {
        logger.info("Received request to update chapter with ID: {}", id);
        logger.debug("Update data: {}", chapterDTO);
        ChapterDTO updatedChapter = chapterService.update(id, chapterDTO);
        logger.info("Chapter with ID {} updated successfully", id);
        return ResponseEntity.ok(updatedChapter);
    }

    @Operation(summary = "Delete chapter", description = "Deletes a chapter by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chapter deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Chapter not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteChapter(
            @Parameter(description = "ID of chapter to be deleted", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("Received request to delete chapter with ID: {}", id);
        chapterService.delete(id);
        logger.info("Chapter with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}