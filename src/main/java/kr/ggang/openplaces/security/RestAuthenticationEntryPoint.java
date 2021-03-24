package kr.ggang.openplaces.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("BEEP Unauthorized");
    }
    
    @ExceptionHandler (value = {AccessDeniedException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        // 403
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("FORBIDDEN : " + accessDeniedException.getMessage());
    }

    @ExceptionHandler (value = {NoHandlerFoundException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response,
            NoHandlerFoundException exception) throws IOException {
        // 404
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("URL not supported");
    }
    @ExceptionHandler (value = {UnsupportedMediaTypeStatusException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response,
            UnsupportedMediaTypeStatusException exception) throws IOException {
        // 404
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        response.getWriter().write("Unsupported Media Type");
    }
}
