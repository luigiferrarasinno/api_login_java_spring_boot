package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "esperoquefuncioneJWT123"; // segredo usado para assinar o token
    private static final long EXPIRATION_TIME = 864000000; // 10 dias em milissegundos, pq por algum motivo quem fez esse ngc decidiu que tinha que colocar o tempo em milesegundos ao inves de minutos

    public static String gerarToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }
    

    // Validar token e retornar o nome do usuário
    public static String validarToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }
}
