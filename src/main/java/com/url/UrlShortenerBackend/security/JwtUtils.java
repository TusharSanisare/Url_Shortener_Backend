package com.url.UrlShortenerBackend.security;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.url.UrlShortenerBackend.service.UserDetailsImpl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String generateToken(UserDetailsImpl userDetailsImpl) {
    String username = userDetailsImpl.getUsername();
    String roles = userDetailsImpl.getAuthorities().stream()
        .map(authority -> authority.getAuthority())
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(username)
        .claim("roles", roles)
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + jwtExpirationMs))
        .signWith(key())
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build().parseSignedClaims(token)
        .getPayload().getSubject();

  }

  // public String getUserNameFromJwtToken(String token) { // gpt
  // return Jwts.parserBuilder()
  // .setSigningKey(key())
  // .build()
  // .parseClaimsJws(token)
  // .getBody()
  // .getSubject();
  // }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public boolean validateToken(String authToken) {
    try {

      Jwts.parser().verifyWith((SecretKey) key())
          .build().parseSignedClaims(authToken);

      return true;

    } catch (JwtException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
