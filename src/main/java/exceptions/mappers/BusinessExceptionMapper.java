package exceptions.mappers;

import java.util.List;

import exceptions.BusinessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import results.Result;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        var response = Result.failure(List.of(exception.getMessage()));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }

}
