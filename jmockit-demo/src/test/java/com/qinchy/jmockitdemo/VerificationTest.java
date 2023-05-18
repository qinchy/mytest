package com.qinchy.jmockitdemo;

import mockit.Expectations;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Verification的使用
 */
public class VerificationTest {
    @Test
    public void testVerification() {
        // 录制阶段
        Calendar cal = Calendar.getInstance();
        new Expectations(Calendar.class) {
            {
                // 对cal.get方法进行录制，并匹配参数 Calendar.YEAR
                cal.get(Calendar.YEAR);
                result = 2016;// 年份不再返回当前年份。而是返回2016年

                cal.get(Calendar.HOUR_OF_DAY);
                result = 7;// 小时不再返回当前小时。而是返回早上7点钟
            }
        };

        // 重放阶段
        Calendar now = Calendar.getInstance();
        Assert.assertEquals(2016, now.get(Calendar.YEAR));
        Assert.assertEquals(7, now.get(Calendar.HOUR_OF_DAY));

        // 验证阶段
        new Verifications() {
            {
                Calendar.getInstance();
                // 限定上面重放阶段的方法只调用了1次，当然也可以不限定
                times = 1;

                cal.get(anyInt);
                // 限定上面重放阶段的方法只调用了2次，当然也可以不限定
                times = 2;
            }
        };

    }
}