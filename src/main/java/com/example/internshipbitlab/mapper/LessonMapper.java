package com.example.internshipbitlab.mapper;


import com.example.internshipbitlab.dto.LessonDTO;
import com.example.internshipbitlab.model.Lesson;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "chapterId", source = "chapter.id")
    LessonDTO toDto(Lesson lesson);

    @Mapping(target = "chapter", ignore = true)
    Lesson toEntity(LessonDTO lessonDTO);

    @AfterMapping
    default void afterLessonMapping(@MappingTarget LessonDTO.LessonDTOBuilder lessonDTO, Lesson lesson) {
        lessonDTO.createdAt(lesson.getCreatedAt());
        lessonDTO.updatedAt(lesson.getUpdatedAt());
    }
}
