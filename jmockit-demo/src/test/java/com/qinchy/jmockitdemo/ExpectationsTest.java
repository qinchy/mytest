package com.qinchy.jmockitdemo;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Expectations对外部类的mock对象进行录制
 */
public class ExpectationsTest {
    @Mocked
    Calendar cal;

    @Test
    public void testRecordOutside() {
        new Expectations() {
            {
                // 对cal.get方法进行录制，并匹配参数 Calendar.YEAR
                cal.get(Calendar.YEAR);
                result = 2016;// 年份不再返回当前小时。而是返回2016年

                // 对cal.get方法进行录制，并匹配参数 Calendar.HOUR_OF_DAY
                cal.get(Calendar.HOUR_OF_DAY);
                result = 7;// 小时不再返回当前小时。而是返回早上7点钟
            }
        };

        Assert.assertEquals(2016, cal.get(Calendar.YEAR));
        Assert.assertEquals(7, cal.get(Calendar.HOUR_OF_DAY));

        // 因为没有录制过，所以这里月份返回默认值 0
        Assert.assertEquals(0, cal.get(Calendar.DAY_OF_MONTH));
    }

}