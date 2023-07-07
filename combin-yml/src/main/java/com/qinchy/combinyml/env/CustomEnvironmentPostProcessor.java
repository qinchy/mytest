package com.qinchy.combinyml.env;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    /**
     * 根据正则，获取配置项序号
     *
     * @see CustomEnvironmentPostProcessor#getPropNum(java.lang.String)
     */
//    private String REGULAR = "spring.cloud.gateway.routes\\[(.+)\\]\\.(.+)$";
    private final String REGULAR = "operator.names\\[(.+)\\]$";

    /**
     * 存放yaml文件名
     */
//    private final String[] profiles = {"application.yml", "application-dev.yml",
//            "application-prd.yml", "application-pre.yml", "application-test.yml"};
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //这里可以取运行环境，自行拓展配置文件的文件名
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> profileFiles = Arrays.stream(activeProfiles)
                .map(s -> StringUtils.join("application-", s, ".yml"))
                .collect(toList());
        // 把默认配置文件也放到list中
        profileFiles.add("application.yml");
        //以配置文件进行分组
        List<Map<String, List<Map.Entry<String, OriginTrackedValue>>>> propGroupList = new ArrayList<>();
        //遍历profiles
        for (String profile : profileFiles) {
            //加载配置文件,
            Resource resource = new ClassPathResource(profile);
            PropertySource<?> propertySource = loadProfiles(resource);
            //记录所有配置项
            Map<String, OriginTrackedValue> sourceMap = (Map<String, OriginTrackedValue>) propertySource.getSource();
            //根据序号分组
            Map<String, List<Map.Entry<String, OriginTrackedValue>>> group = sourceMap.entrySet()
                    .stream()
                    .filter(e -> this.isMatch(e.getKey()))
                    .collect(Collectors.groupingBy(s -> String.valueOf(getPropNum(s.getKey()))));
            propGroupList.add(group);
        }

        //添加到Spring 配置上下文
        this.merge(environment, propGroupList);
    }


    /**
     * 将所有配置文件的配置项合并为一个 PropertySource，并添加到Spring 配置上下文
     *
     * @param groupList PropertySource 分组
     */
    public void merge(ConfigurableEnvironment environment, List<Map<String, List<Map.Entry<String, OriginTrackedValue>>>> groupList) {
        Map<String, OriginTrackedValue> routesMap = new TreeMap<>();
        int i = 0;
        for (Map<String, List<Map.Entry<String, OriginTrackedValue>>> groupItem : groupList) {
            for (Map.Entry<String, List<Map.Entry<String, OriginTrackedValue>>> routeItem : groupItem.entrySet()) {
                for (Map.Entry<String, OriginTrackedValue> routePropItem : routeItem.getValue()) {
                    int itemSort = getPropNum(routePropItem.getKey());
                    // 把原生的序号用我们自己的序号替换掉。这样就避免了被覆盖。然后用替换后的key来存放value。
                    String newKey = routePropItem.getKey().replaceFirst(Integer.toString(itemSort), Integer.toString(i));
                    routesMap.put(newKey, routePropItem.getValue());
                }
                i++;
            }
        }

        PropertySource<Map<String, OriginTrackedValue>> propertySource = new PropertySource<Map<String, OriginTrackedValue>>(this.getClass().getName(), routesMap) {
            @Override
            public OriginTrackedValue getProperty(String s) {
                return routesMap.get(s);
            }
        };

        // 添加到spring配置上下文中
        environment.getPropertySources().addFirst(propertySource);
    }

    /**
     * 通过正则获取当前route配置字符串的序号
     *
     * @param key spring.cloud.gateway.routes[n].xxx
     * @return n
     */
    private int getPropNum(String key) {
        Pattern r = Pattern.compile(REGULAR);
        Matcher m = r.matcher(key);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }

        throw new RuntimeException("配置格式有问题");
    }


    /**
     * 目标配置项是否符合匹配要求
     */
    private boolean isMatch(String key) {
        try {
            getPropNum(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取配置项
     *
     * @param resource
     * @return
     */
    @SneakyThrows
    private PropertySource<?> loadProfiles(Resource resource) {
        if (Objects.requireNonNull(resource.getFilename()).contains("yml")) {
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            List<PropertySource<?>> propertySources = sourceLoader.load(resource.getFilename(), resource);
            return propertySources.get(0);
        }
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        return new PropertiesPropertySource(resource.getFilename(), properties);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}