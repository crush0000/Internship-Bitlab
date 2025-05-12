package com.example.internshipbitlab.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {
    private Long id;
    private String name;
    private String description;
    private String content;
    private int orderNumber;
    private Long chapterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}