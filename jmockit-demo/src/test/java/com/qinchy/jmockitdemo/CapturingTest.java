package com.qinchy.jmockitdemo;

import mockit.Capturing;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Capturing注解用途 <p>
 * 什么测试场景用@Capturing:
 * 我们只知道父类或接口时，但我们需要控制它所有子类的行为时，子类可能有多个实现（可能有人工写的，也可能是AOP代理自动生成时）。就用@Capturing。
 */
public class CapturingTest {

    // 测试用户ID
    long testUserId = 123456L;

    // 权限检验类，可能是人工写的
    IPrivilege privilegeManager1 = new IPrivilege() {
        @Override
        public boolean isAllow(long userId) {
            return userId != testUserId;
        }
    };

    // 权限检验类，可能是JDK动态代理生成。我们通常AOP来做权限校验。
    IPrivilege privilegeManager2 = (IPrivilege) Proxy.newProxyInstance(IPrivilege.class.getClassLoader(), new Class[]{IPrivilege.class}, new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return (long) args[0] != testUserId;
        }
    });

    /**
     * 有Cautring情形
     *
     * @param privilegeManager
     */
    @Test
    public void testCaputring(@Capturing IPrivilege privilegeManager) {
        // 加上了JMockit的API @Capturing,
        // JMockit会帮我们实例化这个对象，它除了具有@Mocked的特点，还能影响它的子类/实现类
        new Expectations() {
            {
                // 对IPrivilege的所有实现类录制，假设测试用户有权限
                privilegeManager.isAllow(testUserId);
                result = true;
            }
        };

        // 不管权限校验的实现类是哪个，这个测试用户都有权限
        Assert.assertTrue(privilegeManager1.isAllow(testUserId));
        Assert.assertTrue(privilegeManager2.isAllow(testUserId));
    }

    /**
     * 没有Cautring情形
     */
    @Test
    public void testWithoutCaputring() {
        // 不管权限校验的实现类是哪个，这个测试用户没有权限
        Assert.assertFalse(privilegeManager1.isAllow(testUserId));
        Assert.assertFalse(privilegeManager2.isAllow(testUserId));
    }
}