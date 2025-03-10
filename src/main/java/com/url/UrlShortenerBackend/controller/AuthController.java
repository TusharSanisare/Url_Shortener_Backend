package com.url.UrlShortenerBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.UrlShortenerBackend.dtos.LoginRequest;
import com.url.UrlShortenerBackend.dtos.RegisterRequest;
import com.url.UrlShortenerBackend.models.User;
import com.url.UrlShortenerBackend.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private UserService userService;

  @PostMapping("/public/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(userService.authenticateUser(loginRequest));
  }

  @PostMapping("/public/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(registerRequest.getPassword());
    user.setEmail(registerRequest.getEmail());
    user.setRole("ROLE_USER");

    userService.registerUser(user);

    return ResponseEntity.ok("User registered seccessfully");
  }
}
