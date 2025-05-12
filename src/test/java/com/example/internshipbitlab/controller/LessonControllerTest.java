package com.example.internshipbitlab.controller;

import com.example.internshipbitlab.dto.LessonDTO;
import com.example.internshipbitlab.service.LessonService;
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
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @Test
    void getLessonById_ShouldReturnLesson() {

        Long lessonId = 1L;
        LessonDTO lessonDTO = new LessonDTO();
        when(lessonService.findById(lessonId)).thenReturn(lessonDTO);


        ResponseEntity<LessonDTO> response = lessonController.getLessonById(lessonId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lessonDTO, response.getBody());
        verify(lessonService).findById(lessonId);
    }

    @Test
    void getLessonsByChapter_ShouldReturnLessons() {
        Long chapterId = 1L;
        LessonDTO lessonDTO = new LessonDTO();
        when(lessonService.findAllByChapterId(chapterId)).thenReturn(Collections.singletonList(lessonDTO));

        ResponseEntity<List<LessonDTO>> response = lessonController.getLessonsByChapter(chapterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(lessonService).findAllByChapterId(chapterId);
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson() {

        LessonDTO lessonDTO = new LessonDTO();
        LessonDTO createdLesson = new LessonDTO();
        createdLesson.setId(1L);
        when(lessonService.create(lessonDTO)).thenReturn(createdLesson);


        ResponseEntity<LessonDTO> response = lessonController.createLesson(lessonDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdLesson, response.getBody());
        verify(lessonService).create(lessonDTO);
    }

    @Test
    void updateLesson_ShouldReturnUpdatedLesson() {

        Long lessonId = 1L;
        LessonDTO lessonDTO = new LessonDTO();
        LessonDTO updatedLesson = new LessonDTO();
        when(lessonService.update(lessonId, lessonDTO)).thenReturn(updatedLesson);


        ResponseEntity<LessonDTO> response = lessonController.updateLesson(lessonId, lessonDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedLesson, response.getBody());
        verify(lessonService).update(lessonId, lessonDTO);
    }

    @Test
    void deleteLesson_ShouldReturnNoContent() {

        Long lessonId = 1L;
        doNothing().when(lessonService).delete(lessonId);

        ResponseEntity<Void> response = lessonController.deleteLesson(lessonId);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(lessonService).delete(lessonId);
    }
}