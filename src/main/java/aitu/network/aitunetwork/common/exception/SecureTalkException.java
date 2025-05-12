package aitu.network.aitunetwork.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SecureTalkException extends RuntimeException {

    private final HttpStatus httpStatus;

    public SecureTalkException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SecureTalkException(Class<?> aClass, Object id, HttpStatus httpStatus) {
        this(aClass.getSimpleName() + " with id: " + id + " not found", httpStatus);
    }
}
