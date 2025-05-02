package aitu.network.aitunetwork.controller.handler;

import aitu.network.aitunetwork.common.exception.SecureTalkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Object[] EMPTY_ARGS = new Object[0];
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(SecureTalkException.class)
    public ProblemDetail handleSecureTalkException(
            SecureTalkException e,
            WebRequest request
    ) {
        String message = getMessage(e.getMessage());
        var problemDetail = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), message);
        problemDetail.setInstance(URI.create(getPath(request)));
        return problemDetail;
    }

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

    private String getPath(WebRequest request) {
        return (request instanceof ServletWebRequest)
                ? ((ServletWebRequest) request).getRequest().getRequestURI()
                : request.getContextPath();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .map(this::getMessage)
                .collect(Collectors.joining(","));

        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setDetail(errorMessage);
        problemDetail.setInstance(URI.create(getPath(request)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    private String getMessage(String messageCode) {
        try {
            return messageSource.getMessage(messageCode, EMPTY_ARGS, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return messageCode;
        }
    }
}
