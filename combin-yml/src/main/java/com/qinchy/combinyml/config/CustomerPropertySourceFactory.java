package com.qinchy.combinyml.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合并配置文件
 */
@Slf4j
public class CustomerPropertySourceFactory implements PropertySourceFactory {

    // 获取当前配置文件同类属性数组下标（0 baseed序号）
    private static final Pattern[] NEED_COMBIN_PATTERN = {Pattern.compile("(spring.cloud.gateway.routes\\[)(\\d)(].*)")};

    @Override
    public PropertySource<?> createPropertySource(String s, EncodedResource encodedResource) throws IOException {
        Properties fullProperties = new Properties();
        ClassPathResource ymlFilesResource = (ClassPathResource) encodedResource.getResource();
        String filename = ymlFilesResource.getFilename();
        String[] ymlFilePaths = ymlFilesResource.getPath().split(",");

        AbstractEnvironment environment = new StandardEnvironment();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();

        int[] offsets = new int[NEED_COMBIN_PATTERN.length];
        int[] localIdxs = new int[NEED_COMBIN_PATTERN.length];

        // 循环加载属性配置文件
        for (String ymlFilePath : ymlFilePaths) {
            String location = ymlFilePath.trim();
            if (location.isEmpty()) {
                // 忽略空文件名
                continue;
            }

            String resolvedLocation = environment.resolveRequiredPlaceholders(location);
            Resource resource = resourceLoader.getResource(resolvedLocation);
            factoryBean.setResources(resource);
            Properties props = factoryBean.getObject();

            for (int i = 0; i < NEED_COMBIN_PATTERN.length; i++) {
                localIdxs[i] = -1;
            }

            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String propKey = entry.getKey().toString();
                int i = 0;
                for (; i < NEED_COMBIN_PATTERN.length; i++) {
                    Matcher matcher = NEED_COMBIN_PATTERN[i].matcher(propKey);
                    if (matcher.find()) {
                        localIdxs[i] = Integer.parseInt(matcher.group(2));
                        int idx = offsets[i] + localIdxs[i];
                        String fullPropKey = matcher.group(1) + idx + matcher.group(3);
                        fullProperties.put(fullPropKey, entry.getValue());
                        break;
                    }
                }

                if (i == NEED_COMBIN_PATTERN.length) {
                    log.warn("用于配置spring.cloud.gateway参数的yml文件{}里不应该出现其他类型的配置参数：{}", location, propKey);
                    fullProperties.put(propKey, entry.getValue());
                }
            }

            for (int i = 0; i < NEED_COMBIN_PATTERN.length; i++) {
                offsets[i] += localIdxs[i] + 1;
            }
        }

        return new PropertiesPropertySource(filename, fullProperties);
    }
}
