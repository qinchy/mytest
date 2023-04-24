package com.qinchy.dynamiceval.aspect;

import com.qinchy.dynamiceval.annotation.OperaLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect // 1.日志切面类
@Component
@Slf4j
public class OperaLogAspect {

    /**
     * 2. PointCut表示这是一个切点，@annotation表示这个切点切到一个注解上，后面带该注解的全类名
     * logPointCut()代表切点名称，这里的名称是operaLogXX，上面@annotation中的名称应该与这里保持一致
     *
     * @param operaLogXX 注解的名称
     */
    @Pointcut("@annotation(operaLogXX)")    // 这里的operaLogXX要与方法中形参保持一致
    public void logPointCut(OperaLog operaLogXX) {
    }

    // 3. 环绕通知
    @Around("logPointCut(operaLogXX)")   // 这里的operaLogXX要与方法中形参保持一致
    public void logAround(ProceedingJoinPoint joinPoint, OperaLog operaLogXX) {
        // 继续执行方法
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        saveLog(joinPoint, operaLogXX);
    }

    /**
     * 保存日志
     *
     * @param joinPoint
     * @param operaLog
     */
    private void saveLog(ProceedingJoinPoint joinPoint, OperaLog operaLog) {
        String msg = getMsg(joinPoint, operaLog.message());
        String ip = getIp(joinPoint, operaLog);
        log.info("msg=" + msg);
        log.info("ip =" + ip);
    }

    /**
     * 通过使用SPEL表达式绑定动态变量参数值的形式获取ip
     *
     * @param joinPoint
     * @param operaLog
     * @return
     */
    private String getIp(ProceedingJoinPoint joinPoint, OperaLog operaLog) {
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        // 参数值
        Object[] args = joinPoint.getArgs();

        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();
        // 参数名称
        String[] paraNameArr = localVariableTable.getParameterNames(method);

        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            log.info("paraNameArr[i]={} , args[i]={}", paraNameArr[i], args[i]);
            context.setVariable(paraNameArr[i], args[i]);
        }

        String ip = "";
        // 注解中的SpEL表达式：#{ip}
        String param = operaLog.ip();
        // 使用变量方式传入业务动态数据
        if (param.matches("^#.*.$")) {
            ip = parser.parseExpression(param).getValue(context, String.class);
        }
        return ip;
    }

    /**
     * 操作详情组装
     *
     * @param joinPoint
     * @param msg
     * @return
     */
    private String getMsg(ProceedingJoinPoint joinPoint, String msg) {
        //获取类的字节码对象，通过字节码对象获取方法信息
        Class<?> targetCls = joinPoint.getTarget().getClass();
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String className = ms.getDeclaringType().getSimpleName();
        String targetClsName = targetCls.getName();
        System.out.println("targetClsName:" + targetClsName);
        // 获取方法名称
        String methodName = ms.getName();
        String[] parameterName = ms.getParameterNames();
        Map<String, String> map = new LinkedHashMap<>();
        for (String o : parameterName) {
            map.put(o, "");
        }
        StringBuilder sb2 = new StringBuilder();
        Class[] parameterType = ms.getParameterTypes();
        for (Class o : parameterType) {
            sb2.append(o.getName() + "; ");
        }
        System.out.println("parameterType[]:" + sb2);
        String declaringTypeName = ms.getDeclaringTypeName();
        System.out.println("declaringTypeName:" + declaringTypeName);
        //获取目标方法上的注解指定的操作名称
        Method targetMethod = null;
        try {
            targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println("targetMethod:" + targetMethod);
        ms.getParameterNames();
        // 获取入参
        Object[] param = joinPoint.getArgs();
        for (Object o : param) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (StringUtils.isEmpty(entry.getValue())) {
                    entry.setValue(o.toString());
                    break;
                }
            }
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("进入：").append(className).append("类[").append(methodName).append("]方法->").append(msg);
        sb.append(" 参数：{");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

}

