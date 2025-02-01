package com.url.UrlShortenerBackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.UrlShortenerBackend.Repositories.UrlMappingRepository;
import com.url.UrlShortenerBackend.models.UrlMapping;
import com.url.UrlShortenerBackend.models.User;

@Service
public class AiService {

  @Value("${gemini.api.url}")
  private String geminiApiUrl;

  @Value("${gemini.api.key}")
  private String geminiApiKey;

  @Autowired
  private UrlMappingRepository urlMappingRepository;

  private String prompt_to_get_ai_slugs = """
            You don't have to visited the URL just treat it as a string and For the Given URL Link Generate a list of 5 meaningful shortest possible slugs for a given URL.
            The slugs should:
            As short as possible, Be a meaningful acronym or abbreviation that reflects the content or purpose of the URL, Use a combination of words that clearly describe the main theme or subject of the URL, Ensure that each slug is unique but directly relevant to the content of the link, It is mix of Upper and lower case.
            the output should be strictly in json format:{"slugs":["slug1", "slug2", "slug3", "slug4", "slug5"]}
            Given URL:
      """;

  private WebClient webClient;

  public AiService(WebClient.Builder webClient) {
    this.webClient = webClient.build();
  }

  public String callAiUsingPrompt(String prompt) {

    Map<String, Object> requestBody = Map.of(
        "contents", new Object[] {
            Map.of(
                "parts", new Object[] {
                    Map.of("text", prompt)
                })
        });

    String response = webClient.post()
        .uri(geminiApiUrl + geminiApiKey)
        .header("Content-Type", "application/json")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();

    return response;
  }

  private String getUniqueSlug() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    Random random = new Random();
    StringBuilder shortUrl = new StringBuilder(8);

    for (int i = 0; i < 3; i++) {
      shortUrl.append(characters.charAt(random.nextInt(characters.length())));
    }
    return shortUrl.toString();
  }

  public List<String> getAiShortUrls(String originalUrl) {
    String question = prompt_to_get_ai_slugs + originalUrl;
    String response = callAiUsingPrompt(question);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(response);

      String text = rootNode.path("candidates").get(0)
          .path("content").path("parts").get(0)
          .path("text").asText();
      String cleanedJson = text.replaceAll("```json|```", "").trim();
      JsonNode jsonObject = objectMapper.readTree(cleanedJson);
      JsonNode slugsNode = jsonObject.path("slugs");
      @SuppressWarnings("unchecked")
      List<String> slugs = objectMapper.convertValue(slugsNode, List.class);

      List<String> urls = new ArrayList<>();

      String uniqueSlug = getUniqueSlug();
      for (String slug : slugs) {
        urls.add(slug + uniqueSlug);
      }

      return urls;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String addAiUrl(String originalUrl, String url, User user) {
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setShortUrl(url);
    urlMapping.setUser(user);
    urlMapping.setCreatedDate(LocalDateTime.now());
    if (urlMappingRepository.save(urlMapping) != null)
      return url;

    return null;
  }

}
