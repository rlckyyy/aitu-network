package aitu.network.aitunetwork.controller.handler;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.common.exception.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDataIntegrityViolationException(
            DataIntegrityViolationException e,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(409), e.getLocalizedMessage());
        problemDetail.setInstance(URI.create(getPath(request)));
        return problemDetail;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e,
                                                       WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        problemDetail.setInstance(URI.create(getPath(request)));
        return problemDetail;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException e,
                                                 WebRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getLocalizedMessage());
        problemDetail.setInstance(URI.create(getPath(request)));
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleUnauthorizedException(
            UnauthorizedException e,
            WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(401), e.getLocalizedMessage());
        problemDetail.setInstance(URI.create(getPath(request)));
        return problemDetail;
    }


    private String getPath(WebRequest request) {
        return (request instanceof ServletWebRequest)
                ? ((ServletWebRequest) request).getRequest().getRequestURI()
                : request.getContextPath();
    }

}
