package com.project.data.generator.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Operations;
import com.project.data.generator.handler.DeleteKeyOperationExecutor;
import com.project.data.generator.handler.HashMapOperationExecutor;
import com.project.data.generator.handler.ListOperationExecutor;
import com.project.data.generator.handler.RedisExecutor;
import com.project.data.generator.handler.StringOperationExecutor;

@Component
public class ServiceUtils
{
    @Autowired
    private StringOperationExecutor stringOperationExecutor;

    @Autowired
    private ListOperationExecutor listOperationExecutor;

    @Autowired
    private HashMapOperationExecutor hashMapOperationExecutor;

    @Autowired
    private DeleteKeyOperationExecutor deleteKeyOperationExecutor;

    public RedisExecutor getExecutor(String operation) throws Exception
    {

        switch (Operations.fromString(operation))
        {
            case String:
                return stringOperationExecutor;
            case List:
                return listOperationExecutor;
            case HashMap:
                return hashMapOperationExecutor;
            case Delete:
                return deleteKeyOperationExecutor;
        }

        return null;
    }
}
