package com.qinchy.jmockitdemo;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

/**
 * 用MockUp来mock类
 */
public class ClassMockingByMockUpTest {

//    @BeforeClass
//    /**
//     * 加载AnOrdinaryClass类的native方法的native实现
//     */
//    public static void loadNative() throws Throwable {
//        JNITools.loadNative();
//    }

    @Test
    public void testClassMockingByMockUp() {

        // 模拟MockUp
        new AnOrdinaryClassMockUp();

        AnOrdinaryClass instance = new AnOrdinaryClass();

        // 静态方法被mock了
        Assert.assertEquals(10, AnOrdinaryClass.staticMethod());

        // 普通方法被mock了
        Assert.assertEquals(20, instance.ordinaryMethod());

        // final方法被mock了
        Assert.assertEquals(30, instance.finalMethod());

//        // native方法被mock了
//        Assert.assertEquals(40, instance.navtiveMethod());

        // private方法被mock了
        Assert.assertEquals(50, instance.callPrivateMethod());
    }

    // AnOrdinaryClass的MockUp类，继承MockUp即可
    public static class AnOrdinaryClassMockUp extends MockUp<AnOrdinaryClass> {
        // Mock静态方法
        @Mock
        public static int staticMethod() {
            return 10;
        }

        // Mock普通方法
        @Mock
        public int ordinaryMethod() {
            return 20;
        }

        @Mock
        // Mock final方法
        public final int finalMethod() {
            return 30;
        }

        // Mock native方法
//        @Mock
//        public int navtiveMethod() {
//            return 40;
//        }

        // Mock private方法
        @Mock
        private int privateMethod() {
            return 50;
        }
    }
}