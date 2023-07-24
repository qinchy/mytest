package com.qinchy.combinyml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gateway")
@PropertySource(value = "classpath:${gatewayconfig}", factory = CustomerPropertySourceFactory.class)
@Data
public class GatewayConfig {
    public Map routes;
}
