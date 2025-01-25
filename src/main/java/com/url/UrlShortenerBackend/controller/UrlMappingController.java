package com.url.UrlShortenerBackend.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.UrlShortenerBackend.dtos.UrlMappingDIO;
import com.url.UrlShortenerBackend.models.User;
import com.url.UrlShortenerBackend.service.UrlMappingService;
import com.url.UrlShortenerBackend.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/urls")
public class UrlMappingController {

  private UrlMappingService urlMappingService;
  private UserService userService;

  @PostMapping("/shorten")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UrlMappingDIO> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {

    String originalUrl = request.get("originalUrl");
    User user = userService.findByUsername(principal.getName());
    UrlMappingDIO urlMappingDIO = urlMappingService.createShortUrl(originalUrl, user);
    return ResponseEntity.ok(urlMappingDIO);
  }
}
