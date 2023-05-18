package com.qinchy.jmockitdemo;

import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 同一方法返回时序结果
 */
public class ReturnSequenceResultInOneMethodTest {

    // 一个类所有实例的某个方法，返回时序结果。
    // 适用场景：每次调用，期望返回的数据不一样。比如从tcp数据流中拿数据
    @Test
    public void testIfMethodOfClass() {
        AnOrdinaryClass instance = new AnOrdinaryClass();
        new Expectations(AnOrdinaryClass.class) {
            {
                instance.ordinaryMethod();
                // 对类AnOrdinaryClass所有实例调用ordinaryMethod方法时，依次返回1，2，3，4，5
                result = new int[]{1, 2, 3, 4, 5};
            }
        };
        AnOrdinaryClass instance1 = new AnOrdinaryClass();
        Assert.assertEquals(1, instance1.ordinaryMethod());
        Assert.assertEquals(2, instance1.ordinaryMethod());
        Assert.assertEquals(3, instance1.ordinaryMethod());
        Assert.assertEquals(4, instance1.ordinaryMethod());
        Assert.assertEquals(5, instance1.ordinaryMethod());

        // 因为在上面录制脚本中，只录制了5个结果，当大于5时，就以最后一次结果为准
        Assert.assertEquals(5, instance1.ordinaryMethod());
        Assert.assertEquals(5, instance1.ordinaryMethod());
    }

    // 与上述不一样的地方，仅仅是对某一个实例的返回值进行录制
    @Test
    public void testIfMethodOfIntance() {
        AnOrdinaryClass instance = new AnOrdinaryClass();
        new Expectations(instance) {
            {
                instance.ordinaryMethod();
                // 对实例instance调用ordinaryMethod方法时，依次返回1，2，3，4，5
                result = new int[]{1, 2, 3, 4, 5};
            }
        };
        // 只影响了instance这个实例
        Assert.assertEquals(1, instance.ordinaryMethod());
        Assert.assertEquals(2, instance.ordinaryMethod());
        Assert.assertEquals(3, instance.ordinaryMethod());
        Assert.assertEquals(4, instance.ordinaryMethod());
        Assert.assertEquals(5, instance.ordinaryMethod());

        // 因为在上面录制脚本中，只录制了5个结果，当大于5时，就以最后一次结果为准
        Assert.assertEquals(5, instance.ordinaryMethod());
        Assert.assertEquals(5, instance.ordinaryMethod());

        // 类AnOrdinaryClass的其它实例并不会受到影响
        AnOrdinaryClass instance1 = new AnOrdinaryClass();
        // ordinaryMethod这个方法本来就返回2
        Assert.assertEquals(2, instance1.ordinaryMethod());
        Assert.assertEquals(2, instance1.ordinaryMethod());
    }

    /**
     * 下面以对tcp数据流返回数据为例子，进行mock
     */
    @Test
    public void testInputStreamSequence() {
        try {
            // 依据地址创建URL
            URL url = new URL("http://jmockit.cn");
            // 获得urlConnecion
            URLConnection connection = url.openConnection();
            // 打开连接
            connection.connect();
            InputStream in = connection.getInputStream();
            //现在对jmockit.cn服务器返回的数据进行mock
            new Expectations(in) {
                {
                    in.read();
                    // -1表示流数据结束了
                    result = new int[]{1, 2, 3, 4, 5, -1};
                }
            };
            // 读取jmockit.cn服务器返回的内容，如果没有上面的mock,返回将是实际的内容
            Assert.assertEquals(1, in.read());
            Assert.assertEquals(2, in.read());
            Assert.assertEquals(3, in.read());
            Assert.assertEquals(4, in.read());
            Assert.assertEquals(5, in.read());
            Assert.assertEquals(-1, in.read());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}