package com.url.UrlShortenerBackend.security;

import lombok.AllArgsConstructor;
import lombok.Data;
// DTO class

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
  private String token;
}
