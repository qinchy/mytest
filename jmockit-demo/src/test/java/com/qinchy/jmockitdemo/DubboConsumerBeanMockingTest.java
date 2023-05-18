package com.qinchy.jmockitdemo;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//dubbo消费bean Mock
@SuppressWarnings({"unchecked", "rawtypes"})
@ContextConfiguration(locations = {"classpath:dubbo-consumer.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class DubboConsumerBeanMockingTest {
    // 现在你使用的dubbo消费bean就是本地mock过的了，并不是指向远程dubbo服务的bean了
    @Resource
    MailService mailService;

    // 这里要@BeforeClass,因为要抢在spring加载dubbo前，对dubbo的消费工厂bean
    // ReferenceBean进行mock，不然dubbo可能因为连上不zk或无法找不
    // 服务的提供者等原因而无法初始化的，进而，单元测试运行不下去
    @BeforeClass
    public static void mockDubbo() {
        // 你准备mock哪个消费bean
        // 比如要对dubbo-consumber.xml里配置的cn.jmockit.demos.MailService这个消费bean进行mock
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("com.qinchy.jmockitdemo.MailService", new MockUp(MailService.class) {
            // 在这里书写对这个消费bean进行mock的mock逻辑，想mock哪个方法，就自行添加，注意方法一定要加上@Mock注解哦
            @Mock
            public boolean sendMail(long userId, String content) {
                // 单元测试时，不需要调用邮件服务器发送邮件，这里统一mock邮件发送成功
                return true;
            }
        }.getMockInstance());
        // 如果要Mock其它的消费bean，自行添加,mockMap.put.....如上
        new DubboConsumerBeanMockUp(mockMap);
    }

    @Test
    public void testSendMail() {
        long userId = 123456;
        String content = "test mail content";
        Assert.isTrue(mailService.sendMail(userId, content));
    }
}

//dubbo消费bean的MockUp(伪类）
@SuppressWarnings("rawtypes")
class DubboConsumerBeanMockUp extends MockUp<ReferenceBean> {
    // 自定义的消费bean mock对象
    private Map<String, Object> mockMap;

    public DubboConsumerBeanMockUp() {
    }

    public DubboConsumerBeanMockUp(Map<String, Object> mockMap) {
        this.mockMap = mockMap;
    }

    // 对ReferenceBean的getObject方法的Mock
    @SuppressWarnings("unchecked")
    @Mock
    public Object getObject(Invocation inv) throws Exception {
        ReferenceBean ref = inv.getInvokedInstance();
        String interfaceName = ref.getInterface();
        Object mock = mockMap.get(interfaceName);
        if (mock != null) {
            return mock;
        }
        return (new MockUp(Class.forName(interfaceName)) {
        }).getMockInstance();
    }
}