package com.qinchy.combinyml.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ConigurationPrinterEvent implements ApplicationListener<SpringApplicationEvent> {

    private static int printCount = 0;

    private final ConfigurableApplicationContext applicationContext;

    @Autowired
    public ConigurationPrinterEvent(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
        if (printCount == 0) {
            Environment environment = applicationContext.getEnvironment();
            ConfigurationPropertySources.attach(environment);
            Binder binder = Binder.get(environment);
            Map<String, Object> properties = binder.bind("", Map.class).orElse(null);
            printYaml(JSONObject.toJSONString(properties));
        }
    }

    private void printYaml(String json) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            Object jsonObject = jsonMapper.readValue(json, Object.class);

            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            String yaml = yamlMapper.writeValueAsString(jsonObject);
            log.info("\n************** Print all configs **************\n" + yaml);
            printCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}