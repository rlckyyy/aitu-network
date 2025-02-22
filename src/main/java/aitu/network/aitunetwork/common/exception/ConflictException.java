package aitu.network.aitunetwork.common.exception;

import aitu.network.aitunetwork.exception.SecureTalkException;
import org.springframework.http.HttpStatus;

public class ConflictException extends SecureTalkException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public ConflictException(String msg) {
        super(msg, HTTP_STATUS);
    }
}
