package services;

import java.util.List;

import entities.Course;
import entities.Course_;
import entities.Lesson;
import exceptions.ResourceNotFoundException;
import io.quarkus.panache.common.Sort;
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

    public Result<List<Course>> getAllCourses(int pageIndex, int pageSize) {
        var courses = courseRepository.findAll(
                Sort.by(Course_.NAME)
                        .and(Course_.ID))
                .page(pageIndex, pageSize)
                .list();

        return Result.success(courses);
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

    public Result<Course> updateCourse(Long id, Course course) {
        courseRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + id));

        var courseViolations = validator.validate(course);
        // var lessonViolations = course.getLessons()
        //         .stream()
        //         .flatMap(lesson -> validator.validate(lesson).stream())
        //         .toList();

        // var allViolations = Stream.concat(courseViolations.stream(), lessonViolations.stream())
        //         .toList();

        if (!courseViolations.isEmpty()) {
            var errors = courseViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        course = new Course(id, course.getName(), course.getLessons());
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

    public Result<List<Lesson>> getLessonsByCourseId(Long courseId) {
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + courseId));
        return Result.success(course.getLessons().stream().toList());
    }

    public Result<Lesson> addLessonToCourse(Long courseId, Lesson lesson) {
        var lessonViolations = validator.validate(lesson);

        if (!lessonViolations.isEmpty()) {
            var errors = lessonViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + courseId));

        Lesson lessonWithCourse = lesson.withCourse(course);
        course.addLesson(lessonWithCourse);
        courseRepository.getEntityManager().merge(course);

        return Result.success(lessonWithCourse);
    }

}
