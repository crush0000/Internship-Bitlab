package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.LessonDTO;
import com.example.internshipbitlab.model.Chapter;
import com.example.internshipbitlab.model.Lesson;
import com.example.internshipbitlab.exception.ResourceNotFoundException;
import com.example.internshipbitlab.mapper.LessonMapper;
import com.example.internshipbitlab.repository.ChapterRepository;
import com.example.internshipbitlab.repository.LessonRepository;
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
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void findAllByChapterId_ShouldReturnLessons() {
        // Arrange
        Long chapterId = 1L;
        Lesson lesson = new Lesson();
        LessonDTO lessonDTO = new LessonDTO();
        when(chapterRepository.existsById(chapterId)).thenReturn(true);
        when(lessonRepository.findAllByChapterId(chapterId)).thenReturn(Collections.singletonList(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDTO);

        // Act
        List<LessonDTO> result = lessonService.findAllByChapterId(chapterId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(lessonDTO, result.get(0));
        verify(lessonRepository).findAllByChapterId(chapterId);
    }

    @Test
    void findAllByChapterId_ShouldThrowException_WhenChapterNotExists() {
        // Arrange
        Long chapterId = 1L;
        when(chapterRepository.existsById(chapterId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> lessonService.findAllByChapterId(chapterId));
        verify(lessonRepository, never()).findAllByChapterId(any());
    }

    @Test
    void findById_ShouldReturnLesson_WhenLessonExists() {
        // Arrange
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        LessonDTO lessonDTO = new LessonDTO();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDTO);

        // Act
        LessonDTO result = lessonService.findById(lessonId);

        // Assert
        assertNotNull(result);
        assertEquals(lessonDTO, result);
        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void findById_ShouldThrowException_WhenLessonNotExists() {
        // Arrange
        Long lessonId = 1L;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> lessonService.findById(lessonId));
        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void create_ShouldSaveNewLesson() {
        // Arrange
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setChapterId(1L);
        lessonDTO.setTitle("New Lesson");

        Chapter chapter = new Chapter();
        Lesson lesson = new Lesson();
        Lesson savedLesson = new Lesson();
        savedLesson.setId(1L);

        when(chapterRepository.findById(lessonDTO.getChapterId())).thenReturn(Optional.of(chapter));
        when(lessonMapper.toEntity(lessonDTO)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(savedLesson);
        when(lessonMapper.toDto(savedLesson)).thenReturn(lessonDTO);

        // Act
        LessonDTO result = lessonService.create(lessonDTO);

        // Assert
        assertNotNull(result);
        verify(lessonRepository).save(lesson);
    }

    @Test
    void create_ShouldThrowException_WhenChapterNotExists() {
        // Arrange
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setChapterId(1L);
        when(chapterRepository.findById(lessonDTO.getChapterId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> lessonService.create(lessonDTO));
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateExistingLesson() {
        // Arrange
        Long lessonId = 1L;
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setTitle("Updated Lesson");
        lessonDTO.setChapterId(1L);

        Lesson existingLesson = new Lesson();
        existingLesson.setId(lessonId);
        Chapter chapter = new Chapter();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existingLesson));
        when(chapterRepository.findById(lessonDTO.getChapterId())).thenReturn(Optional.of(chapter));
        when(lessonRepository.save(existingLesson)).thenReturn(existingLesson);
        when(lessonMapper.toDto(existingLesson)).thenReturn(lessonDTO);

        // Act
        LessonDTO result = lessonService.update(lessonId, lessonDTO);

        // Assert
        assertNotNull(result);
        assertEquals(lessonDTO.getTitle(), result.getTitle());
        verify(lessonRepository).findById(lessonId);
        verify(lessonRepository).save(existingLesson);
    }

    @Test
    void delete_ShouldDeleteLesson_WhenLessonExists() {
        // Arrange
        Long lessonId = 1L;
        when(lessonRepository.existsById(lessonId)).thenReturn(true);

        // Act
        lessonService.delete(lessonId);

        // Assert
        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void delete_ShouldThrowException_WhenLessonNotExists() {
        // Arrange
        Long lessonId = 1L;
        when(lessonRepository.existsById(lessonId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> lessonService.delete(lessonId));
        verify(lessonRepository, never()).deleteById(lessonId);
    }
}