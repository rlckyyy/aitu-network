package aitu.network.aitunetwork.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends SecureTalkException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
