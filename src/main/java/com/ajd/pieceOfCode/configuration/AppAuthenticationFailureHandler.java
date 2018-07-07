package com.ajd.pieceOfCode.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sun.xml.internal.ws.commons.xmlutil.Converter.UTF_8;
import static org.springframework.http.HttpStatus.*;

public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    AppAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);
        response.setStatus(getResponseStatus(exception).value());
        response.getWriter().write(objectMapper.writeValueAsString(exception));
    }

    private HttpStatus getResponseStatus(AuthenticationException exception) {
        if (DisabledException.class.isAssignableFrom(exception.getClass())) {
            return FORBIDDEN;
        } else if (BadCredentialsException.class.isAssignableFrom(exception.getClass())) {
            return UNAUTHORIZED;
        } else if (InsufficientAuthenticationException.class.isAssignableFrom(exception.getClass())) {
            return PRECONDITION_FAILED;
        } else if (LockedException.class.isAssignableFrom(exception.getClass())) {
            return LOCKED;
        } else {
            return INTERNAL_SERVER_ERROR;
        }
    }
}