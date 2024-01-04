package codeep.jean.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;
}
