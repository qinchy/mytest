package com.qinchy.jmockitdemo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

//代码覆盖率测试，观察覆盖率的计算方式,去target/coverage-report目录下，查看SayHello这个类的覆盖率
public class CodeCoverageTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    ISayHello sayHello = new SayHello();

    //测试 sayHello(String who, int gender);
    @Test
    public void testSayHello1() {
        Assert.assertEquals("hello Mr david", sayHello.sayHello("david", ISayHello.MALE));
        Assert.assertEquals("hello Mrs lucy", sayHello.sayHello("lucy", ISayHello.FEMALE));
        thrown.expect(IllegalArgumentException.class);
        sayHello.sayHello("david", 3);
    }

    //测试 sayHello(String[] who, int[] gender)
    @Test
    public void testSayHello2() {
        String[] who = new String[]{"david", "lucy"};
        int[] gender = new int[]{ISayHello.MALE, ISayHello.FEMALE};
        List<String> result = sayHello.sayHello(who, gender);
        Assert.assertEquals("hello Mr david", result.get(0));
        Assert.assertEquals("hello Mrs lucy", result.get(1));
    }

}