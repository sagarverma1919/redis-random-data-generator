package com.project.data.generator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MappingConverter implements Converter<String, List<String>>
{
    @Override
    public List<String> convert(String source)
    {
        List<String> response = new ArrayList<>();
        if (!StringUtils.isBlank(source))
        {
            response.addAll(Stream.of(source.split(","))
                                    .collect(Collectors.toList()));
        }
        return response;
    }
}
