package com.water.delivery.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "waterdeliverysecretkey2026waterdeliverysupersecurekey";
    private long expiration = 604800000L; // 7 days
    private String header = "Authorization";
    private String prefix = "Bearer ";
}
