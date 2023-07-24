package com.qinchy.combinyml.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class ConfigurationPrinter implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public void printMergedConfiguration() {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        StringBuffer yamlFileNames = new StringBuffer("bootstrap.yml\n");
        environment.getPropertySources().stream().forEach(propertySource -> {
            if (propertySource.getName().contains("bootstrapProperties")) {
                yamlFileNames.append(StringUtils.substringAfter(propertySource.getName(), "bootstrapProperties-") + "\n");
            }
        });
        log.info("\n************** Configuration files priority **************\n" + yamlFileNames);

        ConfigurationPropertySources.attach(environment);
        Binder binder = Binder.get(environment);

        Map<String, Object> properties = binder.bind("", Map.class).orElse(null);
        properties.remove("java");
        properties.remove("sun");
        properties.remove("jboss");
        properties.remove("catalina");
        properties.entrySet().stream().sorted(Map.Entry.comparingByKey());
        Map<String, Object> sortedProperties = new LinkedHashMap<>();
        properties.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).forEachOrdered(x -> sortedProperties.put(x.getKey(), x.getValue()));
        printYaml(JSONObject.toJSONString(sortedProperties));
    }

    @Override
    public void run(ApplicationArguments args) {
        printMergedConfiguration();
    }

    private void printYaml(String json) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            Object jsonObject = jsonMapper.readValue(json, Object.class);

            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            String yaml = yamlMapper.writeValueAsString(jsonObject);
            log.info("\n************** All configs **************\n" + yaml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}