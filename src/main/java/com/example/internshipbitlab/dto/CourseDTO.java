package com.example.internshipbitlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Course name cannot be blank")
    @Size(max = 255, message = "Course name must be less than 255 characters")
    private String name;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChapterDTO> chapters;
}