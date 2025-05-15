package aitu.network.aitunetwork.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SecureTalkException {

    public static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(String message) {
        super(message, HTTP_STATUS);
    }

    public NotFoundException(Class<?> aClass, Object id) {
        super(aClass, id, HTTP_STATUS);
    }

    public NotFoundException(Class<?> aClass, String field, Object id) {
        super(aClass, field, id, HTTP_STATUS);
    }
}
