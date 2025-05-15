package aitu.network.aitunetwork.common.exception;

import org.springframework.http.HttpStatus;

public class GoneException extends SecureTalkException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.GONE;

    public GoneException(String message) {
        super(message, HTTP_STATUS);
    }
}
