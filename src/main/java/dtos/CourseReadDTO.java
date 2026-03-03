package dtos;

import java.util.Set;
import java.util.stream.Collectors;

import entities.Course;

public record CourseReadDTO(Long id, String name, Set<LessonReadDTO> lessons) {

    public static CourseReadDTO fromEntity(Course course) {
        return new CourseReadDTO(
                course.getId(),
                course.getName(),
                LessonReadDTO.fromEntities(course.getLessons()));
    }

    public Set<LessonReadDTO> getLessons() {
        return lessons.stream().collect(Collectors.toUnmodifiableSet());
    }

    public static Set<CourseReadDTO> fromEntities(Set<Course> courses) {
        return courses.stream()
                .map(CourseReadDTO::fromEntity)
                .collect(Collectors.toSet());
    }

}
