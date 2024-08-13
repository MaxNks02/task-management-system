package com.example.systemtaskmanagement.exeption;

import com.example.systemtaskmanagement.exeption.handling.HasDomainErrors;
import io.vavr.collection.Seq;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedException extends DomainExceptionWithErrors{

    UnauthorizedException(HttpStatus status, String reason) {
        super(status, reason);
    }
    public UnauthorizedException(String reason) {
        this(reason, null, null);
    }

    public UnauthorizedException(String reason, String objectName,
                                   Seq<HasDomainErrors.DomainError> errors) {
        super(UNAUTHORIZED, reason, objectName, errors);
    }
}
