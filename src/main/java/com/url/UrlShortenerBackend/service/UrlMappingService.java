package com.url.UrlShortenerBackend.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.url.UrlShortenerBackend.Repositories.UrlMappingRepository;
import com.url.UrlShortenerBackend.dtos.UrlMappingDIO;
import com.url.UrlShortenerBackend.models.UrlMapping;
import com.url.UrlShortenerBackend.models.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingService {

  private UrlMappingRepository urlMappingRepository;

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

}
