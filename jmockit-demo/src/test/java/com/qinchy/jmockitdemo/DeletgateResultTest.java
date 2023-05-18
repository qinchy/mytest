package com.qinchy.jmockitdemo;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Invocation;
import org.junit.Assert;
import org.junit.Test;


/**
 * 定制返回结果
 */
public class DeletgateResultTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void testDelegate() {
        new Expectations(SayHello.class) {
            {
                SayHello instance = new SayHello();
                instance.sayHello(anyString, anyInt);
                result = new Delegate() {
                    // 当调用sayHello(anyString, anyInt)时，返回的结果就会匹配delegate方法，
                    // 方法名可以自定义，当入参和返回要与sayHello(anyString, anyInt)匹配上
                    @SuppressWarnings("unused")
                    String delegate(Invocation inv, String who, int gender) {
                        // 如果是向动物鹦鹉Polly问好，就说hello,Polly
                        if ("Polly".equals(who)) {
                            return "hello,Polly";
                        }

                        // 其它的入参，还是走原有的方法调用
                        return inv.proceed(who, gender);
                    }
                };

            }
        };

        SayHello instance = new SayHello();
        Assert.assertEquals("hello Mr david", instance.sayHello("david", ISayHello.MALE));
        Assert.assertEquals("hello Mrs lucy", instance.sayHello("lucy", ISayHello.FEMALE));
        Assert.assertEquals("hello,Polly", instance.sayHello("Polly", ISayHello.FEMALE));
    }
}