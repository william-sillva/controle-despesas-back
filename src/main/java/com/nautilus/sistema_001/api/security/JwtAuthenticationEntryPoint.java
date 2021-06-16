package com.nautilus.sistema_001.api.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ " +
                    "\"timestamp\": \"" + this.dateFormat.format(new Date()) + "\"," +
                    "\"status\": \"" + response.getStatus() + "\"," +
                    "\"error\": \"" + HttpStatus.UNAUTHORIZED.getReasonPhrase() + "\"," +
                    "\"message\": \"" + "Acesso negado. Você deve estar autenticado no sistema para acessar a URL solicitada." + "\"," +
                    "\"path\": \"" + request.getServletPath() + "\"" +
                "}");

    }
}
