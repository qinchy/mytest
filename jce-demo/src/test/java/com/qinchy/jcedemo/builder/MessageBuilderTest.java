package com.qinchy.jcedemo.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    @Test
    public void getMessage1() {
        MessageBuilder builder = new MessageBuilder();
        String message = builder.getMessage("qinchy");
        System.out.println(message);
    }

    @Test
    public void getMessage2(){
        MessageBuilder obj = new MessageBuilder();
        assertEquals("empty!", obj.getMessage(null));
    }
}