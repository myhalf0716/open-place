package kr.ggang.openplaces.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.ggang.openplaces.exception.AccountException;
import kr.ggang.openplaces.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<?> accountException(HttpServletRequest request, AccountException e) {
        log.error("AccountException [{}|{}]", e.getStatusCode(), e.getStatusText());
        return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<?> serviceException(HttpServletRequest request, ServiceException e) {
        log.error("ServiceException [{}|{}|{}]", e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());
        return ResponseEntity.status(e.getStatusCode()).body(
                StringUtils.hasText(e.getResponseBodyAsString()) ?
                        e.getStatusText().concat(":").concat(e.getResponseBodyAsString())
                        : e.getStatusText() );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> defaultException(HttpServletRequest request, Exception e) {
        log.error("Exception [{}]", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
