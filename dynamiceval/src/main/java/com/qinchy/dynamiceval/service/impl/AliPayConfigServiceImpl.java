package com.qinchy.dynamiceval.service.impl;

import com.qinchy.dynamiceval.config.AliPayConfig;
import com.qinchy.dynamiceval.service.AliPayConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("aliPayConfigService")
public class AliPayConfigServiceImpl implements AliPayConfigService {

    @Autowired
    AliPayConfig aliPayConfig;

    @Override
    public String getAlipayConfig() {
        return aliPayConfig.getAppId();
    }
}
