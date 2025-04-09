package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtil {

    //private static final String SECRET = "meuSegredoJWT123";
    private static final String SECRET = "esperoquefuncioneJWT123"; // segredo usado para assinar o token
    private static final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos, pq por algum motivo quem fez esse ngc decidiu que tinha que colocar o tempo em milesegundos ao inves de minutos

    // Gerar token com nome do usuário
    public static String gerarToken(String nomeUsuario) {
        return JWT.create()
                .withSubject(nomeUsuario) // quem é o dono do token
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // validade
                .sign(Algorithm.HMAC256(SECRET)); // assinatura
    }

    // Validar token e retornar o nome do usuário
    public static String validarToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }
}
