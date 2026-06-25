package com.example.demo.service;

import com.example.demo.config.AgentProperties;
import com.example.demo.dto.AgentChatRequest;
import com.example.demo.dto.AgentMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class AgentService {

    private static final int MAX_MESSAGES = 20;
    private static final String CHAT_COMPLETIONS_PATH = "/chat/completions";

    private final AgentProperties properties;
    private final ObjectMapper objectMapper;
    private final Executor executor = Executors.newCachedThreadPool();

    public AgentService(AgentProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public SseEmitter streamChat(AgentChatRequest request) {
        SseEmitter emitter = new SseEmitter(properties.getTimeoutMs());

        CompletableFuture.runAsync(() -> {
            try {
                if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
                    sendError(emitter, "后端未配置 RESLIC_AI_API_KEY，暂时无法使用“苏区红”数字文物大模型。");
                    return;
                }

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(chatCompletionsUrl()))
                        .timeout(Duration.ofMillis(properties.getTimeoutMs()))
                        .header("Authorization", "Bearer " + properties.getApiKey())
                        .header("Content-Type", "application/json")
                        .header("Accept", "text/event-stream")
                        .POST(HttpRequest.BodyPublishers.ofString(buildPayload(request)))
                        .build();

                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                        .build();

                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    sendError(emitter, "大模型服务请求失败：" + response.statusCode());
                    return;
                }

                forwardModelStream(response.body(), emitter);
                emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                emitter.complete();
            } catch (Exception error) {
                sendError(emitter, "智能体暂时无法回复，请稍后重试。");
            }
        }, executor);

        return emitter;
    }

    private String buildPayload(AgentChatRequest request) throws IOException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", properties.getModel());
        payload.put("stream", true);
        payload.put("temperature", 0.4);
        payload.put("messages", buildMessages(request));
        return objectMapper.writeValueAsString(payload);
    }

    private List<Map<String, String>> buildMessages(AgentChatRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", properties.getSystemPrompt()));

        if (request != null && request.getContext() != null && !request.getContext().isEmpty()) {
            messages.add(Map.of("role", "system", "content", "当前页面上下文：" + request.getContext()));
        }

        List<AgentMessage> incoming = request == null ? List.of() : request.getMessages();
        if (incoming == null) {
            incoming = List.of();
        }

        int start = Math.max(0, incoming.size() - MAX_MESSAGES);
        for (int i = start; i < incoming.size(); i++) {
            AgentMessage message = incoming.get(i);
            String role = normalizeRole(message.getRole());
            String content = message.getContent() == null ? "" : message.getContent().trim();
            if (content.isBlank()) {
                continue;
            }
            messages.add(Map.of("role", role, "content", content));
        }

        return messages;
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "user";
        }
        String normalized = role.toLowerCase(Locale.ROOT);
        if ("assistant".equals(normalized)) {
            return "assistant";
        }
        return "user";
    }

    private String chatCompletionsUrl() {
        String baseUrl = properties.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (baseUrl.endsWith(CHAT_COMPLETIONS_PATH)) {
            return baseUrl;
        }
        return baseUrl + CHAT_COMPLETIONS_PATH;
    }

    private void forwardModelStream(String body, SseEmitter emitter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(body))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("data:")) {
                    continue;
                }
                String data = line.substring(5).trim();
                if (data.isBlank() || "[DONE]".equals(data)) {
                    continue;
                }
                String delta = extractDeltaContent(data);
                if (!delta.isBlank()) {
                    emitter.send(SseEmitter.event().name("delta").data(delta));
                }
            }
        }
    }

    private String extractDeltaContent(String data) throws IOException {
        JsonNode root = objectMapper.readTree(data);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            return "";
        }
        JsonNode delta = choices.get(0).path("delta");
        if (delta.has("content")) {
            return delta.path("content").asText("");
        }
        return "";
    }

    private void sendError(SseEmitter emitter, String message) {
        try {
            emitter.send(SseEmitter.event().name("error").data(message));
        } catch (IOException ignored) {
            // Client may have disconnected.
        } finally {
            emitter.complete();
        }
    }
}
