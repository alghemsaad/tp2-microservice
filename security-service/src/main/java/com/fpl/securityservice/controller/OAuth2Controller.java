package com.fpl.securityservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OAuth2Controller {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;

    public OAuth2Controller(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                            JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username,
                                                     @RequestParam String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String scope = toScope(auth.getAuthorities());
            Instant now = Instant.now();

            String accessToken  = creerToken(auth.getName(), scope, now.plus(5, ChronoUnit.MINUTES));
            String refreshToken = creerToken(auth.getName(), null,  now.plus(30, ChronoUnit.MINUTES));

            return ResponseEntity.ok(Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        try {
            Jwt decoded = jwtDecoder.decode(refreshToken);   // erreur si faux ou expiré
            UserDetails user = userDetailsService.loadUserByUsername(decoded.getSubject());

            String newAccessToken = creerToken(user.getUsername(), toScope(user.getAuthorities()),
                    Instant.now().plus(5, ChronoUnit.MINUTES));

            return ResponseEntity.ok(Map.of(
                    "access_token", newAccessToken,
                    "refresh_token", refreshToken));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token invalide ou expiré"));
        }
    }

    private String toScope(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private String creerToken(String username, String scope, Instant expiration) {
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer("security-service")
                .subject(username)
                .issuedAt(Instant.now())
                .expiresAt(expiration);
        if (scope != null) {
            builder.claim("scope", scope);
        }
        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build())).getTokenValue();
    }
}