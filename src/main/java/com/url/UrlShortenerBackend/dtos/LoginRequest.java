package com.url.UrlShortenerBackend.dtos;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
