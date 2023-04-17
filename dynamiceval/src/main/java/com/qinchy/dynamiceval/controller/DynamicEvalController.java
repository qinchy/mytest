package com.qinchy.dynamiceval.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qinchy.dynamiceval.config.AliPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class DynamicEvalController {

    /**
     * $表示获取配置参数
     */
    @Value("${handler-expression:[{\"action\": \"modify\",\"jsonpath\": \"$.comment\",\"value\": \"#{'T(Math).random()'}\"}]}")
    private String handlerExpress;

    /**
     * #表示字面值
     */
    @Value("#{16}")
    private Integer threadCount;

    @Autowired
    AliPayConfig aliPayConfig;

    @PostMapping("/echo")
    public Map echo(@RequestBody Map param) throws Exception {
        log.info(handlerExpress);
        log.info("appId={}", aliPayConfig.getAppId());
        log.info("threadCount={}", threadCount);

        DocumentContext documentContext = JsonPath.parse(param);

        JSONArray jsonArray = JSON.parseArray(handlerExpress);
        if (jsonArray.size() == 0) {
            throw new Exception("配置为空");
        }

        EvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("aliPayConfig", aliPayConfig);

        //创建ExpressionParser解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JsonPath jsonPath = JsonPath.compile(jsonObject.getString("jsonpath"));
            String action = jsonObject.getString("action");
            String valueOrExpress = jsonObject.getString("value");
            Object value = valueOrExpress;
            try {
                exp = parser.parseExpression(valueOrExpress);
                value = exp.getValue();
            } catch (ParseException e) {
                log.error("解析SpEL表达式{}异常，值可能是字面文本值", valueOrExpress, e);
            } catch (SpelEvaluationException e){
                log.error("取值SpEL表达式{}异常，值可能是字面文本值", valueOrExpress, e);
            }

            // 给指定路径设置一个值(无此路径时不添加)
            if ("addKey".equalsIgnoreCase(action)) {
                documentContext.put(jsonPath, jsonObject.getString("key"), value);
            }

            // 添加或者修改指定路径的值(父路径不存在时不添加)
            if ("modify".equalsIgnoreCase(action)) {
                documentContext.set(jsonPath, value);
            }

            // 添加一个值到数组(无此路径时不添加)
            if ("addItem".equalsIgnoreCase(action)) {
                documentContext.add(jsonPath, value);
            }

            // 添加一个json到数组(无此路径时不添加)
            if ("addJsonItem".equalsIgnoreCase(action)) {
                documentContext.add(jsonPath, value);
            }
        }

        return JSON.parseObject(documentContext.jsonString(), Map.class);
    }
}
