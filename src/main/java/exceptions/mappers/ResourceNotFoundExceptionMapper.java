package exceptions.mappers;

import java.util.List;

import exceptions.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import results.Result;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        var response = Result.failure(List.of(exception.getMessage()));

        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }

}
