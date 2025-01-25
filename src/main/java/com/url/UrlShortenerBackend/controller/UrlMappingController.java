package com.url.UrlShortenerBackend.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.url.UrlShortenerBackend.dtos.ClickEventDTO;
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

  @GetMapping("/myurls")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<UrlMappingDIO>> getUserUrls(Principal principal) {

    User user = userService.findByUsername(principal.getName());
    List<UrlMappingDIO> url = urlMappingService.getUrlsByUser(user);

    return ResponseEntity.ok(url);
  }

  @GetMapping("/analytics/{shortUrl}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) {

    DateTimeFormatter formater = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    LocalDateTime start = LocalDateTime.parse(startDate, formater);
    LocalDateTime end = LocalDateTime.parse(endDate, formater);

    List<ClickEventDTO> clickEventDTOs = urlMappingService.getClickEventsByDate(shortUrl, start, end);

    return ResponseEntity.ok(clickEventDTOs);
  }

  @GetMapping("/totalclicks")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) {

    DateTimeFormatter formater = DateTimeFormatter.ISO_LOCAL_DATE;
    User user = userService.findByUsername(principal.getName());
    LocalDate start = LocalDate.parse(startDate, formater);
    LocalDate end = LocalDate.parse(endDate, formater);

    Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);

    return ResponseEntity.ok(totalClicks);
  }

}
