package com.qinchy.jcedemo.builder;

/**
 * @author Administrator
 * <p>
 * 消息构造器
 */
public class MessageBuilder {
    public String getMessage(String name) {
        StringBuilder result = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            result.append("empty!");
        } else {
            result.append("Hello " + name);
        }
        return result.toString();
    }
}