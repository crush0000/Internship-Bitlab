package com.example.internshipbitlab.mapper;

import com.example.internshipbitlab.dto.CourseDTO;
import com.example.internshipbitlab.model.Course;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {

    @Mapping(target = "chapters", ignore = true)
    CourseDTO toDto(Course course);

    @Mapping(target = "chapters", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseFromDto(CourseDTO courseDTO, @MappingTarget Course course);
}