package kr.ggang.openplaces.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AccountException extends HttpStatusCodeException {

    /**
     * 
     */
    private static final long serialVersionUID = -7924169497125599834L;

    public AccountException(HttpStatus statusCode) {
        super(statusCode);
    }

    public AccountException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
