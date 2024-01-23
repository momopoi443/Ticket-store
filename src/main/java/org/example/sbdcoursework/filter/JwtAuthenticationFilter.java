package org.example.sbdcoursework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.entity.token.AccessToken;
import org.example.sbdcoursework.entity.user.UserRole;
import org.example.sbdcoursework.exception.external.InvalidTokenException;
import org.example.sbdcoursework.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String AUTHZ_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authzHeader = request.getHeader("Authorization");
        if (authzHeader == null || !authzHeader.startsWith(AUTHZ_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        tryAuthenticateIfCurrentlyNot(authzHeader.substring(AUTHZ_HEADER_PREFIX.length()), request);
        filterChain.doFilter(request, response);
    }

    private void tryAuthenticateIfCurrentlyNot(String encodedToken, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                AccessToken accessToken = jwtService.parseAccessToken(encodedToken);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        accessToken.claims().sub(),
                        null,
                        List.of(convertRoleToSimpleGrantedAuthority(accessToken.claims().role()))
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (InvalidTokenException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private SimpleGrantedAuthority convertRoleToSimpleGrantedAuthority(UserRole role) {
        return new SimpleGrantedAuthority("ROLE_" + role.name());
    }
}
