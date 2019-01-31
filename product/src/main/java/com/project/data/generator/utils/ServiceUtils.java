package com.project.data.generator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Operations;
import com.project.data.generator.handler.RedisExecutor;
import com.project.data.generator.handler.StringOperationExecutor;

@Component
public class ServiceUtils
{
    @Autowired
    private StringOperationExecutor stringOperationExecutor;

    public RedisExecutor getExecutor(String operation) throws Exception
    {

        switch (Operations.fromString(operation))
        {
            case String:
                return stringOperationExecutor;
        }

        return null;
    }
}
