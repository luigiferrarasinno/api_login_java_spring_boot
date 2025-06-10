package com.example.demo.security;

import com.example.demo.user.dao.UsuarioDAO;
import com.example.demo.user.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UsuarioDAO usuarioDAO;

    public JwtAuthenticationFilter(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");

            try {
                String username = JwtUtil.validarToken(token);
                Usuario usuario = usuarioDAO.findByEmail(username).orElse(null);


                if (usuario != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    usuario.getEmail(),
                    null,
                    Collections.singleton(() -> usuario.getRole())
                );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.UNAUTHORIZED.value());
                body.put("erro", "Acesso não autorizado");

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), body);

                return;
            }
            
        }

        filterChain.doFilter(request, response);
    }
}
