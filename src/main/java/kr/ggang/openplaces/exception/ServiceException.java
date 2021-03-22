package kr.ggang.openplaces.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ServiceException extends HttpStatusCodeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3391223639781329599L;

    public ServiceException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public ServiceException(HttpStatus statusCode) {
        super(statusCode);
    }

}
