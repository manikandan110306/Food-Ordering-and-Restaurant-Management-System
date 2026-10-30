package com.foodordering.backend.config;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import lombok.RequiredArgsConstructor;

import com.foodordering.backend.entity.User;
import com.foodordering.backend.repository.UserRepository;
import com.foodordering.backend.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) 
        {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        System.out.println("JWT FILTER: Token received");

        try 
        {
            String email = jwtService.extractEmail(token);
            System.out.println("JWT FILTER: Token is valid and email extracted");

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) 
            {
                userRepository.findByEmail(email)
                        .filter(User::isEnabled)
                        .filter(user -> jwtService.isTokenValid(token, user))
                        .ifPresent(user -> {

                            List<SimpleGrantedAuthority> authorities = List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + user.getRole().name()));

                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities);

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request));

                            SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);

                            System.out.println("JWT FILTER: User authenticated successfully");
                        });
            }

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            System.out.println("JWT FILTER ERROR: " + exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}