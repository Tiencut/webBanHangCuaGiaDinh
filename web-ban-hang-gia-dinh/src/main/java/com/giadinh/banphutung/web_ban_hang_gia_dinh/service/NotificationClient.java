package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Component
public class NotificationClient {

    @Value("${notifications.service.url:http://localhost:3100}")
    private String notificationsUrl;

    private HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @org.springframework.beans.factory.annotation.Value("${notifications.retry.attempts:3}")
    private int retryAttempts;

    @org.springframework.beans.factory.annotation.Value("${notifications.retry.baseBackoffMs:300}")
    private long baseBackoffMs;

    public boolean enqueue(String name, Map<String, Object> data) {
        Map<String, Object> body = Map.of("name", name, "data", data);
        String json;
        try {
            json = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            return false;
        }

        int attempt = 0;
        while (attempt < Math.max(1, retryAttempts)) {
            attempt++;
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(notificationsUrl + "/enqueue"))
                        .timeout(Duration.ofSeconds(5))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() >= 200 && resp.statusCode() < 300) return true;
            } catch (IOException e) {
                // fall through to retry
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
            long sleep = baseBackoffMs * (1L << (attempt - 1));
            long sleepMs = Math.min(sleep, 10_000);
            // use LockSupport to avoid Thread.sleep anti-pattern in loop
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(sleepMs));
        }
        return false;
    }
}
