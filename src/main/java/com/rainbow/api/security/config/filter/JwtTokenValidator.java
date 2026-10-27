package com.rainbow.api.security.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.rainbow.api.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            DecodedJWT decodedJWT = jwtUtils.validateToken(token);

            String username = jwtUtils.getUsernameFromToken(decodedJWT);

            String authorities = jwtUtils.getClaimFromToken(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authoritiesList= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

            filterChain.doFilter(request, response);

        }
}