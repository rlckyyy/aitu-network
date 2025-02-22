package aitu.network.aitunetwork.common.exception;

import aitu.network.aitunetwork.exception.SecureTalkException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends SecureTalkException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public EntityNotFoundException(Class<?> clazz, Object id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id), HTTP_STATUS);
    }

    public EntityNotFoundException(Class<?> clazz, Object field, Object id) {
        super(String.format("Entity %s with %s %s not found", clazz.getSimpleName(), field, id), HTTP_STATUS);
    }

    public EntityNotFoundException(String desc) {
        super(desc, HTTP_STATUS);
    }

}
