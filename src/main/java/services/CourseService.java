package services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entities.Course;
import entities.Lesson;
import exceptions.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import repositories.CourseRepository;
import results.Result;

@ApplicationScoped
public class CourseService {

    private final CourseRepository courseRepository;
    private final Validator validator;

    @Inject
    public CourseService(CourseRepository courseRepository, Validator validator) {
        this.courseRepository = courseRepository;
        this.validator = validator;
    }

    public Result<Set<Course>> getAllCourses() {
        return Result.success(courseRepository.listAll()
                .stream()
                .collect(Collectors.toSet()));
    }

    public Result<Course> getCourseById(Long id) {
        return courseRepository.findByIdOptional(id)
                .map(Result::success)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + id));
    }

    public Result<Course> createCourse(Course course) {
        var violations = validator.validate(course);

        if (!violations.isEmpty()) {
            var errors = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        courseRepository.persist(course);
        return Result.success(course);
    }

    public Result<Course> updateCourse(Course course) {
        courseRepository.findByIdOptional(course.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + course.getId()));

        var courseViolations = validator.validate(course);
        var lessonViolations = course.getLessons()
                .stream()
                .flatMap(lesson -> validator.validate(lesson).stream())
                .toList();

        var allViolations = Stream.concat(courseViolations.stream(), lessonViolations.stream())
                .toList();

        if (!allViolations.isEmpty()) {
            var errors = allViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        courseRepository.getEntityManager().merge(course);
        return Result.success(course);
    }

    public Result<Boolean> deleteCourseById(Long id) {
        var course = courseRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + id));
    
        boolean deleted = courseRepository.deleteById(course.getId());

        if (deleted) {
            return Result.success(true);
        } else {
            return Result.failure(List.of("Não foi possível deletar o curso."));
        }
    }

    public Result<Set<Lesson>> getLessonsByCourseId(Long courseId) {
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow();
        return Result.success(course.getLessons());
    }

    public Result<Boolean> addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow();

        Lesson lessonWithCourse = lesson.withCourse(course);
        course.addLesson(lessonWithCourse);
        courseRepository.getEntityManager().merge(course);

        return Result.success(true);
    }

}
