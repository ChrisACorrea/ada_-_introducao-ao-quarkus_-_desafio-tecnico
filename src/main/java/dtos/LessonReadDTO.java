package dtos;

import java.util.List;

import entities.Lesson;

public record LessonReadDTO(Long id, String name) {

    public static LessonReadDTO fromEntity(Lesson lesson) {
        return new LessonReadDTO(lesson.getId(), lesson.getName());
    }

    public static List<LessonReadDTO> fromEntities(List<Lesson> lessons) {
        return lessons.stream()
                .map(LessonReadDTO::fromEntity)
                .toList();
    }

}
