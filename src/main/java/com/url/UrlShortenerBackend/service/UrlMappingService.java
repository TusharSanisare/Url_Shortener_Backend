package com.url.UrlShortenerBackend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.url.UrlShortenerBackend.Repositories.ClickEventRepository;
import com.url.UrlShortenerBackend.Repositories.UrlMappingRepository;
import com.url.UrlShortenerBackend.dtos.ClickEventDTO;
import com.url.UrlShortenerBackend.dtos.UrlMappingDIO;
import com.url.UrlShortenerBackend.models.ClickEvent;
import com.url.UrlShortenerBackend.models.UrlMapping;
import com.url.UrlShortenerBackend.models.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingService {

  private UrlMappingRepository urlMappingRepository;
  private ClickEventRepository clickEventRepository;

  public UrlMappingDIO createShortUrl(String originalUrl, User user) {
    String shortUrl = generateShortUrl();
    UrlMapping urlMapping = new UrlMapping();

    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortUrl(shortUrl);
    urlMapping.setUser(user);
    urlMapping.setCreatedDate(LocalDateTime.now());

    UrlMapping saveUrlMapping = urlMappingRepository.save(urlMapping);

    return convertToDIO(saveUrlMapping);
  }

  public UrlMappingDIO convertToDIO(UrlMapping urlMapping) {
    UrlMappingDIO urlMappingDIO = new UrlMappingDIO();
    urlMappingDIO.setId(urlMapping.getId());
    urlMappingDIO.setOriginalUrl(urlMapping.getOriginalUrl());
    urlMappingDIO.setShortUrl(urlMapping.getShortUrl());
    urlMappingDIO.setClickCount(urlMapping.getClickCount());
    urlMappingDIO.setCreatedDate(urlMapping.getCreatedDate());
    urlMappingDIO.setUsername(urlMapping.getUser().getUsername());

    return urlMappingDIO;
  }

  private String generateShortUrl() {

    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();

    StringBuilder shortUrl = new StringBuilder(8);
    for (int i = 0; i < 8; i++) {
      shortUrl.append(characters.charAt(random.nextInt(characters.length())));
    }

    return shortUrl.toString();
  }

  public List<UrlMappingDIO> getUrlsByUser(User user) {

    return urlMappingRepository.findByUser(user).stream().map(this::convertToDIO).toList();
  }

  public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {

    UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
    if (urlMapping != null) {
      return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
          .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
          .entrySet().stream().map(entry -> {
            ClickEventDTO clickEventDTO = new ClickEventDTO();
            clickEventDTO.setClickDate(entry.getKey());
            clickEventDTO.setCount(entry.getValue());
            return clickEventDTO;
          }).collect(Collectors.toList());
    }

    return null;
  }

  public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {

    List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
    List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,
        start.atStartOfDay(), end.plusDays(1).atStartOfDay());

    return clickEvents.stream()
        .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));

  }

  public UrlMapping getOriginalUrl(String shortUrl) {
    UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

    if (urlMapping != null) {
      urlMapping.setClickCount(urlMapping.getClickCount() + 1);
      urlMappingRepository.save(urlMapping);

      ClickEvent clickEvent = new ClickEvent();
      clickEvent.setClickDate(LocalDateTime.now());
      clickEvent.setUrlMapping(urlMapping);
      clickEventRepository.save(clickEvent);
    }

    return urlMapping;
  }

}
