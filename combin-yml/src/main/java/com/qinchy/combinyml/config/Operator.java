package com.qinchy.combinyml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "operator")
@Data
public class Operator {
    private List<String> names;
}
