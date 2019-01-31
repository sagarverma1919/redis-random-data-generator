package com.project.data.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.project.data.generator.service.ExecutorService;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(basePackages = "com.project.data.generator")
public class Application
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args).getBean(ExecutorService.class).process();
    }

}
