package kr.ggang.openplaces.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.ggang.openplaces.exception.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<?> defaultException(HttpServletRequest request, AccountException e) {
        log.error("AccountException [{}|{}]", e.getStatusCode(), e.getStatusText());
        return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
    }

}
