package com.project.data.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.project.data.generator.service.ExecutorService;
import com.project.data.generator.service.MappingConverter;

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

    @Bean
    public ConversionService conversionService(MappingConverter mappingConverter)
    {
        ConversionService conversionService = new DefaultConversionService();
        ((DefaultConversionService) conversionService).addConverter(mappingConverter);
        return conversionService;
    }

}
