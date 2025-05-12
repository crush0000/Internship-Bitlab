package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.ChapterDTO;
import com.example.internshipbitlab.model.Chapter;
import com.example.internshipbitlab.model.Course;
import com.example.internshipbitlab.exception.ResourceNotFoundException;
import com.example.internshipbitlab.mapper.ChapterMapper;
import com.example.internshipbitlab.repository.ChapterRepository;
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
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterService chapterService;

    @Test
    void findAllByCourseId_ShouldReturnChapters() {
        // Arrange
        Long courseId = 1L;
        Chapter chapter = new Chapter();
        ChapterDTO chapterDTO = new ChapterDTO();
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(chapterRepository.findAllByCourseId(courseId)).thenReturn(Collections.singletonList(chapter));
        when(chapterMapper.toDto(chapter)).thenReturn(chapterDTO);

        // Act
        List<ChapterDTO> result = chapterService.findAllByCourseId(courseId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(chapterDTO, result.get(0));
        verify(chapterRepository).findAllByCourseId(courseId);
    }

    @Test
    void findAllByCourseId_ShouldThrowException_WhenCourseNotExists() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> chapterService.findAllByCourseId(courseId));
        verify(chapterRepository, never()).findAllByCourseId(any());
    }

    @Test
    void findById_ShouldReturnChapter_WhenChapterExists() {
        // Arrange
        Long chapterId = 1L;
        Chapter chapter = new Chapter();
        ChapterDTO chapterDTO = new ChapterDTO();
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterMapper.toDto(chapter)).thenReturn(chapterDTO);

        // Act
        ChapterDTO result = chapterService.findById(chapterId);

        // Assert
        assertNotNull(result);
        assertEquals(chapterDTO, result);
        verify(chapterRepository).findById(chapterId);
    }

    @Test
    void findById_ShouldThrowException_WhenChapterNotExists() {
        // Arrange
        Long chapterId = 1L;
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> chapterService.findById(chapterId));
        verify(chapterRepository).findById(chapterId);
    }

    @Test
    void create_ShouldSaveNewChapter() {
        // Arrange
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setCourseId(1L);
        chapterDTO.setTitle("New Chapter");

        Course course = new Course();
        Chapter chapter = new Chapter();
        Chapter savedChapter = new Chapter();
        savedChapter.setId(1L);

        when(courseRepository.findById(chapterDTO.getCourseId())).thenReturn(Optional.of(course));
        when(chapterMapper.toEntity(chapterDTO)).thenReturn(chapter);
        when(chapterRepository.save(chapter)).thenReturn(savedChapter);
        when(chapterMapper.toDto(savedChapter)).thenReturn(chapterDTO);

        // Act
        ChapterDTO result = chapterService.create(chapterDTO);

        // Assert
        assertNotNull(result);
        verify(chapterRepository).save(chapter);
    }

    @Test
    void create_ShouldThrowException_WhenCourseNotExists() {
        // Arrange
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setCourseId(1L);
        when(courseRepository.findById(chapterDTO.getCourseId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> chapterService.create(chapterDTO));
        verify(chapterRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateExistingChapter() {
        // Arrange
        Long chapterId = 1L;
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setTitle("Updated Chapter");
        chapterDTO.setCourseId(1L);

        Chapter existingChapter = new Chapter();
        existingChapter.setId(chapterId);
        Course course = new Course();

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(existingChapter));
        when(courseRepository.findById(chapterDTO.getCourseId())).thenReturn(Optional.of(course));
        when(chapterRepository.save(existingChapter)).thenReturn(existingChapter);
        when(chapterMapper.toDto(existingChapter)).thenReturn(chapterDTO);

        // Act
        ChapterDTO result = chapterService.update(chapterId, chapterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(chapterDTO.getTitle(), result.getTitle());
        verify(chapterRepository).findById(chapterId);
        verify(chapterRepository).save(existingChapter);
    }

    @Test
    void delete_ShouldDeleteChapter_WhenChapterExists() {
        // Arrange
        Long chapterId = 1L;
        when(chapterRepository.existsById(chapterId)).thenReturn(true);

        // Act
        chapterService.delete(chapterId);

        // Assert
        verify(chapterRepository).deleteById(chapterId);
    }

    @Test
    void delete_ShouldThrowException_WhenChapterNotExists() {
        // Arrange
        Long chapterId = 1L;
        when(chapterRepository.existsById(chapterId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> chapterService.delete(chapterId));
        verify(chapterRepository, never()).deleteById(chapterId);
    }
}