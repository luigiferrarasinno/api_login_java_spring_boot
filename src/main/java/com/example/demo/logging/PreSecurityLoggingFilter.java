package com.example.demo.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Order(-101) // Executa antes da segurança
public class PreSecurityLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        try {
            filterChain.doFilter(wrappedRequest, response);
        } catch (Exception e) {
            logRequest(wrappedRequest, response, e);
            throw e;
        }

        if (response.getStatus() == 401 || response.getStatus() == 403) {
            logRequest(wrappedRequest, response, null);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request,
                            HttpServletResponse response,
                            Exception error) throws IOException {

        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        StringBuilder log = new StringBuilder();
        log.append("""
---------- PRE-SECURITY LOG ----------
⏱️  %s
➡️  %s %s
🔐 Status: %d (%s)
🌐 IP do Cliente: %s
📋 Headers:
%s
📥 Body da Requisição:
%s
""".formatted(
            LocalDateTime.now(),
            request.getMethod(), request.getRequestURI(),
            response.getStatus(), getHttpStatusText(response.getStatus()),
            request.getRemoteAddr(),
            extractHeaders(request),
            body.isBlank() ? "(vazio)" : body
        ));

        if (error != null) {
            log.append("""
❌ EXCEÇÃO: %s
💬 Mensagem: %s
""".formatted(
                error.getClass().getSimpleName(),
                error.getMessage()
            ));
        }

        log.append("--------------------------------------\n\n");

        try (FileWriter fw = new FileWriter("log_api.txt", true)) {
            fw.write(log.toString());
        }
    }

    private String extractHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(name ->
            headers.append(" - ").append(name).append(": ").append(request.getHeader(name)).append("\n")
        );
        return headers.toString();
    }

    private String getHttpStatusText(int status) {
        try {
            return HttpStatus.valueOf(status).getReasonPhrase();
        } catch (Exception e) {
            return "Desconhecido";
        }
    }
}
