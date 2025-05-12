package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.ChapterDTO;
import com.example.internshipbitlab.service.ChapterService;
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
class ChapterControllerTest {

    @Mock
    private ChapterService chapterService;

    @InjectMocks
    private ChapterController chapterController;

    @Test
    void getChapterById_ShouldReturnChapter() {
        // Arrange
        Long chapterId = 1L;
        ChapterDTO chapterDTO = new ChapterDTO();
        when(chapterService.findById(chapterId)).thenReturn(chapterDTO);

        // Act
        ResponseEntity<ChapterDTO> response = chapterController.getChapterById(chapterId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chapterDTO, response.getBody());
        verify(chapterService).findById(chapterId);
    }

    @Test
    void getChaptersByCourse_ShouldReturnChapters() {
        // Arrange
        Long courseId = 1L;
        ChapterDTO chapterDTO = new ChapterDTO();
        when(chapterService.findAllByCourseId(courseId)).thenReturn(Collections.singletonList(chapterDTO));

        // Act
        ResponseEntity<List<ChapterDTO>> response = chapterController.getChaptersByCourse(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(chapterService).findAllByCourseId(courseId);
    }

    @Test
    void createChapter_ShouldReturnCreatedChapter() {
        // Arrange
        ChapterDTO chapterDTO = new ChapterDTO();
        ChapterDTO createdChapter = new ChapterDTO();
        createdChapter.setId(1L);
        when(chapterService.create(chapterDTO)).thenReturn(createdChapter);

        // Act
        ResponseEntity<ChapterDTO> response = chapterController.createChapter(chapterDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdChapter, response.getBody());
        verify(chapterService).create(chapterDTO);
    }

    @Test
    void updateChapter_ShouldReturnUpdatedChapter() {
        // Arrange
        Long chapterId = 1L;
        ChapterDTO chapterDTO = new ChapterDTO();
        ChapterDTO updatedChapter = new ChapterDTO();
        when(chapterService.update(chapterId, chapterDTO)).thenReturn(updatedChapter);

        // Act
        ResponseEntity<ChapterDTO> response = chapterController.updateChapter(chapterId, chapterDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedChapter, response.getBody());
        verify(chapterService).update(chapterId, chapterDTO);
    }

    @Test
    void deleteChapter_ShouldReturnNoContent() {
        // Arrange
        Long chapterId = 1L;
        doNothing().when(chapterService).delete(chapterId);

        // Act
        ResponseEntity<Void> response = chapterController.deleteChapter(chapterId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(chapterService).delete(chapterId);
    }
}