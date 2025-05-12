package com.example.internshipbitlab.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDTO {
    private Long id;
    private String name;
    private String description;
    private int orderNumber;
    private Long courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<LessonDTO> lessons;
}