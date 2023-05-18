package com.qinchy.jmockitdemo;

import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpSession;

/**
 * Mocked注解用途:当@Mocked修饰一个接口/抽象类时
 *
 * <p>Mocked功能总结 通过上述例子，可以看出：@Mocked修饰的类/接口，是告诉JMockit，帮我生成一个Mocked对象，这个对象方法（包含静态方法)返回默认值。
 * 即如果返回类型为原始类型(short,int,float,double,long)就返回0，如果返回类型为String就返回null，如果返回类型是其它引用类型，则返回这个引用类型的Mocked对象（这一点，是个递归的定义，需要好好理解一下）。
 *
 * <p>什么测试场景，我们要使用@Mocked
 * 当我们的测试程序依赖某个接口时，用@Mocked非常适合了。只需要@Mocked一个注解，JMockit就能帮我们生成这个接口的实例。
 * 比如在分布式系统中，我们的测试程序依赖某个接口的实例是在远程服务器端时，我们在本地构建是非常困难的，此时就交给@Mocked，就太轻松啦!
 */
public class MockedInterfaceTest {

    /**
     * 加上了JMockit的API @Mocked, JMockit会帮我们实例化这个对象，尽管这个对象的类型是一个接口，不用担心它为null
     */
    @Mocked
    HttpSession session;

    /**
     * 当@Mocked作用于interface
     */
    @Test
    public void testMockedInterface() {
        // （返回类型为String）也不起作用了，返回了null
        Assert.assertNull(session.getId());

        // （返回类型为原始类型）也不起作用了，返回了0
        Assert.assertEquals(0L, session.getCreationTime());

        // (返回类型为原非始类型，非String，返回的对象不为空，这个对象也是JMockit帮你实例化的，同样这个实例化的对象也是一个Mocked对象)
        Assert.assertNotNull(session.getServletContext());

        // Mocked对象返回的Mocked对象，（返回类型为String）的方法也不起作用了，返回了null
        Assert.assertNull(session.getServletContext().getContextPath());
    }
}

// @Mocked功能总结
//
//    通过上述例子，可以看出：@Mocked修饰的类/接口，是告诉JMockit，帮我生成一个Mocked对象，这个对象方法（包含静态方法)返回默认值。
//        即如果返回类型为原始类型(short,int,float,double,long)就返回0，如果返回类型为String就返回null，如果返回类型是其它引用类型，则返回这个引用类型的Mocked对象（这一点，是个递归的定义，需要好好理解一下）。
//
//
// 什么测试场景，我们要使用@Mocked
//
//    当我们的测试程序依赖某个接口时，用@Mocked非常适合了。只需要@Mocked一个注解，JMockit就能帮我们生成这个接口的实例。
//        比如在分布式系统中，我们的测试程序依赖某个接口的实例是在远程服务器端时，我们在本地构建是非常困难的，此时就交给@Mocked，就太轻松啦!