package com.qinchy.jmockitdemo;

import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 用Expectations来Mock Spring Bean
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringBeanMockingByExpectationsTest {
    // 注入Spring Bean，Mock这个实例，就达到了Mock Spring Bean的目的
    @Resource
    AnOrdinaryClass anOrdinaryBean;

//    /**
//     * 加载AnOrdinaryClass类的native方法的native实现
//     *
//     * @throws Throwable
//     */
//    @BeforeClass
//    public static void loadNative() throws Throwable {
//        JNITools.loadNative();
//    }

    @Test
    public void testSpringBeanMockingByExpectation() {
        // 直接把实例传给Expectations的构造函数即可Mock这个实例
        new Expectations(anOrdinaryBean) {
            {
                // 尽管这里也可以Mock静态方法，但不推荐在这里写。静态方法的Mock应该是针对类的
                // mock普通方法
                anOrdinaryBean.ordinaryMethod();
                result = 20;

                // mock final方法
                anOrdinaryBean.finalMethod();
                result = 30;
                // native, private方法无法用Expectations来Mock
            }
        };
        Assert.assertEquals(1, AnOrdinaryClass.staticMethod());
        Assert.assertEquals(20, anOrdinaryBean.ordinaryMethod());
        Assert.assertEquals(30, anOrdinaryBean.finalMethod());

//        // 用Expectations无法mock native方法
//        Assert.assertEquals(4, anOrdinaryBean.navtiveMethod());

        // 用Expectations无法mock private方法
        Assert.assertEquals(5, anOrdinaryBean.callPrivateMethod());
    }
}
