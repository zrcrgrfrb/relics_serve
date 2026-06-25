package com.example.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AgentChatRequest {
    private List<AgentMessage> messages;
    private Map<String, Object> context;
}
