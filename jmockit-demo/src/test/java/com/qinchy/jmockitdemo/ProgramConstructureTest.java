package com.qinchy.jmockitdemo;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;

/**
 * JMockit的程序结构
 */
public class ProgramConstructureTest {

    /**
     * 这是一个测试属性
     */
    @Mocked
    HelloJMockit helloJMockit;

    @Test
    public void test1() {

        // 1.录制(Record)
        new Expectations() {
            {
                helloJMockit.sayHello();
                // 期待上述调用的返回是"hello,david"，而不是返回"hello,JMockit"
                result = "hello,david";
            }
        };

        // 2.重放(Replay)
        String msg = helloJMockit.sayHello();
        Assert.assertEquals("hello,david", msg);

        // 3.验证(Verification)
        new Verifications() {
            {
                helloJMockit.sayHello();

                times = 1;
            }
        };
    }

    @Test
    public void test2(@Mocked HelloJMockit helloJMockit /* 这是一个测试参数 */) {
        // 1.录制(Record)
        new Expectations() {
            {
                helloJMockit.sayHello();
                // 期待上述调用的返回是"hello,david"，而不是返回"hello,JMockit"
                result = "hello,david";
            }
        };

        // 2.重放(Replay)
        String msg = helloJMockit.sayHello();
        Assert.assertEquals("hello,david", msg);

        // 3.验证(Verification)
        new Verifications() {
            {
                helloJMockit.sayHello();
                // 验证helloJMockit.sayHello()这个方法调用了1次
                times = 1;
            }
        };
    }
}
