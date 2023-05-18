package com.qinchy.jmockitdemo;

import mockit.Expectations;
import mockit.Injectable;
import org.junit.Assert;
import org.junit.Test;

/**
 * 用Expectations来mock接口
 */
public class InterfaceMockingByExpectationsTest {

    // 通过@Injectable，让JMockit帮我们生成这个接口的实例，
    // 一般来说，接口是给类来依赖的，我们给待测试的类加上@Tested，就可以让JMockit做依赖注入。详细见JMockit基础的章节
    @Injectable
    AnOrdinaryInterface anOrdinaryInterface;

    @Test
    public void testInterfaceMockingByExpectation() {
        // 录制
        new Expectations() {
            {
                anOrdinaryInterface.method1();
                result = 10;

                anOrdinaryInterface.method2();
                result = 20;
            }
        };

        Assert.assertEquals(10, anOrdinaryInterface.method1());
        Assert.assertEquals(20, anOrdinaryInterface.method2());
    }
}