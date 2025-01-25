package com.url.UrlShortenerBackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.UrlShortenerBackend.models.UrlMapping;
import com.url.UrlShortenerBackend.models.User;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
  UrlMapping findByShortUrl(String shortUrl);

  List<UrlMapping> findByUser(User user);
}
