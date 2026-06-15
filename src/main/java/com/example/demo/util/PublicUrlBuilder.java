package com.example.demo.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class PublicUrlBuilder {

    @Value("${app.public-base-url:}")
    private String publicBaseUrl;

    public String resolveAssetUrl(String url, HttpServletRequest request) {
        if (url == null || url.isBlank()) {
            return url;
        }
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("data:")) {
            return url;
        }

        String path = url.startsWith("/") ? url : "/" + url;
        return baseUrl(request) + path;
    }

    public String buildUploadUrl(String filename, HttpServletRequest request) {
        return baseUrl(request) + request.getContextPath() + "/uploads/" + filename;
    }

    private String baseUrl(HttpServletRequest request) {
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return trimTrailingSlash(publicBaseUrl);
        }
        return trimTrailingSlash(ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .replaceQuery(null)
                .build()
                .toUriString());
    }

    private String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
