//package com.naturedex.observation_service.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtTokenValidator {
//
//    private final Key secretKey;
//
//    public JwtTokenValidator(@Value("${jwt.secret}") String secret) {
//        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        Map<String, Object> claimsMap = new HashMap<>(claims);
//
//        Jwt jwt = new Jwt(
//                token,
//                claims.getIssuedAt().toInstant(),
//                claims.getExpiration().toInstant(),
//                Map.of("alg", "HS256"),
//                claimsMap
//        );
//
//        return new JwtAuthenticationToken(jwt);
//    }
//}
//
