package com.example.internshipbitlab.service;

import com.example.internshipbitlab.dto.ChapterDTO;
import com.example.internshipbitlab.exception.NotFoundException;
import com.example.internshipbitlab.mapper.ChapterMapper;
import com.example.internshipbitlab.model.Chapter;
import com.example.internshipbitlab.model.Course;
import com.example.internshipbitlab.repository.ChapterRepository;
import com.example.internshipbitlab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private static final Logger logger = LoggerFactory.getLogger(ChapterService.class);
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Transactional(readOnly = true)
    public List<ChapterDTO> findAllByCourseId(Long courseId) {
        logger.info("Fetching chapters for course ID: {}", courseId);
        List<ChapterDTO> chapters = chapterRepository.findByCourseId(courseId).stream()
                .map(chapterMapper::toDto)
                .toList();
        logger.debug("Found {} chapters for course ID: {}", chapters.size(), courseId);
        return chapters;
    }

    @Transactional(readOnly = true)
    public ChapterDTO findById(Long id) {
        logger.info("Fetching chapter with ID: {}", id);
        ChapterDTO chapter = chapterRepository.findById(id)
                .map(chapterMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("Chapter not found with ID: {}", id);
                    return new NotFoundException("Chapter not found");
                });
        logger.debug("Found chapter: {}", chapter);
        return chapter;
    }

    @Transactional
    public ChapterDTO create(ChapterDTO chapterDTO) {
        logger.info("Creating new chapter for course ID: {}", chapterDTO.getCourseId());
        logger.debug("Chapter data: {}", chapterDTO);

        Course course = courseRepository.findById(chapterDTO.getCourseId())
                .orElseThrow(() -> {
                    logger.error("Course not found with ID: {}", chapterDTO.getCourseId());
                    return new NotFoundException("Course not found");
                });

        Chapter chapter = chapterMapper.toEntity(chapterDTO);
        chapter.setCourse(course);

        Chapter savedChapter = chapterRepository.save(chapter);
        logger.info("Chapter created successfully with ID: {}", savedChapter.getId());

        return chapterMapper.toDto(savedChapter);
    }

    @Transactional
    public ChapterDTO update(Long id, ChapterDTO chapterDTO) {
        logger.info("Updating chapter with ID: {}", id);
        logger.debug("Update data: {}", chapterDTO);

        Chapter existingChapter = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Chapter not found with ID: {}", id);
                    return new NotFoundException("Chapter not found");
                });

        // Обновляем только изменяемые поля
        existingChapter.setName(chapterDTO.getName());
        existingChapter.setDescription(chapterDTO.getDescription());
        existingChapter.setOrderNumber(chapterDTO.getOrderNumber());

        Chapter updatedChapter = chapterRepository.save(existingChapter);
        logger.info("Chapter with ID {} updated successfully", id);

        return chapterMapper.toDto(updatedChapter);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting chapter with ID: {}", id);
        if (!chapterRepository.existsById(id)) {
            logger.error("Chapter not found for deletion with ID: {}", id);
            throw new NotFoundException("Chapter not found");
        }
        chapterRepository.deleteById(id);
        logger.info("Chapter with ID {} deleted successfully", id);
    }
}