package com.example.internshipbitlab.mapper;


import com.example.internshipbitlab.dto.ChapterDTO;
import com.example.internshipbitlab.model.Chapter;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ChapterMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "lessons", source = "lessons")
    ChapterDTO toDto(Chapter chapter);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Chapter toEntity(ChapterDTO chapterDTO);

    @AfterMapping
    default void afterChapterMapping(@MappingTarget ChapterDTO.ChapterDTOBuilder chapterDTO, Chapter chapter) {
        chapterDTO.createdAt(chapter.getCreatedAt());
        chapterDTO.updatedAt(chapter.getUpdatedAt());
    }
}