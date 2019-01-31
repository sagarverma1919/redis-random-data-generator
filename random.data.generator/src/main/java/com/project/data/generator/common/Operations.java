package com.project.data.generator.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Operations
{
    String("string"),
    List("list"),
    HashMap("hashMap");


    private static final Logger LOG = LoggerFactory.getLogger(Operations.class);

    private final String operation;

    Operations(java.lang.String operation)
    {
        this.operation = operation;
    }

    public static Operations fromString(String value) throws Exception
    {
        for (Operations e : Operations.values())
        {
            if (e.operation.equalsIgnoreCase(value))
            {
                return e;
            }
        }

        LOG.error("UNKNOWN OPERATION");
        throw new Exception();
    }
}
