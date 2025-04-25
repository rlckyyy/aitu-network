package aitu.network.aitunetwork.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends SecureTalkException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message, HTTP_STATUS);
    }
}
