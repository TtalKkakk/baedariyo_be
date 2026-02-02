package com.house.biet.auth.infrastructure.security;

import com.house.biet.auth.infrastructure.jwt.JwtAuthenticationToken;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.vo.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws java.io.IOException, jakarta.servlet.ServletException {

        String token = jwtProvider.resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            Long accountId = jwtProvider.getAccountId(token);
            UserRole role = jwtProvider.getRole(token);

            AuthPrincipal principal = new AuthPrincipal(accountId, role);
            JwtAuthenticationToken authentication =
                    new JwtAuthenticationToken(principal);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}