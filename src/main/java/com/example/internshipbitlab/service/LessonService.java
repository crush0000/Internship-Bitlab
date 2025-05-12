package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.LessonDTO;
import com.example.internshipbitlab.exception.NotFoundException;
import com.example.internshipbitlab.mapper.LessonMapper;
import com.example.internshipbitlab.model.Chapter;
import com.example.internshipbitlab.model.Lesson;
import com.example.internshipbitlab.repository.ChapterRepository;
import com.example.internshipbitlab.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonMapper lessonMapper;

    @Transactional(readOnly = true)
    public List<LessonDTO> findAllByChapterId(Long chapterId) {
        logger.info("Fetching lessons for chapter ID: {}", chapterId);
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId);
        List<LessonDTO> lessonDTOs = lessons.stream()
                .map(lessonMapper::toDto)
                .toList();
        logger.debug("Found {} lessons for chapter ID: {}", lessonDTOs.size(), chapterId);
        return lessonDTOs;
    }

    @Transactional(readOnly = true)
    public LessonDTO findById(Long id) {
        logger.info("Fetching lesson with ID: {}", id);
        LessonDTO lesson = lessonRepository.findById(id)
                .map(lessonMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("Lesson not found with ID: {}", id);
                    return new NotFoundException("Lesson not found");
                });
        logger.debug("Found lesson: {}", lesson);
        return lesson;
    }

    @Transactional
    public LessonDTO create(LessonDTO lessonDTO) {
        logger.info("Creating new lesson for chapter ID: {}", lessonDTO.getChapterId());
        logger.debug("Lesson data: {}", lessonDTO);

        Chapter chapter = chapterRepository.findById(lessonDTO.getChapterId())
                .orElseThrow(() -> {
                    logger.error("Chapter not found with ID: {}", lessonDTO.getChapterId());
                    return new NotFoundException("Chapter not found");
                });

        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setChapter(chapter);
        Lesson savedLesson = lessonRepository.save(lesson);
        logger.info("Lesson created successfully with ID: {}", savedLesson.getId());
        return lessonMapper.toDto(savedLesson);
    }

    @Transactional
    public LessonDTO update(Long id, LessonDTO lessonDTO) {
        logger.info("Updating lesson with ID: {}", id);
        logger.debug("Update data: {}", lessonDTO);

        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Lesson not found with ID: {}", id);
                    return new NotFoundException("Lesson not found");
                });

        existingLesson.setName(lessonDTO.getName());
        existingLesson.setDescription(lessonDTO.getDescription());
        existingLesson.setContent(lessonDTO.getContent());
        existingLesson.setOrderNumber(lessonDTO.getOrderNumber());

        Lesson updatedLesson = lessonRepository.save(existingLesson);
        logger.info("Lesson with ID {} updated successfully", id);
        return lessonMapper.toDto(updatedLesson);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting lesson with ID: {}", id);
        if (!lessonRepository.existsById(id)) {
            logger.error("Lesson not found for deletion with ID: {}", id);
            throw new NotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
        logger.info("Lesson with ID {} deleted successfully", id);
    }
}