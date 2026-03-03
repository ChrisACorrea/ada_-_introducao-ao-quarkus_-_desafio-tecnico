package dtos;

import java.util.List;

import entities.Course;

public record CourseReadDTO(Long id, String name, List<LessonReadDTO> lessons) {

    public static CourseReadDTO fromEntity(Course course) {
        return new CourseReadDTO(
                course.getId(),
                course.getName(),
                LessonReadDTO.fromEntities(course.getLessons().stream().toList()));
    }

    public List<LessonReadDTO> getLessons() {
        return lessons.stream().toList();
    }

    public static List<CourseReadDTO> fromEntities(List<Course> courses) {
        return courses.stream()
                .map(CourseReadDTO::fromEntity)
                .toList();
    }

}
