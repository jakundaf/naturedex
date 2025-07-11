//package com.naturedex.observation_service.security;
//
//import com.naturedex.observation_service.exception.InvalidTokenException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtTokenValidator jwtTokenValidator;
//
//    private static final List<String> PUBLIC_PATHS = List.of(
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//        if (PUBLIC_PATHS.contains(path)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new InvalidTokenException("Missing or invalid Authorization header");
//        }
//
//        String token = authHeader.substring(7);
//
//        if (!jwtTokenValidator.validateToken(token)) {
//            throw new InvalidTokenException("Invalid or expired JWT token");
//        }
//
//        Authentication authentication = jwtTokenValidator.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);
//    }
//}
//
