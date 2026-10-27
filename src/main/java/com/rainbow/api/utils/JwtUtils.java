package com.rainbow.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

 @Value("${security.jwt.private.key}")
 private String privateKey;

 @Value("${security.jwt.user.generator}")
 private String userGenerator;

    // creacion de tokens
    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        // queda en el context holder de spring security
        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String JwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000)) // 1 dia de expiracion
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

        return JwtToken;
    }

    // decodificacion y validacion de tokens
    public DecodedJWT validateToken(String token) {
        try{
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

      // si es correcto, devuelve el token decodificado
        return verifier.verify(token);

        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalido o expirado: " + exception.getMessage());
        }
    }

    // obtener el username del token decodificado
    public String getUsernameFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }

    // obtener un claim del token decodificado
    public Claim getClaimFromToken(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    // obtener todos los claums del token decodificado
    public Map<String, Claim> getAllClaimsFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }
}
