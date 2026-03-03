package dtos;

import entities.Course;

public record CourseCreateDTO(String name) {

    public Course toEntity() {
        return new Course(name);
    }

}
