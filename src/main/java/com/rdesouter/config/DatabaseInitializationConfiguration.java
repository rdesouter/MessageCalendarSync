package com.rdesouter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class DatabaseInitializationConfiguration {

    @Autowired
    void initDatabase(AppConfiguration appConfiguration){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource[] scripts = new Resource[] { resourceLoader.getResource("classpath:schema.sql"),
                resourceLoader.getResource("classpath:data.sql") };
        System.out.println(scripts.length);
    }
}
