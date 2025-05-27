package com.sh32bit.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        log.warn("403 Forbidden - Path: {}, Message: {}", request.getRequestURI(), accessDeniedException.getMessage());

        response.getWriter().write("""
                    {
                      "status": 403,
                      "error": "Forbidden",
                      "message": "You do not have permission to access this resource",
                      "timestamp": "%s"
                    }
                """.formatted(LocalDateTime.now()));
    }
}
