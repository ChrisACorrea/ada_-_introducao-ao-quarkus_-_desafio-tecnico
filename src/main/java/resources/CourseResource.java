package resources;

import java.net.URI;
import java.util.List;

import dtos.CourseCreateDTO;
import dtos.CourseReadDTO;
import dtos.CourseUpdateDTO;
import dtos.LessonCreateDTO;
import dtos.LessonReadDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import results.Result;
import services.CourseService;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class CourseResource {

    private final CourseService courseService;

    @Inject
    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }

    @GET
    public Response getAllCourses() {
        var result = courseService.getAllCourses();
        var courseDtos = CourseReadDTO.fromEntities(result.data());
        var response = Result.success(courseDtos);

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getCourseById(Long id) {
        var course = courseService.getCourseById(id);
        var courseDto = CourseReadDTO.fromEntity(course.data());
        var response = Result.success(courseDto);

        return Response.ok(response).build();
    }

    @POST
    public Response createCourse(CourseCreateDTO courseCreateDTO) {
        var course = courseCreateDTO.toEntity();
        var result = courseService.createCourse(course);

        if (result.failure()) {
            var response = Result.failure(result.errors());
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }

        var response = Result.success(null);

        return Response.created(URI.create("/courses/" + course.getId())).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCourse(Long id, CourseUpdateDTO courseUpdateDTO) {
        if (!id.equals(courseUpdateDTO.id())) {
            var response = Result.failure(List.of("ID do caminho e do corpo devem ser iguais."));
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        var course = courseUpdateDTO.toEntity();
        var result = courseService.updateCourse(course);
        if (result.failure()) {
            var response = Result.failure(result.errors());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        var courseDto = CourseReadDTO.fromEntity(result.data());
        var response = Result.success(courseDto);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(Long id) {
        var result = courseService.deleteCourseById(id);

        if (result.failure()) {
            var response = Result.failure(result.errors());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/{courseId}/lessons")
    public Response getLessonsByCourseId(Long courseId) {
        var lessons = courseService.getLessonsByCourseId(courseId);
        var lessonDtos = LessonReadDTO.fromEntities(lessons.data());
        var response = Result.success(lessonDtos);

        return Response.ok(response).build();
    }

    @POST
    @Path("/{courseId}/lessons")
    public Response addLessonToCourse(Long courseId, LessonCreateDTO lessonCreateDTO) {
        var lesson = lessonCreateDTO.toEntity();
        var result = courseService.addLessonToCourse(courseId, lesson);

        if (result.failure()) {
            var response = Result.failure(result.errors());
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }

        var response = Result.success(null);

        return Response.status(Status.CREATED).entity(response).build();
    }
}
