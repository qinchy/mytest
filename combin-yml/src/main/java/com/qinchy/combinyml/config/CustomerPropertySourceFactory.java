package com.qinchy.combinyml.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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

    private static final String PROPERTY_SOURCE_NAME = "cloud";

    // 获取当前配置文件同类属性数组下标（0 baseed序号）
    private static final Pattern[] NEED_COMBIN_PATTERN = {Pattern.compile("(spring.cloud.gateway.routes\\[)(\\d)(].*)"), Pattern.compile("(config.operator\\[)(\\d)(].*)")};

    @Override
    public PropertySource<?> createPropertySource(String s, EncodedResource encodedResource) throws IOException {
        Properties fullProperties = new Properties();
        ClassPathResource ymlFilesResource = (ClassPathResource) encodedResource.getResource();

        String ymlFilePath = ymlFilesResource.getPath();

        Resource[] resources;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources(ResourceLoader.CLASSPATH_URL_PREFIX + ymlFilePath);
        } catch (IOException e) {
            log.error("get yml resources from {} failed", ymlFilePath, e);
            return null;
        }

        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();

        // offsets[0]：表示所有配置文件中“spring.cloud.gateway.routes”中元素的个数
        // offsets[1]：表示所有配置文件中“config.operator”中元素的个数
        int[] offsets = new int[NEED_COMBIN_PATTERN.length];

        // localIdxs[0]：表示当前配置文件中“spring.cloud.gateway.routes”中元素的个数
        // localIdxs[1]：表示当前配置文件中“config.operator”中元素的个数
        int[] localIdxs = new int[NEED_COMBIN_PATTERN.length];

        // 循环加载属性配置文件
        for (Resource resource : resources) {
            String resourceFilename = resource.getFilename();
            log.info("load properties from {} ", resourceFilename);

            factoryBean.setResources(resource);
            Properties props = factoryBean.getObject();

            for (int i = 0; i < NEED_COMBIN_PATTERN.length; i++) {
                localIdxs[i] = -1;
            }

            // 遍列当前文件中的所有配置项
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String propKey = entry.getKey().toString();
                int i = 0;
                for (; i < NEED_COMBIN_PATTERN.length; i++) {
                    Matcher matcher = NEED_COMBIN_PATTERN[i].matcher(propKey);
                    if (matcher.find()) {
                        // 当前yml配置文件中第i个匹配项的编号
                        localIdxs[i] = Integer.parseInt(matcher.group(2));
                        // 加上前面yml配置文件的编号
                        int idx = offsets[i] + localIdxs[i];
                        String fullPropKey = matcher.group(1) + idx + matcher.group(3);
                        fullProperties.put(fullPropKey, entry.getValue());

                        // 匹配上了则跳出循环
                        break;
                    }
                }

                // 遍列完当前文件的所有配置项都没有匹配的，则给个警告，并把key和value都放到fullProperties
                if (i == NEED_COMBIN_PATTERN.length) {
                    log.warn("用于配置spring.cloud.gateway参数的yml文件{}里不应该出现其他类型的配置参数：{}", resourceFilename, propKey);
                    fullProperties.put(propKey, entry.getValue());
                }
            }

            // 当前配置文件配置项遍列完成后，将次数累计到对应配置项的总次数中
            for (int i = 0; i < NEED_COMBIN_PATTERN.length; i++) {
                offsets[i] += localIdxs[i] + 1;
            }
        }

        return new PropertiesPropertySource(PROPERTY_SOURCE_NAME, fullProperties);
    }
}
