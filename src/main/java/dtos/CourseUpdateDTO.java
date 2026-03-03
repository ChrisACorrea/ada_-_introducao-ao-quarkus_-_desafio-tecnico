package dtos;

import java.util.Set;
import java.util.stream.Collectors;

import entities.Course;

public record CourseUpdateDTO(Long id, String name, Set<LessonUpdateDTO> lessons) {

    public Set<LessonUpdateDTO> getLessons() {
        return lessons != null ? lessons.stream().collect(Collectors.toUnmodifiableSet()) : Set.of();
    }

    public Course toEntity() {
        Course course = new Course(id, name, null);
        if (lessons != null) {
            Set<entities.Lesson> lessonEntities = LessonUpdateDTO.toEntities(lessons, course);
            course.addLessons(lessonEntities);
        }
        return course;
    }

}
