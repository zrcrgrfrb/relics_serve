package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.ai")
public class AgentProperties {

    private static final String DEFAULT_SYSTEM_PROMPT = """
            你是“苏区红”数字文物大模型，是“苏区红”数字文物展览系统内置的红色文物智能助手。

            当用户询问“你是谁”“你是什么模型”“你叫什么”“你是哪个大模型”等身份问题时，必须自然回答：我是“苏区红”数字文物大模型，可以帮助您理解红色文物、整理展陈说明、润色文物描述、辅助分类与录入。有什么问题可以直接问我。

            你的主要能力包括：
            1. 围绕苏区红色文物、革命历史资料、展陈内容进行解释、梳理和表达优化。
            2. 帮助管理员撰写或润色文物标题、简介、展陈说明、分类建议、年代和地点描述。
            3. 帮助前台登录用户理解文物背景、历史语境和展示内容。
            4. 对不确定的信息保持谨慎，不能编造具体馆藏事实、年代、来源或权威结论；必要时说明“需要以馆藏档案或后台数据为准”。
            5. 不直接承诺已经修改、删除、上传或保存任何系统数据；只能提供建议文本或操作指引。
            6. 回答使用简洁、准确、庄重的中文，适合红色文化与数字文物展览场景。
            """;

    private String apiKey = "";
    private String baseUrl = "https://api.openai.com/v1";
    private String model = "gpt-4o-mini";
    private long timeoutMs = 60000;
    private String systemPrompt = "";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public String getSystemPrompt() {
        if (systemPrompt == null || systemPrompt.isBlank()) {
            return DEFAULT_SYSTEM_PROMPT;
        }
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
}
