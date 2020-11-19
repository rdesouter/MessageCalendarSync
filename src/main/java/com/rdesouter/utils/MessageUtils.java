package com.rdesouter.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MessageUtils {

    @Autowired
    private static Environment env;

    public static String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }



}
