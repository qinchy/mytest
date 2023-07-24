package com.qinchy.combinyml.controller;

import com.qinchy.combinyml.config.GatewayConfig;
import com.qinchy.combinyml.config.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Value("${aa}")
    private String aa;

    @Value("${mm}")
    private String mm;

    @Value("${nn}")
    private String nn;

    @Autowired
    private GatewayConfig gatewayConfig;

    @Autowired
    private Operator operator;

    @GetMapping("/getNames")
    public String getNames() {
        System.out.println(operator.getNames());
        System.out.println(gatewayConfig);
        return aa + "~" + mm + "~" + nn;
    }
}
