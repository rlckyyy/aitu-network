package aitu.network.aitunetwork.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends SecureTalkException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message, HTTP_STATUS);
    }
}
